package edu.com.rentapi.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int piso;

    private int numero;

    private String tipo;

    // El estado inicial será DISPONIBLE cuando se registre la habitación
    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estado = EstadoHabitacion.DISPONIBLE;
}
