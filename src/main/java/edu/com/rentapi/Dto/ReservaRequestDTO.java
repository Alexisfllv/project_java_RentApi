package edu.com.rentapi.Dto;

import java.time.LocalDate;

public record ReservaRequestDTO(
        Long habitacionId,
        LocalDate fechaInicio,
        LocalDate fechaFin

) {}