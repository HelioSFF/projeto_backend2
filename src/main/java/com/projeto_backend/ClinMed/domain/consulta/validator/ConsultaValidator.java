package com.projeto_backend.ClinMed.domain.consulta.validator;

import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.paciente.enums.StatusPaciente;
import com.projeto_backend.ClinMed.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ConsultaValidator {

    private final ConsultaRepository consultaRepository;

    public ConsultaValidator(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public void validarAgendamento(PacienteEntity paciente, MedicoEntity medico, LocalDateTime inicio, LocalDateTime fim) {

        validarHorario(inicio);

        validarPacienteAtivo(paciente);

        validarConflito(medico, paciente, inicio, fim);
    }

    public void validarHorario(LocalDateTime inicio) {
        LocalDateTime agoraMais30 = LocalDateTime.now().plusMinutes(30);

        if (inicio.isBefore(agoraMais30)) {
            throw new BusinessException("A consulta deve ser agendada com antecedência mínima de 30 minutos.");
        }

        if (inicio.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new BusinessException("A clínica não funciona aos domingos.");
        }

        LocalTime hora = inicio.toLocalTime();
        if (hora.isBefore(LocalTime.of(7, 0)) || hora.isAfter(LocalTime.of(18, 30))) {
            throw new BusinessException("Horário inválido para consulta.");
        }
    }

    public void validarPacienteAtivo(PacienteEntity paciente) {
        if (paciente.getStatus() != StatusPaciente.ATIVO) {
            throw new BusinessException("Paciente inativo ou suspenso.");
        }
    }

    public void validarConflito(
            MedicoEntity medico,
            PacienteEntity paciente,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        if (consultaRepository.existsOverlappingForMedico(medico.getId(), inicio, fim)) {
            throw new BusinessException("Médico já possui consulta nesse horário.");
        }

        if (consultaRepository.existsOverlappingForPaciente(paciente.getId(), inicio, fim)) {
            throw new BusinessException("Paciente já possui consulta nesse horário.");
        }
    }
}