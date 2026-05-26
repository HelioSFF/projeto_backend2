package com.projeto_backend.ClinMed.domain.usuario.controller;

import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import com.projeto_backend.ClinMed.domain.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Retorna todos os usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // Busca usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    // Cadastra um novo usuario
    @PostMapping
    public ResponseEntity<UsuarioEntity> criar(@RequestBody UsuarioEntity request) {
        UsuarioEntity response = usuarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Altera dados de um usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> atualizar(@PathVariable Long id, @RequestBody UsuarioEntity request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    // Deleta um usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
