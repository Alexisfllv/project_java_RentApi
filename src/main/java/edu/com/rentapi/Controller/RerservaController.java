package edu.com.rentapi.Controller;


import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Dto.ReservaRequestDTO;
import edu.com.rentapi.Dto.ReservaResponseDTO;
import edu.com.rentapi.Service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class RerservaController {

    //
    private final ReservaService reservaService;

    @PostMapping("/crear")
    public ResponseEntity<ReservaResponseDTO> crearReserva(@RequestBody ReservaRequestDTO reservaRequestDTO) {
        return ResponseEntity.ok(reservaService.crearReserva(reservaRequestDTO));
    }


    @PostMapping("/crear/plano")
    public ResponseEntity<PlanoReservaResponseDTO> crearReservaPlano(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
        return ResponseEntity.ok(reservaService.crearReservaPlana(reservaRequestDTO));
    }

    // listado
    @GetMapping("/listado")
    public ResponseEntity<List<PlanoReservaResponseDTO>> listarReservas() {
        return ResponseEntity.status(200).body(reservaService.listadoReservas());
    }

    // busqueda

    @GetMapping("buscar/{id}")
    public ResponseEntity<PlanoReservaResponseDTO> buscarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.buscarReserva(id));
    }

    @PostMapping("culminar/{id}")
    public ResponseEntity<PlanoReservaResponseDTO> culminarReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.culminarReserva(id));
    }
}
