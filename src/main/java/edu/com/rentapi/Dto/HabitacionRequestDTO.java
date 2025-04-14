package edu.com.rentapi.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record HabitacionRequestDTO(

        @Min(value = 1, message = "El piso debe ser mayor o igual a 1")
        int piso,

        @Min(value = 1, message = "El número de habitación debe ser mayor o igual a 1")
        int numero,

        @NotBlank(message = "El tipo de habitación es obligatorio")
        @Size(max = 50, message = "El tipo de habitación no debe exceder los 50 caracteres")
        String tipo

) {}