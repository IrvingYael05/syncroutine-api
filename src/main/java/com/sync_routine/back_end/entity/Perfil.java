package com.sync_routine.back_end.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "perfiles")
@Data
public class Perfil {

    @Id
    private UUID id;

    private String nombre;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}