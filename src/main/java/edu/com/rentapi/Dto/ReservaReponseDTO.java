package edu.com.rentapi.Dto;

import java.time.LocalDate;

public record ReservaReponseDTO(
        Long id,
        HabitacionDTO habitacion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String estado
) {}