package com.projeto_backend.ClinMed.domain.paciente.controller;

import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.paciente.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    // Lista todos os pacientes
    @GetMapping
    public ResponseEntity<List<PacienteEntity>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    // Busca paciente pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PacienteEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    // Cadastra um novo paciente
    @PostMapping
    public ResponseEntity<PacienteEntity> criar(@RequestBody PacienteEntity request) {
        PacienteEntity response = pacienteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Altera dados de um paciente
    @PutMapping("/{id}")
    public ResponseEntity<PacienteEntity> atualizar(@PathVariable Long id, @RequestBody PacienteEntity request) {
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    // Deleta um paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
