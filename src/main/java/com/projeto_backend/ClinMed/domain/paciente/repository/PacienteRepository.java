package com.projeto_backend.ClinMed.domain.paciente.repository;

import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {
    Optional<PacienteEntity> findByCpf(String cpf);
    Optional<PacienteEntity> findByEmail(String email);
}
