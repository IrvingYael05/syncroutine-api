package com.sync_routine.back_end.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import com.sync_routine.back_end.dto.BloqueDto;
import com.sync_routine.back_end.service.BloqueService;

@RestController
@RequestMapping("/api/bloques")
public class BloqueController {

    @Autowired
    private BloqueService service;

    // GET: Obtiene todos los bloques por el ID del usuario
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<BloqueDto>> getByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    // GET: Obtiene un bloque por su ID
    @GetMapping("/{id}")
    public ResponseEntity<BloqueDto> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Crea un bloque nuevo
    @PostMapping
    public ResponseEntity<BloqueDto> create(@RequestBody BloqueDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    // PUT: Actualiza un bloque
    @PutMapping("/{id}")
    public ResponseEntity<BloqueDto> update(@PathVariable UUID id, @RequestBody BloqueDto dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Elimina un bloque
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}