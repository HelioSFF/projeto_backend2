package com.projeto_backend.ClinMed.domain.usuario.service;

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
    public List<UsuarioEntity> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Busca usuario por id, lanca erro se nao existir
    @Transactional(readOnly = true)
    public UsuarioEntity buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com o ID: " + id));
    }

    // Cria um novo usuario
    @Transactional
    public UsuarioEntity criar(UsuarioEntity usuario) {
        // Valida se o email ja esta cadastrado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new BusinessException("E-mail ja cadastrado no sistema.");
        }
        return usuarioRepository.save(usuario);
    }

    // Atualiza um usuario existente
    @Transactional
    public UsuarioEntity atualizar(Long id, UsuarioEntity dadosNovos) {
        UsuarioEntity usuario = buscarPorId(id);

        // Nao deixa cadastrar email repetido de outra pessoa
        usuarioRepository.findByEmail(dadosNovos.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new BusinessException("E-mail ja esta sendo utilizado por outro usuario.");
                });

        usuario.setNome(dadosNovos.getNome());
        usuario.setEmail(dadosNovos.getEmail());
        usuario.setSenha(dadosNovos.getSenha());
        usuario.setStatus(dadosNovos.getStatus());

        return usuarioRepository.save(usuario);
    }

    // Deleta um usuario do banco
    @Transactional
    public void deletar(Long id) {
        UsuarioEntity usuario = buscarPorId(id);

        // Regra de negocio: nao permite deletar se o usuario for medico e estiver cadastrado na TB_MEDICO
        if (medicoRepository.existsByUsuarioId(id)) {
            throw new BusinessException("Nao e possivel excluir o usuario porque ele esta vinculado a um cadastro de medico.");
        }

        usuarioRepository.delete(usuario);
    }
}
