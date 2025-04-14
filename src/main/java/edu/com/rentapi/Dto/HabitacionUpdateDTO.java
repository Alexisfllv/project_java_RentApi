package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HabitacionUpdateDTO(
        @NotNull(message = "El piso es obligatorio")
        @Min(value = 1, message = "El piso debe ser mayor o igual a 1")
        @Schema(description = "Número de piso", example = "2", minimum = "1", required = true)
        Integer piso,

        @NotNull(message = "El número de habitación es obligatorio")
        @Min(value = 1, message = "El número de habitación debe ser mayor o igual a 1")
        @Schema(description = "Número de la habitación", example = "205", minimum = "1", required = true)
        Integer numero,

        @NotBlank(message = "El tipo de habitación es obligatorio")
        @Size(max = 50, message = "El tipo de habitación no debe exceder los 50 caracteres")
        @Schema(description = "Tipo de habitación (ej: simple, doble, suite)", example = "doble", maxLength = 50, required = true)
        String tipo,

        @NotBlank(message = "El estado habitacion es obligatorio")
        @Enumerated(EnumType.STRING)
        @Schema(description = "Estados comunes" , example = "DISPONIBLE,RESERVADA,MANTENIMIENTO")
        EstadoHabitacion estado
) {
}
