package com.projeto_backend.ClinMed.domain.prontuario.controller;

import com.projeto_backend.ClinMed.domain.prontuario.entity.ProntuarioEntity;
import com.projeto_backend.ClinMed.domain.prontuario.service.ProntuarioService;
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
    public ResponseEntity<List<ProntuarioEntity>> listarTodos() {
        return ResponseEntity.ok(prontuarioService.listarTodos());
    }

    // Busca prontuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProntuarioEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(prontuarioService.buscarPorId(id));
    }

    // Busca prontuarios de um paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ProntuarioEntity>> buscarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(prontuarioService.buscarPorPaciente(pacienteId));
    }

    // Registra um prontuario
    @PostMapping
    public ResponseEntity<ProntuarioEntity> registrar(@RequestBody ProntuarioEntity request) {
        ProntuarioEntity response = prontuarioService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
