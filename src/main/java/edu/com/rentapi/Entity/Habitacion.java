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
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false)
    private int piso;

    @Column(nullable = false)
    private String tipo; // Ej: "Suite", "Simple", "Medium"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHabitacion estado = EstadoHabitacion.DISPONIBLE; // Ej: DISPONIBLE, RESERVADA, MANTENIMIENTO
}
