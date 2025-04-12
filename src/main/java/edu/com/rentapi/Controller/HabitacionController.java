package edu.com.rentapi.Controller;


import edu.com.rentapi.Dto.HabitacionRequestDTO;
import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Dto.HabitacionUpdateDTO;
import edu.com.rentapi.Pagination.PageResponseDTO;
import edu.com.rentapi.Response.ResponseDTO;
import edu.com.rentapi.Service.HabitacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HabitacionController {

    private final HabitacionService habitacionService;

    //
    @GetMapping("/disponibles")
    public ResponseEntity<List<HabitacionResponseDTO>> disponibles() {
        return ResponseEntity.status(200).body(habitacionService.listarHabitacionesConEstadoDisponible());
    }


    // listar

    @GetMapping("/listar")
    public ResponseEntity<PageResponseDTO<HabitacionResponseDTO>> paginadoListadoHabitacion(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(200).body(habitacionService.listarHabitaciones(pageable));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<HabitacionResponseDTO> buscarHabitacion(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.buscarHabitacionPorId(id));
    }

    // registrar
    @PostMapping("/crear")
    public ResponseEntity<ResponseDTO> crearHabitacion(@RequestBody HabitacionRequestDTO habitacionRequestDTO) {
        return ResponseEntity.status(201).body(habitacionService.registrarHabitacion(habitacionRequestDTO));
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ResponseDTO> modificarHabitacion(@PathVariable Long id, @RequestBody HabitacionUpdateDTO habitacionUpdateDTO) {
        return ResponseEntity.ok(habitacionService.updateHabitacion(id, habitacionUpdateDTO));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ResponseDTO> eliminarHabitacion(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.eliminarHabitacion(id));
    }
}
