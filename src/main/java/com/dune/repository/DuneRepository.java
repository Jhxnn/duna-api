package com.dune.repository;

import com.dune.model.Dune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DuneRepository extends JpaRepository<Dune, UUID> {
}
