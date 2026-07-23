package com.sync_routine.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sync_routine.back_end.entity.Actividad;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, UUID> {
    // Busca las actividades de un bloque y las ordena por su secuencia lógica
    List<Actividad> findByBloqueIdOrderByOrdenAsc(UUID bloqueId);
}