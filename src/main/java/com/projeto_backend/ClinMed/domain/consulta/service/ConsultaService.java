package com.projeto_backend.ClinMed.domain.consulta.service;

import com.projeto_backend.ClinMed.domain.consulta.dto.ConsultaRequestDTO;
import com.projeto_backend.ClinMed.domain.consulta.dto.ConsultaResponseDTO;
import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.consulta.validator.ConsultaValidator;
import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.medico.service.MedicoService;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.paciente.service.PacienteService;
import com.projeto_backend.ClinMed.exception.BusinessException;
import com.projeto_backend.ClinMed.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ConsultaValidator consultaValidator;

    private ConsultaResponseDTO toDTO(ConsultaEntity c){
        return new ConsultaResponseDTO(
                c.getId(),
                c.getDataHoraInicio(),
                c.getDataHoraFim(),
                c.getStatus(),
                c.getMotivo(),
                c.getPaciente().getNome(),
                c.getMedico().getUsuario().getNome()
        );
    }

    // Metodo para listar todas as consultas
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarTodas() {
        return consultaRepository.findAll().stream().map(this::toDTO).toList();
    }

    // Busca consulta por ID
    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscarPorId(Long id) {
        return toDTO(consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta nao encontrada.")));
    }

    // Metodo principal para agendar uma consulta (Regras de negocio)
    @Transactional
    public ConsultaResponseDTO agendar(ConsultaRequestDTO dto) {
        // Valida se informou paciente e medico
        if (dto.getPacienteId() == null) {
            throw new BusinessException("E necessario informar o ID do paciente.");
        }
        if (dto.getMedicoId() == null) {
            throw new BusinessException("E necessario informar o ID do medico.");
        }

        // 1. Busca as entidades no banco
        PacienteEntity paciente = pacienteService.buscarPorId(dto.getPacienteId());
        MedicoEntity medico = medicoService.buscarPorId(dto.getMedicoId());

        LocalDateTime dataInicio = dto.getDataHoraInicio();
        // Define o fim da consulta somando 30 minutos
        LocalDateTime dataFim = dataInicio.plusMinutes(30);

        consultaValidator.validarAgendamento(paciente, medico, dataInicio, dataFim);

        // Configura dados finais e salva
        ConsultaEntity consulta = ConsultaEntity.builder()
                .paciente(paciente)
                .medico(medico)
                .dataHoraInicio(dataInicio)
                .dataHoraFim(dataFim)
                .status(StatusConsulta.AGENDADA)
                .motivo(dto.getMotivo())
                .build();

        return toDTO(consultaRepository.save(consulta));
    }

    // Metodo para confirmar consulta
    @Transactional
    public ConsultaResponseDTO confirmar(Long id) {
        ConsultaEntity consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));

        // Apenas permite confirmar se estiver agendada
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new BusinessException("Somente consultas com status AGENDADA podem ser confirmadas.");
        }

        consulta.setStatus(StatusConsulta.CONFIRMADA);
        return toDTO(consultaRepository.save(consulta));
    }

    // Metodo para cancelar consulta
    @Transactional
    public ConsultaResponseDTO cancelar(Long id, String motivoCancelamento) {
        ConsultaEntity consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));
        // Valida status da consulta antes de cancelar
        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new BusinessException("Esta consulta ja esta cancelada.");
        }
        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new BusinessException("Nao é possivel cancelar uma consulta que ja foi realizada.");
        }

        // Valida se o motivo foi preenchido
        if (motivoCancelamento == null || motivoCancelamento.trim().isEmpty()) {
            throw new BusinessException("É obrigatório informar o motivo do cancelamento.");
        }

        // Valida se tem mais de 24h de antecedencia
        if (LocalDateTime.now().plusHours(24).isAfter(consulta.getDataHoraInicio())) {
            throw new BusinessException("A consulta so pode ser cancelada com antecedência mínima de 24 horas.");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consulta.setMotivo(motivoCancelamento);
        return toDTO(consultaRepository.save(consulta));
    }
}
