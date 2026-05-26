package com.projeto_backend.ClinMed.domain.especialidade.controller;

import com.projeto_backend.ClinMed.domain.especialidade.entity.EspecialidadeEntity;
import com.projeto_backend.ClinMed.domain.especialidade.service.EspecialidadeService;
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
    public ResponseEntity<List<EspecialidadeEntity>> listarTodas() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    // Busca especialidade por ID
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    // Cadastra uma nova especialidade
    @PostMapping
    public ResponseEntity<EspecialidadeEntity> criar(@RequestBody EspecialidadeEntity request) {
        EspecialidadeEntity response = especialidadeService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Altera dados de uma especialidade
    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeEntity> atualizar(@PathVariable Long id, @RequestBody EspecialidadeEntity request) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, request));
    }

    // Exclui uma especialidade
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
