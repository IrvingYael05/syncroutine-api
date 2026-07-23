package com.sync_routine.back_end.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import com.sync_routine.back_end.dto.ActividadDto;
import com.sync_routine.back_end.service.ActividadService;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadService service;

    @GetMapping("/bloque/{bloqueId}")
    public ResponseEntity<List<ActividadDto>> getByBloqueId(@PathVariable UUID bloqueId) {
        return ResponseEntity.ok(service.findByBloqueId(bloqueId));
    }

    @PostMapping
    public ResponseEntity<ActividadDto> create(@RequestBody ActividadDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDto> update(@PathVariable UUID id, @RequestBody ActividadDto dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}