package edu.com.rentapi.Controller;


import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class RerservaController {

    //
    private final ReservaService reservaService;




    @PostMapping("/crear/plano")
    public ResponseEntity<ResponseDTO> crearReservaPlano(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
        return ResponseEntity.ok(reservaService.crearReservaPlana(reservaRequestDTO));
    }

    // listado
    @GetMapping("/listado")
    public ResponseEntity<PageResponseDTO<PlanoReservaResponseDTO>> listarReservas(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(200).body(reservaService.listadoReservas(pageable));
    }

    // busqueda

    @GetMapping("buscar/{id}")
    public ResponseEntity<PlanoReservaResponseDTO> buscarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarReserva(id));
    }

    @PostMapping("culminar/{id}")
    public ResponseEntity<ResponseDTO> culminarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.culminarReserva(id));
    }

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
