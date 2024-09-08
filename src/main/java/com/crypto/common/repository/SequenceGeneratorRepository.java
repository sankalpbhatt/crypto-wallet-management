package com.crypto.common.repository;


import com.crypto.common.entity.SequenceGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceGeneratorRepository extends JpaRepository<SequenceGenerator, String> {
}