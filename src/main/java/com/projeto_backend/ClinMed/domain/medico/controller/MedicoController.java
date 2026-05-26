package com.projeto_backend.ClinMed.domain.medico.controller;

import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.medico.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    // Retorna todos os medicos
    @GetMapping
    public ResponseEntity<List<MedicoEntity>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    // Busca medico por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicoEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    // Cadastra um novo medico
    @PostMapping
    public ResponseEntity<MedicoEntity> criar(@RequestBody MedicoEntity request) {
        MedicoEntity response = medicoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Altera os dados de um medico
    @PutMapping("/{id}")
    public ResponseEntity<MedicoEntity> atualizar(@PathVariable Long id, @RequestBody MedicoEntity request) {
        return ResponseEntity.ok(medicoService.atualizar(id, request));
    }

    // Deleta um medico pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
