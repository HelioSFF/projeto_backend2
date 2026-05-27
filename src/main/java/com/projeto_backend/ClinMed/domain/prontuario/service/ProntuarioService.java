package com.projeto_backend.ClinMed.domain.prontuario.service;

import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.prontuario.dto.ProntuarioRequestDTO;
import com.projeto_backend.ClinMed.domain.prontuario.dto.ProntuarioResponseDTO;
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

    private ProntuarioResponseDTO toDTO(ProntuarioEntity p){

        return new ProntuarioResponseDTO(p.getSintomas(), p.getDiagnostico(), p.getPrescricao(),
                p.getObservacoesPrivadas(), p.getPaciente().getId(), p.getConsulta().getId());
    }

    // Retorna todos os prontuarios
    @Transactional(readOnly = true)
    public List<ProntuarioResponseDTO> listarTodos() {
        return prontuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    // Busca prontuario por ID
    @Transactional(readOnly = true)
    public ProntuarioResponseDTO buscarPorId(Long id) {
        return toDTO(prontuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prontuário não encontrado com o ID: " + id)));
    }

    @Transactional(readOnly = true)
    public List<ProntuarioResponseDTO> buscarPorPaciente(Long pacienteId) {
        return prontuarioRepository.findByPacienteId(pacienteId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public ProntuarioResponseDTO registrar(ProntuarioRequestDTO dto) {
        ConsultaEntity consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com o ID: " + dto.getConsultaId()));
        if (prontuarioRepository.existsByConsultaId(dto.getConsultaId())) {
            throw new BusinessException("Já existe um prontuário registrado para esta consulta.");
        }
        if (consulta.getStatus() != StatusConsulta.CONFIRMADA && consulta.getStatus() != StatusConsulta.REALIZADA) {
            throw new BusinessException("Só é possível registrar prontuário para consultas CONFIRMADAS.");
        }
        ProntuarioEntity prontuario = new ProntuarioEntity();
        prontuario.setSintomas(dto.getSintomas());
        prontuario.setDiagnostico(dto.getDiagnostico());
        prontuario.setPrescricao(dto.getPrescricao());
        prontuario.setObservacoesPrivadas(dto.getObservacoesPrivadas());
        prontuario.setDataRegistro(LocalDateTime.now());
        prontuario.setPaciente(consulta.getPaciente());
        prontuario.setConsulta(consulta);
        ProntuarioEntity salvo = prontuarioRepository.save(prontuario);
        if (consulta.getStatus() != StatusConsulta.REALIZADA) {
            consulta.setStatus(StatusConsulta.REALIZADA);
            consultaRepository.save(consulta);
        }
        return toDTO(salvo);
    }
}