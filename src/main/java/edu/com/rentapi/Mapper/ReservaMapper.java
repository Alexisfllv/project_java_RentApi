package edu.com.rentapi.Mapper;

import edu.com.rentapi.Dto.HabitacionDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Dto.ReservaResponseDTO;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Entity.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservaMapper {



    HabitacionDTO toDto(Habitacion habitacion);


    ReservaResponseDTO toReservaResponseDTO(Reserva reserva);

    @Mapping(target = "habitacion.id", source = "habitacionId")
    Reserva toReserva(ReservaRequestDTO reservaRequestDTO);

}