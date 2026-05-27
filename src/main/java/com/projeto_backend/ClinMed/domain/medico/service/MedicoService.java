package com.projeto_backend.ClinMed.domain.medico.service;

import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.especialidade.entity.EspecialidadeEntity;
import com.projeto_backend.ClinMed.domain.especialidade.service.EspecialidadeService;
import com.projeto_backend.ClinMed.domain.medico.dto.MedicoRequestDTO;
import com.projeto_backend.ClinMed.domain.medico.dto.MedicoResponseDTO;
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
    public List<MedicoResponseDTO> listarTodos() {
        return medicoRepository.findAll().stream()
                .map(m -> new MedicoResponseDTO(m.getEspecialidade().getId(), m.getDetalhes(), m.getUsuario().getId(), m.getUsuario().getNome()))
                .toList();
    }

    // Busca medico pelo id
    @Transactional(readOnly = true)
    public MedicoResponseDTO buscarPorId(Long id) {
        MedicoEntity m = buscarEntidadePorId(id);
        return new MedicoResponseDTO(m.getEspecialidade().getId(), m.getDetalhes(), m.getUsuario().getId(), m.getUsuario().getNome());
    }

    public MedicoEntity buscarEntidadePorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medico nao encontrado com o ID: " + id));
    }
    // Cria um novo medico
    @Transactional
    public MedicoResponseDTO criar(MedicoRequestDTO dto) {

        UsuarioEntity usuario = usuarioService.buscarEntidadePorId(dto.getUsuarioId());
        if (usuario.getStatus() != StatusUsuario.MEDICO) {
            throw new BusinessException("O usuario vinculado deve ter o perfil de MEDICO.");
        }
        if (medicoRepository.existsByUsuarioId(dto.getUsuarioId())) {
            throw new BusinessException("Este usuario ja esta vinculado a um cadastro de medico.");
        }
        EspecialidadeEntity especialidade = especialidadeService.buscarEntidadePorId(dto.getEspecialidadeId());
        MedicoEntity medico = new MedicoEntity(especialidade, dto.getDetalhes(), usuario);
        MedicoEntity salvo = medicoRepository.save(medico);
        return new MedicoResponseDTO(salvo.getEspecialidade().getId(), salvo.getDetalhes(), salvo.getUsuario().getId(), salvo.getUsuario().getNome());
    }

    @Transactional
    public MedicoResponseDTO atualizar(Long id, MedicoRequestDTO dto) {
        MedicoEntity medico = buscarEntidadePorId(id);
        UsuarioEntity usuario = usuarioService.buscarEntidadePorId(dto.getUsuarioId());
        if (usuario.getStatus() != StatusUsuario.MEDICO) {
            throw new BusinessException("O usuario vinculado deve ter o perfil de MEDICO.");
        }
        medicoRepository.findByUsuarioId(dto.getUsuarioId())
                .filter(m -> !m.getId().equals(id))
                .ifPresent(m -> {
                    throw new BusinessException("Este usuario ja esta vinculado a um outro cadastro de medico.");
                });
        EspecialidadeEntity especialidade = especialidadeService.buscarEntidadePorId(dto.getEspecialidadeId());
        medico.setUsuario(usuario);
        medico.setEspecialidade(especialidade);
        medico.setDetalhes(dto.getDetalhes());
        MedicoEntity salvo = medicoRepository.save(medico);
        return new MedicoResponseDTO(salvo.getEspecialidade().getId(), salvo.getDetalhes(), salvo.getUsuario().getId(), salvo.getUsuario().getNome());
    }

    @Transactional
    public void deletar(Long id) {
        MedicoEntity medico = buscarEntidadePorId(id);
        if (consultaRepository.existsByMedicoId(id)) {
            throw new BusinessException("Nao e possivel excluir o medico pois ele possui consultas cadastradas.");
        }
        medicoRepository.delete(medico);
    }
}