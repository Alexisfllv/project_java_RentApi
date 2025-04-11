package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;

import java.time.LocalDate;

public record PlanoReservaResponseDTO(
        Long reservaId,
        int habitacionNumero,
        String habitacionTipo,
        String clienteNombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String comentarios,
        EstadoHabitacion estado
) {

}
