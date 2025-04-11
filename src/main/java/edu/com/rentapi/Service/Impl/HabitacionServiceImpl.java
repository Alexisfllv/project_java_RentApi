package edu.com.rentapi.Service.Impl;


import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Mapper.HabitacionMapper;
import edu.com.rentapi.Repo.HabitacionRepository;
import edu.com.rentapi.Service.HabitacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {

    // ioc
    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;


    @Override
    public List<HabitacionResponseDTO> listarHabitacionesConEstadoDisponible() {
        List<Habitacion> habitaciones = habitacionRepository.findByEstado(EstadoHabitacion.DISPONIBLE);

        return habitaciones
                .stream()
                .map(habitacion -> habitacionMapper.toHabitacionResponseDTO(habitacion))
                .toList();

    }
}
