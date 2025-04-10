package edu.com.rentapi.Mapper;

@Mapper(componentModel = "spring")
public interface ReservaMapper {
    HabitacionDTO toDto(Habitacion habitacion);
    ReservaResponse toResponse(Reserva reserva);
}