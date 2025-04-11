package edu.com.rentapi.Dto;

import java.time.LocalDate;

public record ReservaResponseDTO(
        Long id,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado,
        HabitacionDTO habitacion
) {}