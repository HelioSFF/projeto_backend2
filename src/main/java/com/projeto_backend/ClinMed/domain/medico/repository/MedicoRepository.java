package com.projeto_backend.ClinMed.domain.medico.repository;

import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<MedicoEntity, Long> {
    boolean existsByEspecialidadeId(Long especialidadeId);
    boolean existsByUsuarioId(Long usuarioId);
    Optional<MedicoEntity> findByUsuarioId(Long usuarioId);
}
