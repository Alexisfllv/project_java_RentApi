package edu.com.rentapi.Service;

import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Dto.ReservaResponseDTO;

public interface ReservaService {


    // datos con relacion
    ReservaResponseDTO crearReserva(ReservaRequestDTO reservaRequestDTO);


    // datos en formato plano
    PlanoReservaResponseDTO crearReservaPlana(ReservaRequestDTO reservaRequestDTO);
}
