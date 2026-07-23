package com.sync_routine.back_end.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sync_routine.back_end.dto.VincularRequestDto;
import com.sync_routine.back_end.dto.VinculacionStatusDto;
import com.sync_routine.back_end.service.VinculacionService;

@RestController
@RequestMapping("/api/vinculacion")
public class VinculacionController {

    @Autowired
    private VinculacionService service;

    // Público: El reloj lo llama al abrir la app
    @PostMapping("/generar")
    public ResponseEntity<VinculacionStatusDto> generarPin() {
        return ResponseEntity.ok(service.generarPin());
    }

    // Público: El reloj lo llama cada 3 segundos
    @GetMapping("/estado/{pin}")
    public ResponseEntity<VinculacionStatusDto> consultarEstado(@PathVariable String pin) {
        return ResponseEntity.ok(service.consultarEstado(pin));
    }

    // Protegido: La web lo llama cuando ingresas el PIN correcto
    @PostMapping("/vincular")
    public ResponseEntity<String> vincularDispositivo(@RequestBody VincularRequestDto request) {
        if (service.vincularDispositivo(request)) {
            return ResponseEntity.ok("Dispositivo vinculado correctamente");
        }
        return ResponseEntity.badRequest().body("PIN inválido o expirado");
    }
}