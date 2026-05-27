package com.projeto_backend.ClinMed.domain.usuario.service;

import com.projeto_backend.ClinMed.domain.usuario.dto.UsuarioRequestDTO;
import com.projeto_backend.ClinMed.domain.usuario.dto.UsuarioResponseDTO;
import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import com.projeto_backend.ClinMed.domain.usuario.repository.UsuarioRepository;
import com.projeto_backend.ClinMed.domain.medico.repository.MedicoRepository;
import com.projeto_backend.ClinMed.exception.BusinessException;
import com.projeto_backend.ClinMed.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Lazy
    private MedicoRepository medicoRepository;

    // Lista todos os usuarios do banco
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseDTO(u.getNome(), u.getEmail()))
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        UsuarioEntity u = buscarEntidadePorId(id);
        return new UsuarioResponseDTO(u.getNome(), u.getEmail());
    }

    public UsuarioEntity buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado no sistema.");
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        UsuarioEntity salvo = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(salvo.getNome(), salvo.getEmail());
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        UsuarioEntity usuario = buscarEntidadePorId(id);
        usuarioRepository.findByEmail(dto.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new BusinessException("E-mail já está sendo utilizado por outro usuário."); });
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        UsuarioEntity salvo = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(salvo.getNome(), salvo.getEmail());
    }

    @Transactional
    public void deletar(Long id) {
        UsuarioEntity usuario = buscarEntidadePorId(id);
        if (medicoRepository.existsByUsuarioId(id)) {
            throw new BusinessException("Não é possível excluir o usuário porque ele está vinculado a um cadastro de médico.");
        }
        usuarioRepository.delete(usuario);
    }
}

