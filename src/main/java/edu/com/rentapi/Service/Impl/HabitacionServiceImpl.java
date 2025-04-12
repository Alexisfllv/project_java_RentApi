package edu.com.rentapi.Service.Impl;


import edu.com.rentapi.Dto.HabitacionRequestDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Dto.HabitacionUpdateDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Exception.ResourceNotFoundException;
import edu.com.rentapi.Mapper.HabitacionMapper;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Repo.HabitacionRepository;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Response.ResponseMessage;
import edu.com.rentapi.Service.HabitacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public PageResponseDTO<HabitacionResponseDTO> listarHabitaciones(Pageable pageable) {

        Page<HabitacionResponseDTO> paged =  habitacionRepository.findAll(pageable)
                .map(habitacion -> habitacionMapper.toHabitacionResponseDTO(habitacion));

        return new PageResponseDTO<>(paged);
    }

    @Override
    public HabitacionResponseDTO buscarHabitacionPorId(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Habitacion en id " + id + " no encontrado"));
        return habitacionMapper.toHabitacionResponseDTO(habitacion);
    }

    @Override
    public ResponseDTO registrarHabitacion(HabitacionRequestDTO habitacionRequestDTO) {
        Habitacion habitacion = habitacionMapper.toHabitacion(habitacionRequestDTO);

        habitacion.setPiso(habitacionRequestDTO.piso());
        habitacion.setNumero(habitacionRequestDTO.numero());
        habitacion.setTipo(habitacionRequestDTO.tipo());

        habitacionRepository.save(habitacion);
        HabitacionResponseDTO dto = habitacionMapper.toHabitacionResponseDTO(habitacion);

        return new ResponseDTO(ResponseMessage.SUCCESSFUL_ADDITION.getMessage(), dto);
    }

    @Override
    public ResponseDTO updateHabitacion(Long id, HabitacionUpdateDTO habitacionUpdateDTO) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion en id " + id + " no encontrado"));

        habitacion.setPiso(habitacionUpdateDTO.piso());
        habitacion.setNumero(habitacionUpdateDTO.numero());
        habitacion.setTipo(habitacionUpdateDTO.tipo());
        habitacion.setEstado(habitacionUpdateDTO.estado());
        habitacionRepository.save(habitacion);

        HabitacionResponseDTO dto = habitacionMapper.toHabitacionResponseDTO(habitacion);

        return new ResponseDTO(ResponseMessage.SUCCESSFUL_MODIFICATION.getMessage(), dto);
    }

    @Override
    public ResponseDTO eliminarHabitacion(Long id) {
        habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion en id " + id + " no encontrado"));
        habitacionRepository.deleteById(id);
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_DELETION.getMessage(), "Id eliminado : "+ id);
    }
}
