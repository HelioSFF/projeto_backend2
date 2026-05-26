package com.projeto_backend.ClinMed.domain.especialidade.repository;

import com.projeto_backend.ClinMed.domain.especialidade.entity.EspecialidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspecialidadeRepository extends JpaRepository<EspecialidadeEntity, Long> {
    Optional<EspecialidadeEntity> findByNome(String nome);
}
