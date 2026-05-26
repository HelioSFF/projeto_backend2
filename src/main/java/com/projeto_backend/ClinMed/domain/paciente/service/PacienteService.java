package com.projeto_backend.ClinMed.domain.paciente.service;

import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
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
    public List<PacienteEntity> listarTodos() {
        return pacienteRepository.findAll();
    }

    // Busca paciente por id
    @Transactional(readOnly = true)
    public PacienteEntity buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente nao encontrado com o ID: " + id));
    }

    // Cria um novo paciente
    @Transactional
    public PacienteEntity criar(PacienteEntity paciente) {
        // Valida se o CPF ja existe
        if (pacienteRepository.findByCpf(paciente.getCpf()).isPresent()) {
            throw new BusinessException("CPF ja cadastrado.");
        }

        // Valida se o E-mail ja existe
        if (paciente.getEmail() != null && !paciente.getEmail().trim().isEmpty()) {
            if (pacienteRepository.findByEmail(paciente.getEmail()).isPresent()) {
                throw new BusinessException("E-mail ja cadastrado para outro paciente.");
            }
        }

        // Define status inicial como ATIVO
        paciente.setStatus(paciente.getStatus() != null ? paciente.getStatus() : StatusPaciente.ATIVO);

        return pacienteRepository.save(paciente);
    }

    // Atualiza um paciente
    @Transactional
    public PacienteEntity atualizar(Long id, PacienteEntity dadosNovos) {
        PacienteEntity paciente = buscarPorId(id);

        pacienteRepository.findByCpf(dadosNovos.getCpf())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new BusinessException("CPF ja cadastrado para outro paciente.");
                });

        if (dadosNovos.getEmail() != null && !dadosNovos.getEmail().trim().isEmpty()) {
            pacienteRepository.findByEmail(dadosNovos.getEmail())
                    .filter(p -> !p.getId().equals(id))
                    .ifPresent(p -> {
                        throw new BusinessException("E-mail ja cadastrado para outro paciente.");
                    });
        }

        paciente.setNome(dadosNovos.getNome());
        paciente.setCpf(dadosNovos.getCpf());
        paciente.setDataNascimento(dadosNovos.getDataNascimento());
        paciente.setEmail(dadosNovos.getEmail());
        paciente.setTelefone(dadosNovos.getTelefone());
        if (dadosNovos.getStatus() != null) {
            paciente.setStatus(dadosNovos.getStatus());
        }

        return pacienteRepository.save(paciente);
    }

    // Deleta um paciente
    @Transactional
    public void deletar(Long id) {
        PacienteEntity paciente = buscarPorId(id);

        // Regra de negocio: nao permite deletar paciente com consultas cadastradas
        if (consultaRepository.existsByPacienteId(id)) {
            throw new BusinessException("Nao e possivel excluir o paciente pois ele possui consultas cadastradas.");
        }

        pacienteRepository.delete(paciente);
    }
}
