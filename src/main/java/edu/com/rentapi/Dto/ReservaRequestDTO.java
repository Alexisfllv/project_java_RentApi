package edu.com.rentapi.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ReservaRequestDTO(

        @NotNull(message = "El ID de la habitación es obligatorio")
        @Schema(description = "Campo id habitacion autoincrementado")
        Long habitacionId,

        @NotBlank(message = "El nombre del cliente es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        @Schema(description = "Nombre de cliente",example = "Juanito")
        String clienteNombre,

        @NotBlank(message = "El DNI del cliente es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
        @Schema(description = "numero de dni",example = "12345678",minimum = "8",maxLength = 8,required = true)
        String clienteDni,

        @NotNull(message = "La fecha de inicio es obligatoria")
        @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
        @Schema(description = "Fecha de Inicio reservada",example = "2025-12-31")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        @Future(message = "La fecha de fin debe ser en el futuro")
        @Schema(description = "Fecha de Fin reservada mayor a fecha inicio reserva",example = "2025-12-31")
        LocalDate fechaFin,

        @Size(max = 255, message = "Los comentarios no deben superar los 255 caracteres")
        @Schema(description = "Comentarios sobre la reserva adicionales")
        String comentarios

) {}
