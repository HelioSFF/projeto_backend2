package com.projeto_backend.ClinMed.domain.consulta.service;

import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.medico.service.MedicoService;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.paciente.enums.StatusPaciente;
import com.projeto_backend.ClinMed.domain.paciente.service.PacienteService;
import com.projeto_backend.ClinMed.exception.BusinessException;
import com.projeto_backend.ClinMed.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MedicoService medicoService;

    // Metodo para listar todas as consultas
    @Transactional(readOnly = true)
    public List<ConsultaEntity> listarTodas() {
        return consultaRepository.findAll();
    }

    // Busca consulta por ID
    @Transactional(readOnly = true)
    public ConsultaEntity buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta nao encontrada com o ID: " + id));
    }

    // Metodo principal para agendar uma consulta (Regras de negocio)
    @Transactional
    public ConsultaEntity agendar(ConsultaEntity consulta) {
        // Valida se informou paciente e medico
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
            throw new BusinessException("E necessario informar o ID do paciente.");
        }
        if (consulta.getMedico() == null || consulta.getMedico().getId() == null) {
            throw new BusinessException("E necessario informar o ID do medico.");
        }

        // 1. Busca as entidades no banco
        PacienteEntity paciente = pacienteService.buscarPorId(consulta.getPaciente().getId());
        MedicoEntity medico = medicoService.buscarPorId(consulta.getMedico().getId());

        LocalDateTime dataInicio = consulta.getDataHoraInicio();
        
        // Define o fim da consulta somando 30 minutos
        LocalDateTime dataFim = dataInicio.plusMinutes(30); 
        consulta.setDataHoraFim(dataFim);

        // 2. Valida se a data e futura e tem mais de 30 minutos de antecedencia
        LocalDateTime agoraMais30Min = LocalDateTime.now().plusMinutes(30);
        if (dataInicio.isBefore(agoraMais30Min)) {
            throw new BusinessException("A consulta deve ser agendada com antecedencia minima de 30 minutos.");
        }

        // 3. Valida se a clinica esta aberta (Segunda a Sabado das 7h as 19h)
        if (dataInicio.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new BusinessException("A clinica nao funciona aos domingos.");
        }

        int hora = dataInicio.getHour();
        int minuto = dataInicio.getMinute();
        if (hora < 7 || hora > 18 || (hora == 18 && minuto > 30)) {
            throw new BusinessException("O horario da consulta deve ser das 07:00 as 19:00 (ultimo agendamento as 18:30).");
        }

        // 4. Valida se o paciente nao esta suspenso
        if (paciente.getStatus() != StatusPaciente.ATIVO) {
            throw new BusinessException("Nao e possivel agendar consulta para um paciente SUSPENSO.");
        }

        // 5. Valida se o medico ja nao tem consulta no mesmo horario
        if (consultaRepository.existsOverlappingForMedico(medico.getId(), dataInicio, dataFim)) {
            throw new BusinessException("O medico selecionado ja possui outra consulta agendada neste mesmo horario.");
        }

        // 6. Valida se o paciente ja nao tem consulta no mesmo horario
        if (consultaRepository.existsOverlappingForPaciente(paciente.getId(), dataInicio, dataFim)) {
            throw new BusinessException("O paciente ja possui outra consulta agendada neste mesmo horario.");
        }

        // Configura dados finais e salva
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setStatus(StatusConsulta.AGENDADA);

        return consultaRepository.save(consulta);
    }

    // Metodo para confirmar consulta
    @Transactional
    public ConsultaEntity confirmar(Long id) {
        ConsultaEntity consulta = buscarPorId(id);

        // Apenas permite confirmar se estiver agendada
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new BusinessException("Somente consultas com status AGENDADA podem ser confirmadas.");
        }

        consulta.setStatus(StatusConsulta.CONFIRMADA);
        return consultaRepository.save(consulta);
    }

    // Metodo para cancelar consulta
    @Transactional
    public ConsultaEntity cancelar(Long id, String motivoCancelamento) {
        ConsultaEntity consulta = buscarPorId(id);

        // Valida status da consulta antes de cancelar
        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new BusinessException("Esta consulta ja esta cancelada.");
        }
        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new BusinessException("Nao e possivel cancelar uma consulta que ja foi realizada.");
        }

        // Valida se o motivo foi preenchido
        if (motivoCancelamento == null || motivoCancelamento.trim().isEmpty()) {
            throw new BusinessException("E obrigatorio informar o motivo do cancelamento.");
        }

        // Valida se tem mais de 24h de antecedencia
        if (LocalDateTime.now().plusHours(24).isAfter(consulta.getDataHoraInicio())) {
            throw new BusinessException("A consulta so pode ser cancelada com antecedencia minima de 24 horas.");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consulta.setMotivo(motivoCancelamento);
        return consultaRepository.save(consulta);
    }
}
