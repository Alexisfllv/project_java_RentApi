package edu.com.rentapi.Mapper;

import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Dto.ReservaResponseDTO;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Entity.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservaMapper {



    //HabitacionResponseDTO toDto(Habitacion habitacion);


    ReservaResponseDTO toReservaResponseDTO(Reserva reserva);

    @Mapping(target = "habitacion.id", source = "habitacionId")
    Reserva toReserva(ReservaRequestDTO reservaRequestDTO);

    // plano
    @Mapping(target = "habitacionNumero", source = "habitacion.numero")
    @Mapping(target = "habitacionTipo", source = "habitacion.tipo")
    @Mapping(target = "estadoReserva", source = "estado")
    @Mapping(target = "reservaId", source = "id")
    @Mapping(target = "fechaCreacion", source = "fechaCreacion")
    @Mapping(target = "fechaCulminada", source = "fechaCulminada")
    PlanoReservaResponseDTO toPlanoReservaResponseDto(Reserva reserva);

}