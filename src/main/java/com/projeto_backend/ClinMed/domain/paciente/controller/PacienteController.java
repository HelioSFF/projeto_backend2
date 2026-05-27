package com.projeto_backend.ClinMed.domain.paciente.controller;

import com.projeto_backend.ClinMed.domain.paciente.dto.PacienteRequestDTO;
import com.projeto_backend.ClinMed.domain.paciente.dto.PacienteResponseDTO;
import com.projeto_backend.ClinMed.domain.paciente.service.PacienteService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<PacienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    // Busca paciente pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    // Cadastra um novo paciente
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> criar(@RequestBody @Valid PacienteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(request));
    }

    // Altera dados de um paciente
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PacienteRequestDTO request) {
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    // Deleta um paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
