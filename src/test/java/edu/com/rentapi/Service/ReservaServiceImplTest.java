package edu.com.rentapi.Service;

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
import edu.com.rentapi.Service.Impl.ReservaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

        Habitacion habitacion1 = new Habitacion();
        habitacion1.setId(idHabitacion);
        habitacion1.setNumero(101);
        habitacion1.setPiso(1);
        habitacion1.setTipo("Simple");
        habitacion1.setEstado(EstadoHabitacion.DISPONIBLE);

        Reserva reserva1 = new Reserva();
        reserva1.setId(idReserva);
        reserva1.setHabitacion(habitacion1);
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


    @Test
    void shouldThrowExceptionWhenHabitacionNotFound() {
        // Given
        Long habitacionIdInexistente = 999L;

        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO(
                habitacionIdInexistente,
                "Carlos De",
                "12345678",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                "Reservar para evento"
        );

        // El mapper genera una reserva vacía (necesario para evitar null)
        Reserva reserva = new Reserva();
        when(reservaMapper.toReserva(reservaRequestDTO)).thenReturn(reserva);

        // Simulamos que la habitación no existe
        when(habitacionRepository.findById(habitacionIdInexistente))
                .thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> reservaServiceImpl.crearReservaPlana(reservaRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Habitacion no encontrada :" + habitacionIdInexistente);

        // Verifica que se llamó a los mocks necesarios
        verify(habitacionRepository).findById(habitacionIdInexistente);
        verify(reservaMapper).toReserva(reservaRequestDTO); // sí se usa
        verifyNoInteractions(reservaRepository); // este sí se mantiene
    }

    @Test
    void shouldThrowExceptionWhenHabitacionNotAvailable() {
        // Given
        Long habitacionId = 2L;

        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO(
                habitacionId,
                "Lucía Vargas",
                "87654321",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                "Cliente frecuente"
        );

        Reserva reserva = new Reserva();
        when(reservaMapper.toReserva(reservaRequestDTO)).thenReturn(reserva);

        // Creamos una habitación que NO está disponible
        Habitacion habitacionNoDisponible = new Habitacion(
                habitacionId,
                105,
                1,
                "Suite",
                EstadoHabitacion.MANTENIMIENTO
        );

        when(habitacionRepository.findById(habitacionId))
                .thenReturn(Optional.of(habitacionNoDisponible));

        // When / Then
        assertThatThrownBy(() -> reservaServiceImpl.crearReservaPlana(reservaRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(" la habitacion no esta disponible para reservar :"
                        + habitacionNoDisponible.getEstado());

        verify(habitacionRepository).findById(habitacionId);
        verify(reservaMapper).toReserva(reservaRequestDTO);
        verifyNoInteractions(reservaRepository); // no se debería guardar ninguna reserva
    }


    // page reserva response dto

    @Test
    void shouldListAllReservasPaginated() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 3);

        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);
        Habitacion habitacion2 = new Habitacion(2L, 201, 2, "Basica", EstadoHabitacion.RESERVADA);

        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        reserva1.setHabitacion(habitacion1);
        reserva1.setClienteNombre("Ana");
        reserva1.setClienteDni("11112222");
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));
        reserva1.setComentarios("Comentario 1");
        reserva1.setEstado(EstadoReserva.REALIZADA);

        Reserva reserva2 = new Reserva();
        reserva1.setId(2L);
        reserva1.setHabitacion(habitacion2);
        reserva1.setClienteNombre("Luis");
        reserva1.setClienteDni("33334444");
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 2));
        reserva1.setFechaFin(LocalDate.of(2025, 5, 6));
        reserva1.setComentarios("Comentario 2");
        reserva1.setEstado(EstadoReserva.REALIZADA);

        List<Reserva> reservas = List.of(reserva1, reserva2);

        Page<Reserva> pageReservas = new PageImpl<>(reservas,pageable,reservas.size());

        PlanoReservaResponseDTO planoReservaResponseDTO1 = new PlanoReservaResponseDTO(
                1L,
                101,
                "Matrimonial",
                "Ana",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                "Comentario 1",
                EstadoReserva.REALIZADA,
                LocalDateTime.of(2025, 4, 1, 10, 30),
                null
        );

        PlanoReservaResponseDTO planoReservaResponseDTO2 = new PlanoReservaResponseDTO(
                2L,
                201,
                "Basica",
                "Luis",
                LocalDate.of(2025, 5, 2),
                LocalDate.of(2025, 5, 6),
                "Comentario 2",
                EstadoReserva.REALIZADA,
                LocalDateTime.of(2025, 4, 1, 10, 30),
                null
        );

        when(reservaRepository.findAll(pageable)).thenReturn(pageReservas);
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO1);
        when(reservaMapper.toPlanoReservaResponseDto(reserva2)).thenReturn(planoReservaResponseDTO2);

        // act
        PageResponseDTO<PlanoReservaResponseDTO> resultado = reservaServiceImpl.listadoReservas(pageable);

        // Assert

        assertThat(resultado).isNotNull();
        assertThat(resultado.content()).hasSize(2);
        assertThat(resultado.content()).containsExactly(planoReservaResponseDTO1, planoReservaResponseDTO2);
        assertThat(resultado.totalElements()).isEqualTo(2);
        assertThat(resultado.page()).isEqualTo(0);
        assertThat(resultado.size()).isEqualTo(3);

        //verificar

        verify(reservaRepository).findAll(pageable);
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);
        verify(reservaMapper).toPlanoReservaResponseDto(reserva2);

    }

    @Test
    void shouldReturnReservaWhenIdExists(){

        // Given
        Long idReserva = 1L;
        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);

        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        reserva1.setHabitacion(habitacion1);
        reserva1.setClienteNombre("Ana");
        reserva1.setClienteDni("11112222");
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));
        reserva1.setComentarios("Comentario 1");
        reserva1.setEstado(EstadoReserva.REALIZADA);

        PlanoReservaResponseDTO planoReservaResponseDTO1 = new PlanoReservaResponseDTO(
                1L,
                101,
                "Matrimonial",
                "Ana",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                "Comentario 1",
                EstadoReserva.REALIZADA,
                LocalDateTime.of(2025, 4, 1, 10, 30),
                null
        );

        // When

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva1));
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO1);

        // ACT
        PlanoReservaResponseDTO resultado = reservaServiceImpl.buscarReserva(idReserva);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.reservaId()).isEqualTo(idReserva);
        assertThat(resultado.habitacionNumero()).isEqualTo(101);
        assertThat(resultado.habitacionTipo()).isEqualTo("Matrimonial");
        assertThat(resultado.clienteNombre()).isEqualTo("Ana");
        assertThat(resultado.fechaInicio()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(resultado.fechaFin()).isEqualTo(LocalDate.of(2025, 5, 5));
        assertThat(resultado.comentarios()).isEqualTo("Comentario 1");
        assertThat(resultado.estadoReserva()).isEqualTo(EstadoReserva.REALIZADA);
        assertThat(resultado.fechaCreacion()).isEqualTo(LocalDateTime.of(2025, 4, 1, 10, 30));
        assertThat(resultado.fechaCulminada()).isNull();

        verify(reservaRepository).findById(idReserva);
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);

    }


    @Test
    void shouldReturnReservaWhenIdDoesNotExist(){

        // Given
        Long idReserva = 999L;

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> reservaServiceImpl.buscarReserva(idReserva))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Reserva no encontrada :"+idReserva);

        // THen
        verify(reservaRepository).findById(idReserva);

    }

    //
    @Test
    void shouldCulminarReservaWhenIdExists(){

        // Given
        Long idReserva = 1L;
        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);

        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        reserva1.setHabitacion(habitacion1);
        reserva1.setClienteNombre("Ana");
        reserva1.setClienteDni("11112222");
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));
        reserva1.setComentarios("Comentario 1");
        reserva1.setEstado(EstadoReserva.REALIZADA);

        PlanoReservaResponseDTO planoReservaResponseDTO1 = new PlanoReservaResponseDTO(
                1L,
                101,
                "Matrimonial",
                "Ana",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 5),
                "Comentario 1",
                EstadoReserva.CULMINADA,
                LocalDateTime.of(2025, 4, 1, 10, 30),
                LocalDateTime.of(2025, 4, 5, 10, 30)
        );


        // When
        when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva1));
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(planoReservaResponseDTO1);

        ResponseDTO responseDTO = reservaServiceImpl.culminarReserva(idReserva);


        // Assert
        assertThat(responseDTO.mensaje()).isEqualTo("Modification completed successfully");
        assertThat(responseDTO.data()).isEqualTo(planoReservaResponseDTO1);
        assertThat(((PlanoReservaResponseDTO) responseDTO.data()).reservaId()).isEqualTo(idReserva);
        assertThat(reserva1.getEstado()).isEqualTo(EstadoReserva.CULMINADA);
        assertThat(reserva1.getFechaCulminada()).isNotNull();
        assertThat(reserva1.getHabitacion().getEstado()).isEqualTo(EstadoHabitacion.DISPONIBLE);

        // verify
        verify(reservaRepository).findById(idReserva);
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);
        verify(reservaRepository).save(reserva1);

    }

    @Test
    void shouldCulminarReservaWhenIdExistsNotFound(){
        // Given
        Long idReserva = 999L;

        when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> reservaServiceImpl.culminarReserva(idReserva))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Reserva no encontrada :"+idReserva);

        // THen
        verify(reservaRepository).findById(idReserva);
    }


    @Test
    void shouldCulminarReservaWhenIdDoesNotExist(){
        // Arrange
        Pageable pageable = PageRequest.of(0, 3);
        LocalDateTime desde = LocalDateTime.of(2025, 4, 1, 0, 0);
        LocalDateTime hasta = LocalDateTime.of(2025, 4, 30, 23, 59);

        Habitacion habitacion1 = new Habitacion(1L, 101, 1, "Matrimonial", EstadoHabitacion.RESERVADA);

        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);
        reserva1.setHabitacion(habitacion1);
        reserva1.setClienteNombre("Ana");
        reserva1.setClienteDni("11112222");
        reserva1.setFechaInicio(LocalDate.of(2025, 5, 1));
        reserva1.setFechaFin(LocalDate.of(2025, 5, 5));
        reserva1.setComentarios("Comentario 1");
        reserva1.setEstado(EstadoReserva.REALIZADA);
        reserva1.setFechaCreacion(LocalDateTime.of(2025, 4, 10, 12, 0));

        PlanoReservaResponseDTO dto = new PlanoReservaResponseDTO(
                1L,
                101,
                "Simple",
                "Carlos",
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 3),
                "Comentario",
                EstadoReserva.REALIZADA,
                reserva1.getFechaCreacion(),
                null
        );

        Page<Reserva> reservas = new PageImpl<>(List.of(reserva1),pageable,1);

        when(reservaRepository.findByFechaCreacionBetween(pageable,desde,hasta)).thenReturn(reservas);
        when(reservaMapper.toPlanoReservaResponseDto(reserva1)).thenReturn(dto);

        // Act
        PageResponseDTO<PlanoReservaResponseDTO> resultado = reservaServiceImpl.filtradoFechaInicio(pageable,desde,hasta);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.content()).hasSize(1);
        assertThat(resultado.content().get(0)).isEqualTo(dto);
        assertThat(resultado.totalElements()).isEqualTo(1);
        assertThat(resultado.page()).isEqualTo(0);
        assertThat(resultado.size()).isEqualTo(3);

        // verificar
        verify(reservaRepository).findByFechaCreacionBetween(pageable,desde,hasta);
        verify(reservaMapper).toPlanoReservaResponseDto(reserva1);
    }
}



