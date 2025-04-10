package edu.com.rentapi.Dto;

public record HabitacionDTO(
        Long id,
        int piso,
        int numero,
        String tipo
) {}