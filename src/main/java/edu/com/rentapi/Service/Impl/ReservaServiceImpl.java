package edu.com.rentapi.Service.Impl;


import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.EstadoReserva;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Entity.Reserva;
import edu.com.rentapi.Exception.ResourceNotFoundException;
import edu.com.rentapi.Mapper.ReservaMapper;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Repo.HabitacionRepository;
import edu.com.rentapi.Repo.ReservaRepository;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Response.ResponseMessage;
import edu.com.rentapi.Service.ReservaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {


    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;

    private final HabitacionRepository habitacionRepository;


    @Override
    @Transactional
    public ResponseDTO crearReservaPlana(ReservaRequestDTO reservaRequestDTO) {
        Reserva reserva =  reservaMapper.toReserva(reservaRequestDTO);

        reserva.setClienteNombre(reservaRequestDTO.clienteNombre());
        reserva.setClienteDni(reservaRequestDTO.clienteDni());
        reserva.setFechaInicio(reservaRequestDTO.fechaInicio());
        reserva.setFechaFin(reservaRequestDTO.fechaFin());
        reserva.setComentarios(reservaRequestDTO.comentarios());

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

        PlanoReservaResponseDTO dto = reservaMapper.toPlanoReservaResponseDto(reserva);

        // response
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_ADDITION.getMessage(), dto);

    }

    @Override
    public PageResponseDTO<PlanoReservaResponseDTO> listadoReservas(Pageable pageable) {

        Page<PlanoReservaResponseDTO> paged = reservaRepository.findAll(pageable)
                .map(reserva -> reservaMapper.toPlanoReservaResponseDto(reserva));

        return new PageResponseDTO<>(paged);
    }

    @Override
    public PlanoReservaResponseDTO buscarReserva(Long id) {
        Reserva reserva  =reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada :"+id));
        return reservaMapper.toPlanoReservaResponseDto(reserva);
    }

    @Override
    @Transactional
    public ResponseDTO culminarReserva(Long id) {
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
        PlanoReservaResponseDTO dto = reservaMapper.toPlanoReservaResponseDto(reservaExistente);

        return new ResponseDTO(ResponseMessage.SUCCESSFUL_MODIFICATION.getMessage(), dto);
    }

    @Override
    public PageResponseDTO<PlanoReservaResponseDTO> filtradoFechaInicio(Pageable pageable, LocalDateTime desde, LocalDateTime hasta) {

        Page<Reserva> reservaFiltro = reservaRepository.findByFechaCreacionBetween(pageable,desde,hasta);

        Page<PlanoReservaResponseDTO> paged = reservaFiltro
                .map(reserva -> reservaMapper.toPlanoReservaResponseDto(reserva));

        return new PageResponseDTO<>(paged);
    }
}
