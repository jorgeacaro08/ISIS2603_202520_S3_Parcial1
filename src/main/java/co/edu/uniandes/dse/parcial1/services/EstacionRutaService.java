package co.edu.uniandes.dse.parcial1.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;

@Service
@Transactional
public class EstacionRutaService {
    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private EstacionRepository estacionRepository;

public RutaEntity addEstacionRuta(Long estacionId, Long rutaId) throws EntityNotFoundException {
    EstacionEntity estacion = estacionRepository.findById(estacionId).orElseThrow(() -> new EntityNotFoundException("Estacion no encontrada"));
    RutaEntity ruta = rutaRepository.findById(rutaId).orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada"));
    if (!estacion.getRutas().contains(ruta)) {
            estacion.getRutas().add(ruta);
        }
        return ruta;
} 


public void removeRuta(Long estacionId, Long rutaId) throws EntityNotFoundException {
    EstacionEntity estacion = estacionRepository.findById(estacionId).orElseThrow(() -> new EntityNotFoundException("Estacion no encontrada"));
    RutaEntity ruta = rutaRepository.findById(rutaId).orElseThrow(() -> new EntityNotFoundException("Ruta no encontrada"));

    if (estacion.getRutas().contains(ruta)) {
            estacion.getRutas().remove(ruta);
        }
    }

public List<RutaEntity> getRutas(Long estacionId) throws EntityNotFoundException {
    EstacionEntity equipo = estacionRepository.findById(estacionId).orElseThrow(() -> new EntityNotFoundException("Estacion no encontrada"));;
    return new ArrayList<>(equipo.getRutas());
    }
}