package edu.com.rentapi.Service;

import edu.com.rentapi.Dto.HabitacionDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;

import java.util.List;

public interface HabitacionService {

    // listado de habitaciones disponibles
    List<HabitacionResponseDTO> listarHabitacionesConEstadoDisponible();
}
