package com.sync_routine.back_end.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import com.sync_routine.back_end.dto.HistorialDto;
import com.sync_routine.back_end.dto.MetricasBloqueDto;
import com.sync_routine.back_end.dto.ResumenGlobalDto;
import com.sync_routine.back_end.service.HistorialService;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    @Autowired
    private HistorialService service;

    // Registrar actividad (SmartWatch)
    @PostMapping
    public ResponseEntity<HistorialDto> registrarActividad(@RequestBody HistorialDto dto) {
        return ResponseEntity.ok(service.registrarActividad(dto));
    }

    // Dashboard Nivel 1
    @GetMapping("/resumen/{userId}")
    public ResponseEntity<ResumenGlobalDto> getResumenGlobal(
            @PathVariable UUID userId,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        return ResponseEntity.ok(service.obtenerResumenGlobal(userId, fechaInicio, fechaFin));
    }

    // Dashboard Nivel 2 y 3
    @GetMapping("/metricas/bloque/{bloqueId}/usuario/{userId}")
    public ResponseEntity<MetricasBloqueDto> getMetricasBloque(
            @PathVariable UUID bloqueId,
            @PathVariable UUID userId,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        return ResponseEntity.ok(service.obtenerMetricasBloque(userId, bloqueId, fechaInicio, fechaFin));
    }
}