package edu.com.rentapi.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la habitación (muchas reservas pueden ser de una habitación)
    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    // Datos del cliente como texto (no entidad, porque no es socio/usuario registrado)
    @Column(name = "cliente_nombre", nullable = false)
    private String clienteNombre;

    @Column(name = "cliente_dni", nullable = false)
    private String clienteDni;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "comentarios")
    private String comentarios;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private EstadoHabitacion estado = EstadoHabitacion.RESERVADA; // Reutilizas el enum para saber si está RESERVADA, etc.

}
