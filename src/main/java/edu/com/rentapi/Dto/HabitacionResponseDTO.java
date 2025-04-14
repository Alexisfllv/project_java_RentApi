package edu.com.rentapi.Dto;

import edu.com.rentapi.Entity.EstadoHabitacion;

public record HabitacionResponseDTO(
        Long id,
        int piso,
        int numero,
        String tipo,
        EstadoHabitacion estado
) {}