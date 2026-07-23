package com.sync_routine.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sync_routine.back_end.entity.Historial;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, UUID> {
    // Dashboard Nivel 1
    List<Historial> findByUserIdAndFechaCompletadoBetweenOrderByFechaCompletadoDesc(
            UUID userId, java.time.ZonedDateTime start, java.time.ZonedDateTime end);

    List<Historial> findByUserIdOrderByFechaCompletadoDesc(UUID userId);

    // Dashboard Nivel 2 y 3
    List<Historial> findByUserIdAndActividadIdIn(UUID userId, List<UUID> actividadIds);

    List<Historial> findByUserIdAndActividadIdInAndFechaCompletadoBetween(
            UUID userId, List<UUID> actividadIds, java.time.ZonedDateTime start, java.time.ZonedDateTime end);
}