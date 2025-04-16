package edu.com.rentapi.Service;


import edu.com.rentapi.Dto.HabitacionRequestDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Dto.HabitacionUpdateDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.Habitacion;
import edu.com.rentapi.Exception.ResourceNotFoundException;
import edu.com.rentapi.Mapper.HabitacionMapper;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Repo.HabitacionRepository;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Service.Impl.HabitacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HabitacionServiceImplTest {

    // creacion de mocks de instancias a probar
    @Mock
    private HabitacionRepository habitacionRepository;
    @Mock
    private HabitacionMapper habitacionMapper;

    @InjectMocks // instancia real de mocks injectados
    private HabitacionServiceImpl habitacionServiceImpl;


    // listar disponibles
    @Test
    void shouldListAllHabitacionesDisponiblesTest() {
        // Arrange
        Habitacion habitacion1 = new Habitacion(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);
        Habitacion habitacion2 = new Habitacion(2L,2,201,"Basica",EstadoHabitacion.DISPONIBLE);
        Habitacion habitacion3 = new Habitacion(3L,3,301,"Vip",EstadoHabitacion.RESERVADA);

        List<Habitacion> habitaciones = List.of(habitacion1,habitacion2,habitacion3);

        HabitacionResponseDTO dto1 =  new HabitacionResponseDTO(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);
        HabitacionResponseDTO dto2 =  new HabitacionResponseDTO(2L,2,301,"Basica",EstadoHabitacion.DISPONIBLE);


        // WHEN
        when(habitacionRepository.findByEstado(EstadoHabitacion.DISPONIBLE)).thenReturn(List.of(habitacion1,habitacion2));
        when(habitacionMapper.toHabitacionResponseDTO(habitacion1)).thenReturn(dto1);
        when(habitacionMapper.toHabitacionResponseDTO(habitacion2)).thenReturn(dto2);

        List<HabitacionResponseDTO> resultado = habitacionServiceImpl.listarHabitacionesConEstadoDisponible();

        // THEN

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado).containsExactly(dto1,dto2);

        // Verify
        verify(habitacionRepository).findByEstado(EstadoHabitacion.DISPONIBLE);
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion1);
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion2);

    }



    @Test
    void shouldListAllHabitacionesPaginated() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 3);
        Habitacion habitacion1 = new Habitacion(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);
        Habitacion habitacion2 = new Habitacion(2L,2,202,"Simple",EstadoHabitacion.MANTENIMIENTO);

        List<Habitacion> listaHabitaciones = List.of(habitacion1,habitacion2);
        Page<Habitacion> pageHabitaciones = new PageImpl<>(listaHabitaciones,pageable,listaHabitaciones.size());

        HabitacionResponseDTO dto1 = new HabitacionResponseDTO(1L,1,101,"Simple",EstadoHabitacion.DISPONIBLE);
        HabitacionResponseDTO dto2 = new HabitacionResponseDTO(2L,2,202,"Simple",EstadoHabitacion.MANTENIMIENTO);

        when(habitacionRepository.findAll(pageable)).thenReturn(pageHabitaciones);
        when(habitacionMapper.toHabitacionResponseDTO(habitacion1)).thenReturn(dto1);
        when(habitacionMapper.toHabitacionResponseDTO(habitacion2)).thenReturn(dto2);

        // Act

        PageResponseDTO<HabitacionResponseDTO> resultado = habitacionServiceImpl.listarHabitaciones(pageable);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.content()).hasSize(2);
        assertThat(resultado.content()).containsExactly(dto1,dto2);
        assertThat(resultado.totalElements()).isEqualTo(2);
        assertThat(resultado.page()).isEqualTo(0);
        assertThat(resultado.size()).isEqualTo(3);

        // Verificacion
        verify(habitacionRepository).findAll(pageable);
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion1);
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion2);

    }




    @Test
    void shouldReturnHabitacionWhenIdExists(){
        // Given
        Long id = 1L;
        Habitacion habitacion =  new Habitacion(id,101,1,"Basico", EstadoHabitacion.DISPONIBLE);
        HabitacionResponseDTO habitacionResponseDTO = new HabitacionResponseDTO(id,1,101,"Basico", EstadoHabitacion.DISPONIBLE);

        when(habitacionRepository.findById(id)).thenReturn(Optional.of(habitacion));
        when(habitacionMapper.toHabitacionResponseDTO(habitacion)).thenReturn(habitacionResponseDTO);

        // When
        HabitacionResponseDTO resultado = habitacionServiceImpl.buscarHabitacionPorId(id);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.numero()).isEqualTo(101);
        assertThat(resultado.tipo()).isEqualTo("Basico");
        assertThat(resultado.estado()).isEqualTo(EstadoHabitacion.DISPONIBLE);
        verify(habitacionRepository,times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenHabitacionNotFound(){

        // Given
        Long id = 999L;
        when(habitacionRepository.findById(id)).thenReturn(Optional.empty());

        // When /Then
        assertThatThrownBy(()-> habitacionServiceImpl.buscarHabitacionPorId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Habitacion en id " + id + " no encontrado");

        verify(habitacionRepository,times(1)).findById(id);
    }

    // metodo de registrar habitacion

    @Test
    void shouldRegisterHabitacionSuccessfully() {
        // Given (preparacion)
        HabitacionRequestDTO habitacionRequestDTO = new HabitacionRequestDTO(1,101,"Basico");
        Habitacion habitacion = new Habitacion(1L,101,1,"Basico", EstadoHabitacion.DISPONIBLE);
        HabitacionResponseDTO habitacionResponseDTO = new HabitacionResponseDTO(1L,1,101,"Basico", EstadoHabitacion.DISPONIBLE);

        // Mocks de flujo interno
        when(habitacionMapper.toHabitacion(habitacionRequestDTO)).thenReturn(habitacion);
        when(habitacionRepository.save(habitacion)).thenReturn(habitacion);
        when(habitacionMapper.toHabitacionResponseDTO(habitacion)).thenReturn(habitacionResponseDTO);


        // When (ejecutar)
        ResponseDTO response =  habitacionServiceImpl.registrarHabitacion(habitacionRequestDTO);

        // Then (verificacion)
        assertThat(response.mensaje()).isEqualTo("Added successfully");
        assertThat(response.data()).isInstanceOf(HabitacionResponseDTO.class);
        assertThat(((HabitacionResponseDTO) response.data()).id()).isEqualTo(1L);


        // verificar que se haya llamado lo esperado
        verify(habitacionRepository,times(1)).save(habitacion);
        verify(habitacionMapper).toHabitacionResponseDTO(habitacion);
        verify(habitacionMapper).toHabitacion(habitacionRequestDTO);

    }


    @Test
    void shouldUpdateHabitacionSuccessfully(){

        // Given
        Long id = 1L;
        Habitacion habitacionExistente =  new Habitacion(id,101,1,"Basico", EstadoHabitacion.DISPONIBLE);
        HabitacionUpdateDTO habitacionUpdateDTO =  new HabitacionUpdateDTO(2,202,"Doble", EstadoHabitacion.MANTENIMIENTO);
        Habitacion HabitacionActualizada =  new Habitacion(id,202,2,"Doble", EstadoHabitacion.MANTENIMIENTO);
        HabitacionResponseDTO habitacionResponseDTO =  new HabitacionResponseDTO(id,2,202,"Doble", EstadoHabitacion.MANTENIMIENTO);

        when(habitacionRepository.findById(id)).thenReturn(Optional.of(habitacionExistente));
        when(habitacionRepository.save(any(Habitacion.class))).thenReturn(HabitacionActualizada);
        when(habitacionMapper.toHabitacionResponseDTO(HabitacionActualizada)).thenReturn(habitacionResponseDTO);

        // ACTION
        ResponseDTO responseDTO = habitacionServiceImpl.updateHabitacion(id,habitacionUpdateDTO);

        // Assert Verificacion

        assertThat(responseDTO).isNotNull(); // datos poblados correctamente
        assertThat(responseDTO.mensaje()).isEqualTo("Modification completed successfully");
        assertThat(responseDTO.data()).isInstanceOf(HabitacionResponseDTO.class);

        HabitacionResponseDTO result = (HabitacionResponseDTO) responseDTO.data();

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.numero()).isEqualTo(202);
        assertThat(result.piso()).isEqualTo(2);
        assertThat(result.tipo()).isEqualTo("Doble");
        assertThat(result.estado()).isEqualTo(EstadoHabitacion.MANTENIMIENTO);

        // Verificar llamadas
        verify(habitacionRepository).findById(id);
        verify(habitacionRepository).save(habitacionExistente);
        verify(habitacionMapper).toHabitacionResponseDTO(HabitacionActualizada);

    }


    @Test
    void shouldUpdateHabitacionNotFound(){
        // Given
        Long id = 999L;
        when(habitacionRepository.findById(id)).thenReturn(Optional.empty());

        HabitacionUpdateDTO habitacionUpdateDTO = new HabitacionUpdateDTO(1,101,"Basico", EstadoHabitacion.DISPONIBLE);

        // When / Then
        assertThatThrownBy(()-> habitacionServiceImpl.updateHabitacion(id, habitacionUpdateDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Habitacion en id " + id + " no encontrado");

        verify(habitacionRepository,times(1)).findById(id);
    }


    @Test
    void shouldDeleteHabitacionSuccessfully() {
        // Arrange
        Long id = 1L;
        Habitacion habitacion =  new Habitacion(id,101,1,"Basico", EstadoHabitacion.DISPONIBLE);

        // habitacion existe
        when(habitacionRepository.findById(id)).thenReturn(Optional.of(habitacion));

        // Act
        ResponseDTO responseDTO = habitacionServiceImpl.eliminarHabitacion(id);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.mensaje()).isEqualTo("Deletion completed successfully");
        assertThat(responseDTO.data()).isEqualTo("Id eliminado : "+ id);

        // Verify interacciones
        verify(habitacionRepository).findById(id);
        verify(habitacionRepository).deleteById(id);
    }


    @Test
    void shouldDeleteHabitacionNotFound(){
        // Given
        Long id = 999L;

        when(habitacionRepository.findById(id)).thenReturn(Optional.empty());

        //Wheb / Then

        assertThatThrownBy(() -> habitacionServiceImpl.eliminarHabitacion(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Habitacion en id " + id + " no encontrado");

        verify(habitacionRepository,times(1)).findById(id);

    }
}
