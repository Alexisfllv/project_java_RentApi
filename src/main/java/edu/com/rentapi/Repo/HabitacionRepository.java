package edu.com.rentapi.Repo;

import edu.com.rentapi.Dto.HabitacionResponseDTO;
import edu.com.rentapi.Entity.EstadoHabitacion;
import edu.com.rentapi.Entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

   // jpql
   List<Habitacion> findByEstado(EstadoHabitacion estado);

}
