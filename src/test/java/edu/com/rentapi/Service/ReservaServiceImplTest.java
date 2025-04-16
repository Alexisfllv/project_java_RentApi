package edu.com.rentapi.Service;

import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.EstadoReserva;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Entity.Reserva;
import edu.com.rentapi.Mapper.ReservaMapper;
import edu.com.rentapi.Repo.HabitacionRepository;
import edu.com.rentapi.Repo.ReservaRepository;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Service.Impl.ReservaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceImplTest {

    //
    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaServiceImpl reservaServiceImpl;

    @Test
    void crearReservaPlana() {
        // Arrange
        Long idHabitacion = 1L;
        Long idReserva = 1L;

        Habitacion habitacion1 = new Habitacion(idHabitacion, 101, 1, "Simple", EstadoHabitacion.DISPONIBLE);

        Reserva reserva1 = new Reserva();
        reserva1.setId(idReserva);
        reserva1.setClienteNombre("Juan Pérez");
        reserva1.setClienteDni("12345678");
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));
        reserva1.setComentarios("Vista al mar");
        reserva1.setEstado(EstadoReserva.REALIZADA);

        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO(
                idHabitacion,
                "Juan Pérez",
                "12345678",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                "Vista al mar"
        );

        PlanoReservaResponseDTO planoReservaResponseDTO = new PlanoReservaResponseDTO(
                idReserva,
                101,
                "Simple",
                "Juan Pérez",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                "Vista al mar",
                EstadoReserva.REALIZADA,
                LocalDateTime.of(2025, 4, 1, 10, 30),
                null
        );

        // Mocks
        when(habitacionRepository.findById(idHabitacion)).thenReturn(Optional.of(habitacion1));
        when(reservaMapper.toReserva(reservaRequestDTO)).thenReturn(reserva1);
        when(reservaRepository.save(reserva1)).thenReturn(reserva1);
        when(habitacionRepository.save(habitacion1)).thenReturn(habitacion1);
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO);

        // Act
        ResponseDTO responseDTO = reservaServiceImpl.crearReservaPlana(reservaRequestDTO);

        // Assert
        assertThat(habitacion1.getEstado()).isEqualTo(EstadoHabitacion.RESERVADA);
        assertThat(responseDTO.mensaje()).isEqualTo("Added successfully");
        assertThat(responseDTO.data()).isEqualTo(planoReservaResponseDTO);


        verify(habitacionRepository).findById(idHabitacion);
        verify(reservaMapper).toReserva(reservaRequestDTO);
        verify(habitacionRepository).save(habitacion1);
        verify(reservaRepository).save(reserva1);
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);
    }


}



