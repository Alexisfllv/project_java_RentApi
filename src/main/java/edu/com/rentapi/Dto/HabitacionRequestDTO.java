package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;

public record HabitacionRequestDTO(


        int piso,
        int numero,
        String tipo
) {
}
