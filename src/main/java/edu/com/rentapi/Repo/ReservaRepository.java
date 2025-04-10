package edu.com.rentapi.Repo;

import edu.com.rentapi.Entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {


    @Query("""
        SELECT r FROM Reserva r
        WHERE r.estado = 'CONFIRMADA'
        AND r.habitacion.id = :habitacionId
        AND (r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio)
        """)
    List<Reserva> findReservasEnRango(Long habitacionId, LocalDate
            fechaInicio, LocalDate fechaFin);
}