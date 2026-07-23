package com.sync_routine.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sync_routine.back_end.entity.VinculacionReloj;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VinculacionRelojRepository extends JpaRepository<VinculacionReloj, UUID> {
    Optional<VinculacionReloj> findByPin(String pin);
}