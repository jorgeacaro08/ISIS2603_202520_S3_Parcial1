package co.edu.uniandes.dse.parcial1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaRepository extends JpaRepository<RutaEntity, Long> {

}
