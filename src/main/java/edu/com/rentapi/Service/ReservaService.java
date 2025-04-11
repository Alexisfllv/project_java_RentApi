package edu.com.rentapi.Service;

import edu.com.rentapi.Dto.HabitacionDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Dto.ReservaResponseDTO;
import edu.com.rentapi.Entity.Reserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {

    ReservaResponseDTO crearReserva(ReservaRequestDTO reservaRequestDTO);

}
