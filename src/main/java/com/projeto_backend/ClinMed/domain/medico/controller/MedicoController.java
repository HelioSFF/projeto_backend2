package com.projeto_backend.ClinMed.domain.medico.controller;

import com.projeto_backend.ClinMed.domain.medico.dto.MedicoRequestDTO;
import com.projeto_backend.ClinMed.domain.medico.dto.MedicoResponseDTO;
import com.projeto_backend.ClinMed.domain.medico.service.MedicoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<MedicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    // Busca medico por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    // Cadastra um novo medico
    @PostMapping
    public ResponseEntity<MedicoResponseDTO> criar(@RequestBody @Valid MedicoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.criar(request));
    }

    // Altera os dados de um medico
    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid MedicoRequestDTO request) {
        return ResponseEntity.ok(medicoService.atualizar(id, request));
    }

    // Deleta um medico pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        medicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
