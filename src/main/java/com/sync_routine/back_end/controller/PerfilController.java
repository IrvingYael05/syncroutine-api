package com.sync_routine.back_end.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import com.sync_routine.back_end.dto.PerfilDto;
import com.sync_routine.back_end.service.PerfilService;

@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService service;

    @GetMapping("/{id}")
    public ResponseEntity<PerfilDto> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilDto> update(@PathVariable UUID id, @RequestBody PerfilDto dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}