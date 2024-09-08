package com.crypto.common.repository;


import com.crypto.common.entity.SequenceGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SequenceGeneratorRepository extends JpaRepository<SequenceGenerator, String> {

    Optional<SequenceGenerator> findBySequenceType(String name);
}