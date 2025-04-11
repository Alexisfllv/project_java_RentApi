package edu.com.rentapi.Service;

import edu.com.rentapi.Dto.HabitacionResponseDTO;

import java.util.List;

public interface HabitacionService {

    // listado de habitaciones disponibles
    List<HabitacionResponseDTO> listarHabitacionesConEstadoDisponible();
}
