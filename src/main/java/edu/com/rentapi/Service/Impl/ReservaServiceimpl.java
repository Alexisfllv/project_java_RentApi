package edu.com.rentapi.Service.Impl;


import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Dto.ReservaResponseDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.EstadoReserva;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Entity.Reserva;
import edu.com.rentapi.Exception.ResourceNotFoundException;
import edu.com.rentapi.Mapper.HabitacionMapper;
import edu.com.rentapi.Mapper.ReservaMapper;
import edu.com.rentapi.Repo.HabitacionRepository;
import edu.com.rentapi.Repo.ReservaRepository;
import edu.com.rentapi.Service.ReservaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaServiceimpl implements ReservaService {

    //
    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    //
    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Transactional
    @Override
    public ReservaResponseDTO crearReserva(ReservaRequestDTO reservaRequestDTO) {
        Reserva reserva =  reservaMapper.toReserva(reservaRequestDTO);

        reserva.setEstado(EstadoReserva.REALIZADA);
        reserva.setClienteNombre(reservaRequestDTO.clienteNombre());
        reserva.setClienteDni(reservaRequestDTO.clienteDni());
        reserva.setFechaInicio(reservaRequestDTO.fechaInicio());
        reserva.setFechaFin(reservaRequestDTO.fechaFin());
        reserva.setComentarios(reservaRequestDTO.comentarios());

        // habitacion
        Habitacion habitacion = habitacionRepository.findById(reservaRequestDTO.habitacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada :"+reservaRequestDTO.habitacionId()));

        if (!habitacion.getEstado().equals(EstadoHabitacion.DISPONIBLE)) {
            log.warn("Habitacion estado :"+habitacion.getEstado());
            throw new ResourceNotFoundException(" la habitacion no esta disponible par reservar :"+ habitacion.getEstado());

        }


        habitacion.setEstado(EstadoHabitacion.RESERVADA);
        habitacionRepository.save(habitacion);

        reserva.setHabitacion(habitacion);

        reservaRepository.save(reserva);

        // response
        return reservaMapper.toReservaResponseDTO(reserva);
    }

    @Override
    @Transactional
    public PlanoReservaResponseDTO crearReservaPlana(ReservaRequestDTO reservaRequestDTO) {
        Reserva reserva =  reservaMapper.toReserva(reservaRequestDTO);

        reserva.setClienteNombre(reservaRequestDTO.clienteNombre());
        reserva.setClienteDni(reservaRequestDTO.clienteDni());
        reserva.setFechaInicio(reservaRequestDTO.fechaInicio());
        reserva.setFechaFin(reservaRequestDTO.fechaFin());
        reserva.setComentarios(reservaRequestDTO.comentarios());
        //
        reserva.setFechaCreacion(LocalDateTime.now());


        // habitacion
        Habitacion habitacion = habitacionRepository.findById(reservaRequestDTO.habitacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada :"+reservaRequestDTO.habitacionId()));

        if (!habitacion.getEstado().equals(EstadoHabitacion.DISPONIBLE)) {
            log.warn("Habitacion estado :"+habitacion.getEstado());
            throw new ResourceNotFoundException(" la habitacion no esta disponible para reservar :"+ habitacion.getEstado());

        }


        log.info("estado :"+ reserva.getEstado());
        habitacion.setEstado(EstadoHabitacion.RESERVADA);
        habitacionRepository.save(habitacion);

        reserva.setHabitacion(habitacion);

        reservaRepository.save(reserva);

        // response
        return reservaMapper.toPlanoReservaResponseDto(reserva);

    }

    @Override
    public List<PlanoReservaResponseDTO> listadoReservas() {
        List<Reserva> reservas = reservaRepository.findAll();

        return  reservas
                .stream()
                .map(reserva -> reservaMapper.toPlanoReservaResponseDto(reserva))
                .toList();
    }

    @Override
    public PlanoReservaResponseDTO buscarReserva(Long id) {
        Reserva reserva  =reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada :"+id));
        return reservaMapper.toPlanoReservaResponseDto(reserva);
    }

    @Override
    @Transactional
    public PlanoReservaResponseDTO culminarReserva(Long id) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada :"+id));

        reservaExistente.setFechaCulminada(LocalDateTime.now());
        reservaExistente.setEstado(EstadoReserva.CULMINADA);

        // habitacion
        if (reservaExistente.getHabitacion().getEstado().equals(EstadoHabitacion.DISPONIBLE)) {
            log.warn("Habitacion estado :"+reservaExistente.getHabitacion().getEstado());
            throw new ResourceNotFoundException(" la habitacion ya se encuentra disponible:"+ reservaExistente.getHabitacion().getEstado());

        }
        reservaExistente.getHabitacion().setEstado(EstadoHabitacion.DISPONIBLE);

        reservaRepository.save(reservaExistente);
        return reservaMapper.toPlanoReservaResponseDto(reservaExistente);
    }


}
