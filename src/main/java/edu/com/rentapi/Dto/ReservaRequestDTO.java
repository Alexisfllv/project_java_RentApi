package edu.com.rentapi.Dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ReservaRequestDTO(

        @NotNull(message = "El ID de la habitación es obligatorio")
        Long habitacionId,

        @NotBlank(message = "El nombre del cliente es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String clienteNombre,

        @NotBlank(message = "El DNI del cliente es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
        String clienteDni,

        @NotNull(message = "La fecha de inicio es obligatoria")
        @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        @Future(message = "La fecha de fin debe ser en el futuro")
        LocalDate fechaFin,

        @Size(max = 255, message = "Los comentarios no deben superar los 255 caracteres")
        String comentarios

) {}
