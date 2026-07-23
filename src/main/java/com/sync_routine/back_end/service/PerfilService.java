package com.sync_routine.back_end.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

import com.sync_routine.back_end.dto.PerfilDto;
import com.sync_routine.back_end.entity.Perfil;
import com.sync_routine.back_end.repository.PerfilRepository;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository repository;

    // Read: Obtener perfil por ID
    @Transactional(readOnly = true)
    public Optional<PerfilDto> findById(UUID id) {
        return repository.findById(id).map(this::toDto);
    }

    // Update: Actualizar el nombre del usuario
    @Transactional
    public Optional<PerfilDto> update(UUID id, PerfilDto dto) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setNombre(dto.getNombre());
                    Perfil saved = repository.save(existing);
                    return toDto(saved);
                });
    }

    // Convert Entity to DTO
    private PerfilDto toDto(Perfil perfil) {
        if (perfil == null)
            return null;
        PerfilDto dto = new PerfilDto();
        dto.setId(perfil.getId());
        dto.setNombre(perfil.getNombre());
        return dto;
    }
}