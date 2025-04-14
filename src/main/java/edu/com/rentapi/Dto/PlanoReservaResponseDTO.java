package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.EstadoReserva;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PlanoReservaResponseDTO(
        Long reservaId,
        int habitacionNumero,
        String habitacionTipo,
        String clienteNombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String comentarios,
        EstadoReserva estadoReserva,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaCulminada
) { }
