package com.sync_routine.back_end.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ResumenGlobalDto {
    private Integer tiempoTotalSegundos;
    private Integer rachaDiasActivos;
    private Integer tiempoPromedioDiario;
    private Map<String, Integer> tiempoPorDia;
}