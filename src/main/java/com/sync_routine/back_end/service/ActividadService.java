package com.sync_routine.back_end.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sync_routine.back_end.dto.ActividadDto;
import com.sync_routine.back_end.entity.Actividad;
import com.sync_routine.back_end.repository.ActividadRepository;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository repository;

    @Transactional(readOnly = true)
    public List<ActividadDto> findByBloqueId(UUID bloqueId) {
        return repository.findByBloqueIdOrderByOrdenAsc(bloqueId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ActividadDto create(ActividadDto dto) {
        Actividad actividad = new Actividad();
        actividad.setBloqueId(dto.getBloqueId());
        actividad.setNombre(dto.getNombre());
        actividad.setTiempoObjetivoSegundos(dto.getTiempoObjetivoSegundos());

        // Si no mandan orden, calculamos cuántas hay y lo ponemos al final
        if (dto.getOrden() == null) {
            int total = repository.findByBloqueIdOrderByOrdenAsc(dto.getBloqueId()).size();
            actividad.setOrden(total + 1);
        } else {
            actividad.setOrden(dto.getOrden());
        }

        Actividad saved = repository.save(actividad);
        return toDto(saved);
    }

    @Transactional
    public Optional<ActividadDto> update(UUID id, ActividadDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setNombre(dto.getNombre());
            existing.setTiempoObjetivoSegundos(dto.getTiempoObjetivoSegundos());
            if (dto.getOrden() != null) {
                existing.setOrden(dto.getOrden());
            }
            Actividad saved = repository.save(existing);
            return toDto(saved);
        });
    }

    @Transactional
    public boolean delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private ActividadDto toDto(Actividad entity) {
        ActividadDto dto = new ActividadDto();
        dto.setId(entity.getId());
        dto.setBloqueId(entity.getBloqueId());
        dto.setNombre(entity.getNombre());
        dto.setTiempoObjetivoSegundos(entity.getTiempoObjetivoSegundos());
        dto.setOrden(entity.getOrden());
        return dto;
    }
}