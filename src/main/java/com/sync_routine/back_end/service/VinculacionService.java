package com.sync_routine.back_end.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import com.sync_routine.back_end.dto.VincularRequestDto;
import com.sync_routine.back_end.dto.VinculacionStatusDto;
import com.sync_routine.back_end.entity.VinculacionReloj;
import com.sync_routine.back_end.repository.VinculacionRelojRepository;

@Service
public class VinculacionService {

    @Autowired
    private VinculacionRelojRepository repository;

    // 1. Reloj: Genera un nuevo PIN
    @Transactional
    public VinculacionStatusDto generarPin() {
        String pin;
        // Nos aseguramos de que el PIN de 6 dígitos no exista ya
        do {
            pin = String.format("%06d", new Random().nextInt(999999));
        } while (repository.findByPin(pin).isPresent());

        VinculacionReloj vinculacion = new VinculacionReloj();
        vinculacion.setPin(pin);
        vinculacion.setStatus("PENDING");
        vinculacion.setExpiresAt(LocalDateTime.now().plusMinutes(10)); // 10 mins para usarlo

        repository.save(vinculacion);

        VinculacionStatusDto dto = new VinculacionStatusDto();
        dto.setPin(pin);
        dto.setStatus("PENDING");
        return dto;
    }

    // 2. Web: Vincula el PIN con el Usuario
    @Transactional
    public boolean vincularDispositivo(VincularRequestDto request) {
        Optional<VinculacionReloj> opt = repository.findByPin(request.getPin());

        if (opt.isPresent()) {
            VinculacionReloj vinculacion = opt.get();
            if (vinculacion.getStatus().equals("PENDING") && vinculacion.getExpiresAt().isAfter(LocalDateTime.now())) {
                vinculacion.setUserId(request.getUserId());
                vinculacion.setToken(request.getToken());
                vinculacion.setRefreshToken(request.getRefreshToken());

                vinculacion.setStatus("PAIRED");
                repository.save(vinculacion);
                return true;
            }
        }
        return false;
    }

    // 3. Reloj: Pregunta cómo va su PIN
    @Transactional(readOnly = true)
    public VinculacionStatusDto consultarEstado(String pin) {
        Optional<VinculacionReloj> opt = repository.findByPin(pin);
        VinculacionStatusDto dto = new VinculacionStatusDto();
        dto.setPin(pin);

        if (opt.isPresent()) {
            VinculacionReloj vinculacion = opt.get();
            if (vinculacion.getExpiresAt().isBefore(LocalDateTime.now())) {
                dto.setStatus("EXPIRED");
            } else {
                dto.setStatus(vinculacion.getStatus());
                if (vinculacion.getStatus().equals("PAIRED")) {
                    dto.setToken(vinculacion.getToken());
                    dto.setRefreshToken(vinculacion.getRefreshToken());
                }
            }
        } else {
            dto.setStatus("INVALID");
        }
        return dto;
    }
}