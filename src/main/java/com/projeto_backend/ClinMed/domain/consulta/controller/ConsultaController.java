package com.projeto_backend.ClinMed.domain.consulta.controller;

import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import com.projeto_backend.ClinMed.domain.consulta.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    // Retorna todas as consultas
    @GetMapping
    public ResponseEntity<List<ConsultaEntity>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    // Busca consulta por ID
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    // Agenda uma nova consulta
    @PostMapping
    public ResponseEntity<ConsultaEntity> agendar(@RequestBody ConsultaEntity request) {
        ConsultaEntity response = consultaService.agendar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Confirma uma consulta
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<ConsultaEntity> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.confirmar(id));
    }

    // Cancela uma consulta
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ConsultaEntity> cancelar(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String motivo = request.get("motivo");
        return ResponseEntity.ok(consultaService.cancelar(id, motivo));
    }
}
