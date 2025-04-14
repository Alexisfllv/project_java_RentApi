package edu.com.rentapi.Controller;


import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Habitaciones", description = "API para gestion de Reservas")
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class RerservaController {

    private final ReservaService reservaService;

    @Operation(
            summary = "Creacion de reserva",
            description = "Retorna un PlanoReservaResponseDTO despues de la creacion de ReservaRequestDTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ResponseDTO con mensaje exitoso de creacion y PlanoReservaResponseDTO"),
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido por error de Validacion"),
                    @ApiResponse(responseCode = "500", description = "ResponsedTO con mensaje fallido de URL"),
                    @ApiResponse(responseCode = "400", description = "ResponsedTO con mensaje fallido de Mal formato de JSON")
            }
    )
    @PostMapping("/crear/plano")
    public ResponseEntity<ResponseDTO> crearReservaPlano(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
        return ResponseEntity.ok(reservaService.crearReservaPlana(reservaRequestDTO));
    }

    // listado
    @Operation(
            summary = "Listado de reservas paginadas",
            description = "Retorna todas las PlanoReservaResponseDTO  paginadas ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "listado PlanoReservaResponseDTO paginadas"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }    )
    @GetMapping("/listado")
    public ResponseEntity<PageResponseDTO<PlanoReservaResponseDTO>> listarReservas(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(200).body(reservaService.listadoReservas(pageable));
    }


    // busqueda
    @Operation(
            summary = "Busqueda de Reservas",
            description = "Retorna la PlanoReservaResponseDTO buscada por id",
            responses = {
                    @ApiResponse(responseCode = "200", description = " PlanoReservaResponseDTO "),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }    )
    @GetMapping("buscar/{id}")
    public ResponseEntity<PlanoReservaResponseDTO> buscarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarReserva(id));
    }

    @Operation(
            summary = "Culminar el estado de la reserva",
            description = "Enviar el id para culminar esa reserva cambia el estado del campo estadoReserva a CULMINADA",
            responses = {
                    @ApiResponse(responseCode = "200", description = " PlanoReservaResponseDTO "),
                    @ApiResponse(responseCode = "404", description = "ResponseDTO con mensaje fallido de id no encontrado o no existe"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }    )
    @PostMapping("culminar/{id}")
    public ResponseEntity<ResponseDTO> culminarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.culminarReserva(id));
    }

    @Operation(
            summary = "Filtrar por fechas paginadas",
            description = "Busqueda entre fachas de creacion las reservas y paginadas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado PlanoReservaResponseDTO "),
                    @ApiResponse(responseCode = "400", description = "ResponseDTO con mensaje fallido de error de parametros"),
                    @ApiResponse(responseCode = "500", description = "ResponseDTO con mensaje fallido de URL")
            }    )

    @GetMapping("buscar/fecha")
    public ResponseEntity<PageResponseDTO<PlanoReservaResponseDTO>> filtrarReservaFechasCreacion
            (
                    @RequestParam (defaultValue = "0") Integer page,
                    @RequestParam (defaultValue = "3") Integer size,
                    @RequestParam LocalDateTime desde,
                    @RequestParam LocalDateTime hasta
            ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reservaService.filtradoFechaInicio(pageable,desde,hasta));
    }
}
