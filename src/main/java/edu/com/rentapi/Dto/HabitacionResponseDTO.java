package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;

public record HabitacionResponseDTO(
        Long id,
        Integer piso,
        Integer numero,
        String tipo,
        EstadoHabitacion estado
) {
}