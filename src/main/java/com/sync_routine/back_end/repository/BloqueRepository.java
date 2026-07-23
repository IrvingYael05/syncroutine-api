package com.sync_routine.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sync_routine.back_end.entity.Bloque;
import java.util.List;
import java.util.UUID;

@Repository
public interface BloqueRepository extends JpaRepository<Bloque, UUID> {
    // Busca todos los bloques pertenecientes a un usuario
    List<Bloque> findByUserId(UUID userId);
}