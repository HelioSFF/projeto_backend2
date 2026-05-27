package com.projeto_backend.ClinMed.domain.prontuario.controller;

import com.projeto_backend.ClinMed.domain.prontuario.dto.ProntuarioRequestDTO;
import com.projeto_backend.ClinMed.domain.prontuario.dto.ProntuarioResponseDTO;
import com.projeto_backend.ClinMed.domain.prontuario.service.ProntuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioService prontuarioService;

    // Retorna todos os prontuarios
    @GetMapping
    public ResponseEntity<List<ProntuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(prontuarioService.listarTodos());
    }

    // Busca prontuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prontuarioService.buscarPorId(id));
    }

    // Busca prontuarios de um paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ProntuarioResponseDTO>> buscarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(prontuarioService.buscarPorPaciente(pacienteId));
    }

    // Registra um prontuario
    @PostMapping
    public ResponseEntity<ProntuarioResponseDTO> registrar(@RequestBody @Valid ProntuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prontuarioService.registrar(request));
    }
}
