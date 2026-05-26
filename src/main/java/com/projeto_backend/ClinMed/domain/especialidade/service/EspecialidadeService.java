package com.projeto_backend.ClinMed.domain.especialidade.service;

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

    // Retorna a lista de todas as especialidades do banco
    @Transactional(readOnly = true)
    public List<EspecialidadeEntity> listarTodas() {
        return especialidadeRepository.findAll();
    }

    // Busca uma especialidade pelo ID, se nao achar lanca excecao
    @Transactional(readOnly = true)
    public EspecialidadeEntity buscarPorId(Long id) {
        return especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade nao encontrada com o ID: " + id));
    }

    // Cria uma nova especialidade
    @Transactional
    public EspecialidadeEntity criar(EspecialidadeEntity especialidade) {
        // Verifica se ja existe especialidade com o mesmo nome
        if (especialidadeRepository.findByNome(especialidade.getNome()).isPresent()) {
            throw new BusinessException("Especialidade com este nome ja cadastrada.");
        }
        return especialidadeRepository.save(especialidade);
    }

    // Atualiza os dados de uma especialidade existente
    @Transactional
    public EspecialidadeEntity atualizar(Long id, EspecialidadeEntity dadosAtualizados) {
        EspecialidadeEntity especialidade = buscarPorId(id);

        // Nao permite nome duplicado para outra especialidade
        especialidadeRepository.findByNome(dadosAtualizados.getNome())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new BusinessException("Especialidade com este nome ja cadastrada.");
                });

        especialidade.setNome(dadosAtualizados.getNome());
        especialidade.setDescricao(dadosAtualizados.getDescricao());

        return especialidadeRepository.save(especialidade);
    }

    // Deleta uma especialidade pelo ID
    @Transactional
    public void deletar(Long id) {
        EspecialidadeEntity especialidade = buscarPorId(id);

        // Regra de negocio: nao permite excluir especialidade que tem medicos vinculados
        if (medicoRepository.existsByEspecialidadeId(id)) {
            throw new BusinessException("Nao e possivel excluir a especialidade pois existem medicos vinculados a ela.");
        }

        especialidadeRepository.delete(especialidade);
    }
}
