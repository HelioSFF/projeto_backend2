package com.projeto_backend.ClinMed.domain.paciente.service;

import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.paciente.dto.PacienteRequestDTO;
import com.projeto_backend.ClinMed.domain.paciente.dto.PacienteResponseDTO;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.paciente.enums.StatusPaciente;
import com.projeto_backend.ClinMed.domain.paciente.repository.PacienteRepository;
import com.projeto_backend.ClinMed.exception.BusinessException;
import com.projeto_backend.ClinMed.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    @Lazy
    private ConsultaRepository consultaRepository;

    // Retorna todos os pacientes cadastrados
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listarTodos() {

        return pacienteRepository.findAll().stream()
                .map(p -> new PacienteResponseDTO(p.getNome(), p.getCpf(), p.getDataNascimento(), p.getEmail(), p.getTelefone()))
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        PacienteEntity p = buscarEntidadePorId(id);
        return new PacienteResponseDTO(p.getNome(), p.getCpf(), p.getDataNascimento(), p.getEmail(), p.getTelefone());
    }

    public PacienteEntity buscarEntidadePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID: " + id));
    }

    @Transactional
    public PacienteResponseDTO criar(PacienteRequestDTO dto) {
        if (pacienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BusinessException("CPF já cadastrado.");
        }
        if (dto.getEmail() != null && pacienteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado para outro paciente.");
        }
        PacienteEntity paciente = new PacienteEntity();
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefone(dto.getTelefone());
        paciente.setStatus(StatusPaciente.ATIVO);
        PacienteEntity salvo = pacienteRepository.save(paciente);
        return new PacienteResponseDTO(salvo.getNome(), salvo.getCpf(), salvo.getDataNascimento(), salvo.getEmail(), salvo.getTelefone());
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        PacienteEntity paciente = buscarEntidadePorId(id);
        pacienteRepository.findByCpf(dto.getCpf())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new BusinessException("CPF já cadastrado para outro paciente."); });
        if (dto.getEmail() != null) {
            pacienteRepository.findByEmail(dto.getEmail())
                    .filter(p -> !p.getId().equals(id))
                    .ifPresent(p -> { throw new BusinessException("E-mail já cadastrado para outro paciente."); });
        }
        paciente.setNome(dto.getNome());
        paciente.setCpf(dto.getCpf());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefone(dto.getTelefone());
        PacienteEntity salvo = pacienteRepository.save(paciente);
        return new PacienteResponseDTO(salvo.getNome(), salvo.getCpf(), salvo.getDataNascimento(), salvo.getEmail(), salvo.getTelefone());
    }

    @Transactional
    public void deletar(Long id) {
        PacienteEntity paciente = buscarEntidadePorId(id);
        if (consultaRepository.existsByPacienteId(id)) {
            throw new BusinessException("Não é possivel excluir o paciente pois ele possui consultas cadastradas.");
        }
        pacienteRepository.delete(paciente);
    }
}
