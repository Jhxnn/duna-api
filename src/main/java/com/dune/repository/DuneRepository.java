package com.dune.repository;

import com.dune.model.Dune;
import com.dune.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DuneRepository extends JpaRepository<Dune, UUID> {

    Dune findByUser(User user);
}
