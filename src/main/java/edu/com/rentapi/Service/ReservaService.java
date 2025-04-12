package edu.com.rentapi.Service;

import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Dto.ReservaResponseDTO;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Response.ResponseDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaService {



    // datos en formato plano
    ResponseDTO crearReservaPlana(ReservaRequestDTO reservaRequestDTO);

    // listado de reservas
    PageResponseDTO<PlanoReservaResponseDTO> listadoReservas(Pageable pageable);

    // busqueda de reservas
    PlanoReservaResponseDTO buscarReserva(Long id);

    // culminar reserva
    ResponseDTO culminarReserva(Long id);

    // filtrado de fechas
    PageResponseDTO<PlanoReservaResponseDTO> filtradoFechaInicio(Pageable pageable, LocalDateTime desde, LocalDateTime hasta);
}
