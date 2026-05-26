package com.projeto_backend.ClinMed.domain.medico.service;

import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.especialidade.entity.EspecialidadeEntity;
import com.projeto_backend.ClinMed.domain.especialidade.service.EspecialidadeService;
import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.medico.repository.MedicoRepository;
import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import com.projeto_backend.ClinMed.domain.usuario.enums.StatusUsuario;
import com.projeto_backend.ClinMed.domain.usuario.service.UsuarioService;
import com.projeto_backend.ClinMed.exception.BusinessException;
import com.projeto_backend.ClinMed.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EspecialidadeService especialidadeService;

    @Autowired
    @Lazy
    private ConsultaRepository consultaRepository;

    // Retorna todos os medicos
    @Transactional(readOnly = true)
    public List<MedicoEntity> listarTodos() {
        return medicoRepository.findAll();
    }

    // Busca medico pelo id
    @Transactional(readOnly = true)
    public MedicoEntity buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medico nao encontrado com o ID: " + id));
    }

    // Cria um novo medico
    @Transactional
    public MedicoEntity criar(MedicoEntity medico) {
        // Valida se informou o usuario e a especialidade
        if (medico.getUsuario() == null || medico.getUsuario().getId() == null) {
            throw new BusinessException("E necessario informar o ID do usuario.");
        }
        if (medico.getEspecialidade() == null || medico.getEspecialidade().getId() == null) {
            throw new BusinessException("E necessario informar o ID da especialidade.");
        }

        UsuarioEntity usuario = usuarioService.buscarPorId(medico.getUsuario().getId());

        // Valida perfil de medico
        if (usuario.getStatus() != StatusUsuario.MEDICO) {
            throw new BusinessException("O usuario vinculado deve ter o perfil de MEDICO.");
        }

        // Valida se ja e medico
        if (medicoRepository.existsByUsuarioId(medico.getUsuario().getId())) {
            throw new BusinessException("Este usuario ja esta vinculado a um cadastro de medico.");
        }

        EspecialidadeEntity especialidade = especialidadeService.buscarPorId(medico.getEspecialidade().getId());

        medico.setUsuario(usuario);
        medico.setEspecialidade(especialidade);

        return medicoRepository.save(medico);
    }

    // Atualiza dados de um medico
    @Transactional
    public MedicoEntity atualizar(Long id, MedicoEntity dadosNovos) {
        MedicoEntity medico = buscarPorId(id);

        if (dadosNovos.getUsuario() == null || dadosNovos.getUsuario().getId() == null) {
            throw new BusinessException("E necessario informar o ID do usuario.");
        }
        if (dadosNovos.getEspecialidade() == null || dadosNovos.getEspecialidade().getId() == null) {
            throw new BusinessException("E necessario informar o ID da especialidade.");
        }

        UsuarioEntity usuario = usuarioService.buscarPorId(dadosNovos.getUsuario().getId());
        if (usuario.getStatus() != StatusUsuario.MEDICO) {
            throw new BusinessException("O usuario vinculado deve ter o perfil de MEDICO.");
        }

        medicoRepository.findByUsuarioId(dadosNovos.getUsuario().getId())
                .filter(m -> !m.getId().equals(id))
                .ifPresent(m -> {
                    throw new BusinessException("Este usuario ja esta vinculado a um outro cadastro de medico.");
                });

        EspecialidadeEntity especialidade = especialidadeService.buscarPorId(dadosNovos.getEspecialidade().getId());

        medico.setUsuario(usuario);
        medico.setEspecialidade(especialidade);
        medico.setDetalhes(dadosNovos.getDetalhes());

        return medicoRepository.save(medico);
    }

    // Deleta um medico
    @Transactional
    public void deletar(Long id) {
        MedicoEntity medico = buscarPorId(id);

        // Regra de negocio: nao permite excluir medico que ja possui consultas cadastradas
        if (consultaRepository.existsByMedicoId(id)) {
            throw new BusinessException("Nao e possivel excluir o medico pois ele possui consultas cadastradas.");
        }

        medicoRepository.delete(medico);
    }
}
