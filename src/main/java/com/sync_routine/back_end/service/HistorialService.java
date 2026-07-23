package com.sync_routine.back_end.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.sync_routine.back_end.dto.HistorialDto;
import com.sync_routine.back_end.dto.MetricaActividadDto;
import com.sync_routine.back_end.dto.ResumenGlobalDto;
import com.sync_routine.back_end.entity.Historial;
import com.sync_routine.back_end.repository.HistorialRepository;

import com.sync_routine.back_end.repository.BloqueRepository;
import com.sync_routine.back_end.repository.ActividadRepository;
import com.sync_routine.back_end.entity.Bloque;
import com.sync_routine.back_end.entity.Actividad;
import com.sync_routine.back_end.dto.MetricasBloqueDto;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository repository;

    @Autowired
    private BloqueRepository bloqueRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    // Registrar actividad (SmartWatch)
    @Transactional
    public HistorialDto registrarActividad(HistorialDto dto) {
        Historial historial = new Historial();
        historial.setUserId(dto.getUserId());
        historial.setActividadId(dto.getActividadId());
        historial.setTiempoRealSegundos(dto.getTiempoRealSegundos());

        Historial guardado = repository.save(historial);
        return toDto(guardado);
    }

    // Dashboard Nivel 1
    @Transactional(readOnly = true)
    public ResumenGlobalDto obtenerResumenGlobal(UUID userId, String fechaInicioStr, String fechaFinStr) {
        List<Historial> historiales;

        if (fechaInicioStr != null && fechaFinStr != null) {
            java.time.ZonedDateTime start = LocalDate.parse(fechaInicioStr).atStartOfDay(ZoneId.of("UTC"));
            java.time.ZonedDateTime end = LocalDate.parse(fechaFinStr).atTime(23, 59, 59).atZone(ZoneId.of("UTC"));
            historiales = repository.findByUserIdAndFechaCompletadoBetweenOrderByFechaCompletadoDesc(userId, start,
                    end);
        } else {
            historiales = repository.findByUserIdOrderByFechaCompletadoDesc(userId);
        }

        ResumenGlobalDto resumen = new ResumenGlobalDto();

        if (historiales.isEmpty()) {
            resumen.setTiempoTotalSegundos(0);
            resumen.setTiempoPromedioDiario(0); // <--- Asignar 0
            resumen.setRachaDiasActivos(0);
            resumen.setTiempoPorDia(new HashMap<>());
            return resumen;
        }

        int tiempoTotal = historiales.stream().mapToInt(Historial::getTiempoRealSegundos).sum();
        resumen.setTiempoTotalSegundos(tiempoTotal);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Integer> tiempoPorDia = historiales.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getFechaCompletado().toLocalDate().format(formatter),
                        Collectors.summingInt(Historial::getTiempoRealSegundos)));
        resumen.setTiempoPorDia(tiempoPorDia);

        int diasActivosReal = tiempoPorDia.size();
        resumen.setTiempoPromedioDiario(diasActivosReal > 0 ? tiempoTotal / diasActivosReal : 0);

        Set<LocalDate> diasActivos = historiales.stream().map(h -> h.getFechaCompletado().toLocalDate())
                .collect(Collectors.toSet());
        LocalDate hoy = LocalDate.now(ZoneId.of("UTC"));
        int racha = 0;
        LocalDate diaEvaluado = hoy;
        if (!diasActivos.contains(diaEvaluado) && diasActivos.contains(diaEvaluado.minusDays(1))) {
            diaEvaluado = diaEvaluado.minusDays(1);
        }
        while (diasActivos.contains(diaEvaluado)) {
            racha++;
            diaEvaluado = diaEvaluado.minusDays(1);
        }
        resumen.setRachaDiasActivos(racha);

        return resumen;
    }

    // Dashboard Nivel 2 y 3
    @Transactional(readOnly = true)
    public MetricasBloqueDto obtenerMetricasBloque(UUID userId, UUID bloqueId, String fechaInicioStr,
            String fechaFinStr) {
        MetricasBloqueDto dto = new MetricasBloqueDto();

        Bloque bloque = bloqueRepository.findById(bloqueId).orElseThrow();
        dto.setNombreBloque(bloque.getNombre());

        List<Actividad> actividades = actividadRepository.findByBloqueIdOrderByOrdenAsc(bloqueId);
        int objetivoTotal = actividades.stream().mapToInt(Actividad::getTiempoObjetivoSegundos).sum();
        dto.setTiempoObjetivoTotal(objetivoTotal);

        List<UUID> actividadIds = actividades.stream().map(Actividad::getId).collect(Collectors.toList());

        if (actividadIds.isEmpty()) {
            dto.setPrecisionPorcentaje(0.0);
            dto.setFechas(new ArrayList<>());
            dto.setTiemposReales(new ArrayList<>());
            return dto;
        }

        List<Historial> historiales;

        if (fechaInicioStr != null && fechaFinStr != null) {
            java.time.ZonedDateTime start = LocalDate.parse(fechaInicioStr).atStartOfDay(ZoneId.of("UTC"));
            java.time.ZonedDateTime end = LocalDate.parse(fechaFinStr).atTime(23, 59, 59).atZone(ZoneId.of("UTC"));
            historiales = repository.findByUserIdAndActividadIdInAndFechaCompletadoBetween(userId, actividadIds, start,
                    end);
        } else {
            historiales = repository.findByUserIdAndActividadIdIn(userId, actividadIds);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Integer> tiempoPorDia = historiales.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getFechaCompletado().toLocalDate().format(formatter),
                        TreeMap::new,
                        Collectors.summingInt(Historial::getTiempoRealSegundos)));

        int totalReal = historiales.stream().mapToInt(Historial::getTiempoRealSegundos).sum();
        int totalEsperado = objetivoTotal * tiempoPorDia.size();

        double precision = 0.0;
        if (totalEsperado > 0) {
            precision = 100.0 - (Math.abs((double) totalReal - totalEsperado) / totalEsperado * 100.0);
            if (precision < 0)
                precision = 0.0;
        }

        dto.setPrecisionPorcentaje(Math.round(precision * 10.0) / 10.0);
        dto.setFechas(new ArrayList<>(tiempoPorDia.keySet()));
        dto.setTiemposReales(new ArrayList<>(tiempoPorDia.values()));

        // Dashboard Nivel 3
        List<MetricaActividadDto> detalles = new ArrayList<>();

        for (Actividad act : actividades) {
            MetricaActividadDto det = new MetricaActividadDto();
            det.setActividadId(act.getId());
            det.setNombre(act.getNombre());
            det.setTiempoObjetivo(act.getTiempoObjetivoSegundos());

            // Filtramos el historial para quedarnos solo con los registros de esta
            // actividad
            List<Historial> histActividad = historiales.stream()
                    .filter(h -> h.getActividadId().equals(act.getId()))
                    .collect(Collectors.toList());

            if (histActividad.isEmpty()) {
                det.setTiempoPromedioReal(0);
                det.setMejorTiempo(0);
                det.setPeorTiempo(0);
            } else {
                int sum = histActividad.stream().mapToInt(Historial::getTiempoRealSegundos).sum();
                int min = histActividad.stream().mapToInt(Historial::getTiempoRealSegundos).min().orElse(0);
                int max = histActividad.stream().mapToInt(Historial::getTiempoRealSegundos).max().orElse(0);

                det.setTiempoPromedioReal(sum / histActividad.size());
                det.setMejorTiempo(min);
                det.setPeorTiempo(max);
            }
            detalles.add(det);
        }

        dto.setDetalleActividades(detalles);

        return dto;
    }

    private HistorialDto toDto(Historial entity) {
        HistorialDto dto = new HistorialDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setActividadId(entity.getActividadId());
        dto.setTiempoRealSegundos(entity.getTiempoRealSegundos());
        dto.setFechaCompletado(entity.getFechaCompletado());
        return dto;
    }
}