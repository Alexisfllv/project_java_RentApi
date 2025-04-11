package edu.com.rentapi.Mapper;


import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Entity.Habitacion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {

    // response -> modelo
    HabitacionResponseDTO toHabitacionResponseDTO(Habitacion habitacion);
}
