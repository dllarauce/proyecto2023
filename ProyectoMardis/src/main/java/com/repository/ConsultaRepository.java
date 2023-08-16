package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.*;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
}