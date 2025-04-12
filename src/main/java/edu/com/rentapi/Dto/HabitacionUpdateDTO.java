package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;

public record HabitacionUpdateDTO(
        int piso,
        int numero,
        String tipo,
        EstadoHabitacion estado
) {
}
