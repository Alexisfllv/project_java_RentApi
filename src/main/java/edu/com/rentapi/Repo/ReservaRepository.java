package edu.com.rentapi.Repo;

import edu.com.rentapi.Dto.PlanoReservaResponseDTO;
import edu.com.rentapi.Entity.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {


    // findByFechaInicioBetween
    Page<Reserva> findByFechaCreacionBetween(Pageable pageable, LocalDateTime desde, LocalDateTime hasta);


}