package com.projeto_backend.ClinMed.domain.prontuario.repository;

import com.projeto_backend.ClinMed.domain.prontuario.entity.ProntuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProntuarioRepository extends JpaRepository<ProntuarioEntity, Long> {
    List<ProntuarioEntity> findByPacienteId(Long pacienteId);
    boolean existsByConsultaId(Long consultaId);
}
