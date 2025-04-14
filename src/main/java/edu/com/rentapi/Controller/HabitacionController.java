package edu.com.rentapi.Controller;


import edu.com.rentapi.Dto.HabitacionRequestDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Dto.HabitacionUpdateDTO;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Service.HabitacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Habitaciones", description = "API para gestion de Habitaciones")
@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HabitacionController {

    private final HabitacionService habitacionService;

    //
    @Operation(
            summary = "Creacion de habitacion",
            description = "Retorna todas las habitaciones disponibles con la condicion de *DISPONIBLES* de habitacion",
            responses = {
                    @ApiResponse(responseCode = "200", description = "listado HabitacionResponseDTO disponibles"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }
    )
    @GetMapping("/disponibles")
    public ResponseEntity<List<HabitacionResponseDTO>> disponibles() {
        return ResponseEntity.status(200).body(habitacionService.listarHabitacionesConEstadoDisponible());
    }

    // listar

    @Operation(
            summary = "Listado de habitaciones paginadas",
            description = "Retorna todas las habitaciones disponibles paginadas ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "listado HabitacionResponseDTO paginadas"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<PageResponseDTO<HabitacionResponseDTO>> paginadoListadoHabitacion(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(200).body(habitacionService.listarHabitaciones(pageable));
    }

    @Operation(
            summary = "Busqueda de Habitaciones por id",
            description = "Retorna habitacion buscada por el ID ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "HabitacionResponseDTO"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<HabitacionResponseDTO> buscarHabitacion(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.buscarHabitacionPorId(id));
    }

    @Operation(
            summary = "Creacion de Habitacion",
            description = "Creacion de habitacion con modelo de HabitacionRequestDTO y retonar un HabitacionResponseDTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de creacion y HabitacionResponseDTO"),
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido por error de Validacion"),
                    @ApiResponse(responseCode = "500", description = "ResponsedTO con mensaje fallido de URL"),
                    @ApiResponse(responseCode = "400", description = "ResponsedTO con mensaje fallido de Mal formato de JSON")
            }
    )
    // registrar
    @PostMapping("/crear")
    public ResponseEntity<ResponseDTO> crearHabitacion(@Valid @RequestBody HabitacionRequestDTO habitacionRequestDTO) {
        return ResponseEntity.status(201).body(habitacionService.registrarHabitacion(habitacionRequestDTO));
    }

    @Operation(
            summary = "Modificar habitacion por Id",
            description = "Modifica la habitacion con HabitacionRequestDTO por id y retorna HabitacionResponseDTO ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de modificacion y HabitacionResponseDTO"),
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido por error de Validacion"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL"),
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido de Mal formato de JSON")
            }
    )
    @PutMapping("/modificar/{id}")
    public ResponseEntity<ResponseDTO> modificarHabitacion(@PathVariable Long id, @RequestBody HabitacionUpdateDTO habitacionUpdateDTO) {
        return ResponseEntity.ok(habitacionService.updateHabitacion(id, habitacionUpdateDTO));
    }

    @Operation(
            summary = "Eliminar habitacion por Id",
            description = "Elimina la habitacion por el id de @PathVariable Ingresado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de eliminacion"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }
    )
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ResponseDTO> eliminarHabitacion(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.eliminarHabitacion(id));
    }
}
