package com.projeto_backend.ClinMed.domain.especialidade.service;

import com.projeto_backend.ClinMed.domain.especialidade.dto.EspecialidadeRequestDTO;
import com.projeto_backend.ClinMed.domain.especialidade.dto.EspecialidadeResponseDTO;
import com.projeto_backend.ClinMed.domain.especialidade.entity.EspecialidadeEntity;
import com.projeto_backend.ClinMed.domain.especialidade.repository.EspecialidadeRepository;
import com.projeto_backend.ClinMed.domain.medico.repository.MedicoRepository;
import com.projeto_backend.ClinMed.exception.BusinessException;
import com.projeto_backend.ClinMed.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    @Lazy
    private MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public List<EspecialidadeResponseDTO> listarTodas() {
        return especialidadeRepository.findAll().stream()
                .map(e -> new EspecialidadeResponseDTO(e.getId(), e.getNome(), e.getDescricao()))
                .toList();
    }

    @Transactional(readOnly = true)
    public EspecialidadeResponseDTO buscarPorId(Long id) {
        EspecialidadeEntity e = buscarEntidadePorId(id);
        return new EspecialidadeResponseDTO(e.getId(), e.getNome(), e.getDescricao());
    }

    public EspecialidadeEntity buscarEntidadePorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade nao encontrada com o ID: " + id));
    }

    @Transactional
    public EspecialidadeResponseDTO criar(EspecialidadeRequestDTO dto) {
        if (especialidadeRepository.findByNome(dto.getNome()).isPresent()) {
            throw new BusinessException("Especialidade com este nome ja cadastrada.");
        }
        EspecialidadeEntity especialidade = new EspecialidadeEntity();
        especialidade.setNome(dto.getNome());
        especialidade.setDescricao(dto.getDescricao());
        EspecialidadeEntity salva = especialidadeRepository.save(especialidade);
        return new EspecialidadeResponseDTO(salva.getId(), salva.getNome(), salva.getDescricao());
    }

    @Transactional
    public EspecialidadeResponseDTO atualizar(Long id, EspecialidadeRequestDTO dto) {
        EspecialidadeEntity especialidade = buscarEntidadePorId(id);
        especialidadeRepository.findByNome(dto.getNome())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> { throw new BusinessException("Especialidade com este nome ja cadastrada."); });
        especialidade.setNome(dto.getNome());
        especialidade.setDescricao(dto.getDescricao());
        EspecialidadeEntity salva = especialidadeRepository.save(especialidade);
        return new EspecialidadeResponseDTO(salva.getId(), salva.getNome(), salva.getDescricao());
    }

    @Transactional
    public void deletar(Long id) {
        EspecialidadeEntity especialidade = buscarEntidadePorId(id);

        // Regra de negocio: nao permite excluir especialidade que tem medicos vinculados
        if (medicoRepository.existsByEspecialidadeId(id)) {
            throw new BusinessException("Não é possível excluir a especialidade pois existem médicos vinculados à ela.");
        }

        especialidadeRepository.delete(especialidade);
    }

}
