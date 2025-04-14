package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;
import io.swagger.v3.oas.annotations.media.Schema;

public record HabitacionResponseDTO(

        @Schema(description = "ID único de la habitación AutoIncrement")
        Long id,

        @Schema(description = "Número de piso", example = "2")
        Integer piso,

        @Schema(description = "Número de habitación", example = "205")
        Integer numero,

        @Schema(description = "Tipo de habitación", example = "doble")
        String tipo,

        @Schema(description = "Estado actual de la habitación", example = "DISPONIBLE/RESERVADA/MANTENIMIENTO")
        EstadoHabitacion estado

) {}