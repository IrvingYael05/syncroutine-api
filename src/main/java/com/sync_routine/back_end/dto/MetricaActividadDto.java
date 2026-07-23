package com.sync_routine.back_end.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class MetricaActividadDto {
    private UUID actividadId;
    private String nombre;
    private Integer tiempoObjetivo;
    private Integer tiempoPromedioReal;
    private Integer mejorTiempo;
    private Integer peorTiempo;
}