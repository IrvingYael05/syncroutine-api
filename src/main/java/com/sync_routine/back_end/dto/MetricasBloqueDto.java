package com.sync_routine.back_end.dto;

import lombok.Data;
import java.util.List;

@Data
public class MetricasBloqueDto {
    private String nombreBloque;
    private Integer tiempoObjetivoTotal;
    private Double precisionPorcentaje;
    private List<String> fechas;
    private List<Integer> tiemposReales;
    private List<MetricaActividadDto> detalleActividades;
}