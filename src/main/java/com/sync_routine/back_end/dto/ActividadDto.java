package com.sync_routine.back_end.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ActividadDto {
    private UUID id;
    private UUID bloqueId;
    private String nombre;
    private Integer tiempoObjetivoSegundos;
    private Integer orden;
}