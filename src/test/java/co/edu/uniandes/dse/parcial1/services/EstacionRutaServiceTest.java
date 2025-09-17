package co.edu.uniandes.dse.parcial1.services;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;



@DataJpaTest
@Transactional
@Import(EstacionRutaService.class)
public class EstacionRutaServiceTest {

    @Autowired
    private EstacionRutaService estacionRutaService;

 
    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory fabrica = new PodamFactoryImpl();

    private EstacionEntity estacion;
    private final List<RutaEntity> rutasSeed = new ArrayList<>();

    @BeforeEach
    void setUp() throws EntityNotFoundException, IllegalOperationException {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from RutaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EstacionEntity").executeUpdate();
        rutasSeed.clear();
    }

    private void insertData() throws EntityNotFoundException, IllegalOperationException {
        estacion = fabrica.manufacturePojo(EstacionEntity.class);

        // 3 rutas persistidas y asociadas vía servicio
        for (int i = 0; i < 3; i++) {
            RutaEntity m = fabrica.manufacturePojo(RutaEntity.class);
            entityManager.persist(m);

            estacionRutaService.addEstacionRuta(estacion.getId(), m.getId());
            rutasSeed.add(entityManager.find(RutaEntity.class, m.getId()));
        }
        estacion = entityManager.find(EstacionEntity.class, estacion.getId());
    }

    //ADD

    @Test
    void testAddRuta_ok() throws EntityNotFoundException {
        RutaEntity borrador = fabrica.manufacturePojo(RutaEntity.class);
        entityManager.persist(borrador);
        final RutaEntity nuevo = entityManager.find(RutaEntity.class, borrador.getId());

        RutaEntity asociado = estacionRutaService.addEstacionRuta(estacion.getId(), nuevo.getId());
        assertNotNull(asociado);
        assertEquals(nuevo.getId(), asociado.getId());

        List<RutaEntity> enEstacion = estacionRutaService.getRutas(estacion.getId());
    }

    @Test
    void testAddRuta_estacionInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            RutaEntity m = fabrica.manufacturePojo(RutaEntity.class);
            entityManager.persist(m);
            estacionRutaService.addEstacionRuta(0L, m.getId());
        });
    }

    @Test
    void testAddRuta_rutaInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            estacionRutaService.addEstacionRuta(estacion.getId(), 0L);
        });
    }





@Test
    void testRemoveRuta_ok() throws EntityNotFoundException {
        RutaEntity m = rutasSeed.get(0);
        estacionRutaService.removeRuta(estacion.getId(), m.getId());

        List<RutaEntity> actual = estacionRutaService.getRutas(estacion.getId());
    }

    @Test
    void testRemoveRuta_estacionInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            RutaEntity m = rutasSeed.get(0);
            estacionRutaService.removeRuta(0L, m.getId());
        });
    }

    @Test
    void testRemoveRuta_rutaInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            estacionRutaService.removeRuta(estacion.getId(), 0L);
        });
    }
    }




