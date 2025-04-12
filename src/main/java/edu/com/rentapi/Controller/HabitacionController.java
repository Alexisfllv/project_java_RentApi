package edu.com.rentapi.Controller;


import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Service.HabitacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
