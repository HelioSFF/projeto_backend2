package com.projeto_backend.ClinMed.domain.prontuario.service;

import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.prontuario.entity.ProntuarioEntity;
import com.projeto_backend.ClinMed.domain.prontuario.repository.ProntuarioRepository;
import com.projeto_backend.ClinMed.exception.BusinessException;
import com.projeto_backend.ClinMed.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    // Retorna todos os prontuarios
    @Transactional(readOnly = true)
    public List<ProntuarioEntity> listarTodos() {
        return prontuarioRepository.findAll();
    }

    // Busca prontuario por ID
    @Transactional(readOnly = true)
    public ProntuarioEntity buscarPorId(Long id) {
        return prontuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuario nao encontrado com o ID: " + id));
    }

    // Lista prontuarios de um paciente
    @Transactional(readOnly = true)
    public List<ProntuarioEntity> buscarPorPaciente(Long pacienteId) {
        return prontuarioRepository.findByPacienteId(pacienteId);
    }

    // Registra um novo prontuario (Regras de negocio)
    @Transactional
    public ProntuarioEntity registrar(ProntuarioEntity prontuario) {
        // Valida se informou a consulta
        if (prontuario.getConsulta() == null || prontuario.getConsulta().getId() == null) {
            throw new BusinessException("E necessario informar o ID da consulta.");
        }

        Long consultaId = prontuario.getConsulta().getId();

        ConsultaEntity consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta nao encontrada com o ID: " + consultaId));

        // Regra: evita prontuario duplicado para a mesma consulta
        if (prontuarioRepository.existsByConsultaId(consultaId)) {
            throw new BusinessException("Ja existe um prontuario registrado para esta consulta.");
        }

        // Regra: so permite se a consulta estiver CONFIRMADA
        if (consulta.getStatus() != StatusConsulta.CONFIRMADA && consulta.getStatus() != StatusConsulta.REALIZADA) {
            throw new BusinessException("So e possivel registrar prontuario para consultas CONFIRMADAS.");
        }

        PacienteEntity paciente = consulta.getPaciente();

        // Configura prontuario
        prontuario.setDataRegistro(LocalDateTime.now());
        prontuario.setPaciente(paciente);
        prontuario.setConsulta(consulta);

        ProntuarioEntity prontuarioSalvo = prontuarioRepository.save(prontuario);

        // Regra de negocio: muda status da consulta automaticamente para REALIZADA
        if (consulta.getStatus() != StatusConsulta.REALIZADA) {
            consulta.setStatus(StatusConsulta.REALIZADA);
            consultaRepository.save(consulta);
        }

        return prontuarioSalvo;
    }
}
