package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.EstadoReserva;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PlanoReservaResponseDTO(
        @Schema(description = "La reserva ID autogenerada")
        Long reservaId,
        @Schema(description = "El campo de habitacion de numero",example = "101")
        Integer habitacionNumero,
        @Schema(description = "El campo muestra el tipo de habitacion")
        String habitacionTipo,
        @Schema(description = "El campo muestra el nombre del cliente registrado")
        String clienteNombre,
        @Schema(description = "Fecha de inicio para la reserva")
        LocalDate fechaInicio,
        @Schema(description = "Fecha de fin para la reserva")
        LocalDate fechaFin,
        @Schema(description = "Campo para datos adicionales de la reserva")
        String comentarios,
        @Schema(description = "El estado del la reserva",example = "REALIZADA,CULMINADA")
        EstadoReserva estadoReserva,
        @Schema(description = "fecha creada como actual desde que se realizo")
        LocalDateTime fechaCreacion,
        @Schema(description = "fecha que se modifica para cuando termine la reserva",example = "CULMINADA -> REALIZADA")
        LocalDateTime fechaCulminada
) { }
