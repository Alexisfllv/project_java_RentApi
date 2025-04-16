package edu.com.rentapi.Mapper;


import edu.com.rentapi.Dto.HabitacionRequestDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Entity.Habitacion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {

    // response -> modelo
    HabitacionResponseDTO toHabitacionResponseDTO(Habitacion habitacion);

    // entity -> request
    Habitacion toHabitacion(HabitacionRequestDTO habitacionRequestDTO);
}
