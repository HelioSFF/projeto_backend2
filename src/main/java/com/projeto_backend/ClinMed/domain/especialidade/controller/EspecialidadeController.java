package com.projeto_backend.ClinMed.domain.especialidade.controller;

import com.projeto_backend.ClinMed.domain.especialidade.dto.EspecialidadeRequestDTO;
import com.projeto_backend.ClinMed.domain.especialidade.dto.EspecialidadeResponseDTO;
import com.projeto_backend.ClinMed.domain.especialidade.service.EspecialidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService especialidadeService;

    // Retorna todas as especialidades cadastrada
    @GetMapping
    public ResponseEntity<List<EspecialidadeResponseDTO>> listarTodas() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    // Busca especialidade por ID
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    // Cadastra uma nova especialidade
    @PostMapping
    public ResponseEntity<EspecialidadeResponseDTO> criar(@RequestBody @Valid EspecialidadeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadeService.criar(request));
    }

    // Altera dados de uma especialidade
    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EspecialidadeRequestDTO request) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, request));
    }

    // Exclui uma especialidade
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
