package com.projeto_backend.ClinMed.domain.consulta.repository;

import com.projeto_backend.ClinMed.domain.consulta.dto.ConsultaResponseDTO;
import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Long> {

    @Query("SELECT COUNT(c) > 0 FROM ConsultaEntity c " +
           "WHERE c.medico.id = :medicoId " +
           "AND c.status <> 'CANCELADA' " +
           "AND c.dataHoraInicio < :dataHoraFim " +
           "AND c.dataHoraFim > :dataHoraInicio")
    boolean existsOverlappingForMedico(
            @Param("medicoId") Long medicoId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim
    );

    @Query("SELECT COUNT(c) > 0 FROM ConsultaEntity c " +
           "WHERE c.paciente.id = :pacienteId " +
           "AND c.status <> 'CANCELADA' " +
           "AND c.dataHoraInicio < :dataHoraFim " +
           "AND c.dataHoraFim > :dataHoraInicio")
    boolean existsOverlappingForPaciente(
            @Param("pacienteId") Long pacienteId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim
    );

    boolean existsByPacienteId(Long pacienteId);
    boolean existsByMedicoId(Long medicoId);
    List<ConsultaEntity> findByPacienteId(Long pacienteId);
    List<ConsultaEntity> findByMedicoId(Long medicoId);
}
