package com.dune.service;


import com.dune.dto.LoginDto;
import com.dune.dto.LoginResponseDto;
import com.dune.dto.UserRegisterDto;
import com.dune.dto.UserResponseDto;
import com.dune.exception.BadRequestException;
import com.dune.exception.ConflictException;
import com.dune.exception.UnauthorizedException;
import com.dune.model.User;
import com.dune.model.enums.UserRole;
import com.dune.repository.UserRepository;
import com.dune.security.TokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, EmailService emailService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public UserResponseDto findById(UUID id) {
        User userRegister = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userRegister.getRole() == UserRole.ADMIN) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

            return new UserResponseDto(user.getUserId(), user.getName(), user.getEmail());
        } else {
            if (!userRegister.getUserId().equals(id)) {
                throw new BadRequestException("Você não tem permissão para visualizar este usuário");
            }
            return new UserResponseDto(userRegister.getUserId(), userRegister.getName(), userRegister.getEmail());
        }
    }


    public LoginResponseDto login(LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword =
                    new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

            var auth = this.authenticationManager.authenticate(usernamePassword);
            String token = tokenService.generateToken((User) auth.getPrincipal());
            User user = (User) auth.getPrincipal();
            return new LoginResponseDto(token,user.getName());

        } catch (Exception e) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }
    }

    public List<UserResponseDto> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserResponseDto> userResponseDto = new ArrayList<>();

        if (user.getRole() == UserRole.ADMIN) {
            List<User> users = userRepository.findAll();

            for (User u : users) {
                userResponseDto.add(new UserResponseDto(u.getUserId(), u.getName(), u.getEmail()));
            }
            return userResponseDto;
        }
        throw new UnauthorizedException("Você não tem permissão para visualizar todos os usuários");
    }

    public UserResponseDto register(UserRegisterDto registerDto) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (userRepository.existsByEmail(registerDto.email())) {
            throw new ConflictException("O e-mail já está cadastrado");
        }

        if (!registerDto.email().matches(emailRegex)) {
            throw new BadRequestException("Formato de e-mail inválido");
        }

        String encryptedPass = new BCryptPasswordEncoder().encode(registerDto.password());
        User user = new User();
        BeanUtils.copyProperties(registerDto, user);

        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(encryptedPass);

        emailService.sendTextEmail(
                user.getEmail(),
                "Conta Criada - Delp",
                "Sua conta foi criada com sucesso.\nBem-vindo à Delp!"
        );

        userRepository.save(user);

        return new UserResponseDto(user.getUserId(), user.getName(), user.getEmail());
    }
}