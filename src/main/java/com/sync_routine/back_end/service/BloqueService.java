package com.sync_routine.back_end.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sync_routine.back_end.dto.BloqueDto;
import com.sync_routine.back_end.entity.Bloque;
import com.sync_routine.back_end.repository.BloqueRepository;

@Service
public class BloqueService {

    @Autowired
    private BloqueRepository repository;

    // Obtener todos los bloques de un usuario
    @Transactional(readOnly = true)
    public List<BloqueDto> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Obtener un bloque específico
    @Transactional(readOnly = true)
    public Optional<BloqueDto> findById(UUID id) {
        return repository.findById(id).map(this::toDto);
    }

    // Crear un bloque
    @Transactional
    public BloqueDto create(BloqueDto dto) {
        Bloque bloque = new Bloque();
        bloque.setUserId(dto.getUserId());
        bloque.setNombre(dto.getNombre());
        bloque.setEsAleatorio(dto.getEsAleatorio() != null ? dto.getEsAleatorio() : false);

        Bloque saved = repository.save(bloque);
        return toDto(saved);
    }

    // Actualizar un bloque
    @Transactional
    public Optional<BloqueDto> update(UUID id, BloqueDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setNombre(dto.getNombre());
            existing.setEsAleatorio(dto.getEsAleatorio() != null ? dto.getEsAleatorio() : false);

            Bloque saved = repository.save(existing);
            return toDto(saved);
        });
    }

    // Eliminar un bloque
    @Transactional
    public boolean delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // Mapper auxiliar (Entity -> DTO)
    private BloqueDto toDto(Bloque entity) {
        BloqueDto dto = new BloqueDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setNombre(entity.getNombre());
        dto.setEsAleatorio(entity.getEsAleatorio());
        return dto;
    }
}