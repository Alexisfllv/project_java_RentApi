package edu.com.rentapi.Service;

import edu.com.rentapi.Dto.HabitacionRequestDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Dto.HabitacionUpdateDTO;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Response.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HabitacionService {

    // listado de habitaciones disponibles
    List<HabitacionResponseDTO> listarHabitacionesConEstadoDisponible();


    // listar todas las habitaciones
    PageResponseDTO<HabitacionResponseDTO> listarHabitaciones(Pageable pageable);

    // busqueda
    HabitacionResponseDTO buscarHabitacionPorId(Long id);

    // insercion
    ResponseDTO registrarHabitacion(HabitacionRequestDTO habitacionRequestDTO);

    // update
    ResponseDTO updateHabitacion(Long id, HabitacionUpdateDTO habitacionUpdateDTO);

    // delete
    ResponseDTO eliminarHabitacion(Long id);

}
