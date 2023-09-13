package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsFilter;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import edu.eci.arsw.blueprints.persistence.impl.RedundancyFilter;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BlueprintsServicesTest {

    @Mock
    private BlueprintsPersistence mockBlueprintsPersistence;

    private BlueprintsServices blueprintsServices;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        blueprintsServices = context.getBean(BlueprintsServices.class);
        blueprintsServices.bpp = mockBlueprintsPersistence;
    }

    @Test
    public void testDependencyInjection() {
        // Verificar que las dependencias no sean nulas
        assertNotNull(blueprintsServices.bpp);
        assertNotNull(blueprintsServices.bpf);
    }

    @Test
    public void testGetBlueprintsByAuthor() throws BlueprintNotFoundException, BlueprintPersistenceException {
        // Mocking data
        String author = "testAuthor";
        Blueprint blueprint1 = new Blueprint("Blueprint1", author);
        Blueprint blueprint2 = new Blueprint("Blueprint2", author);
        Set<Blueprint> expectedBlueprints = new HashSet<>();
        expectedBlueprints.add(blueprint1);
        expectedBlueprints.add(blueprint2);

        // Mocking behavior of the persistence layer
        when(mockBlueprintsPersistence.getBlueprintsByAuthor(author)).thenReturn(expectedBlueprints);

        // Testing the service
        Set<Blueprint> result = blueprintsServices.getBlueprintsByAuthor(author);

        // Verifying that the expected data was returned
        assertEquals(expectedBlueprints, result);
    }

    @Test(expected = BlueprintNotFoundException.class)
    public void testGetBlueprintsByAuthorNotFound() throws BlueprintNotFoundException {
        // Mocking data
        String author = "nonExistentAuthor";

        // Mocking behavior of the persistence layer to throw an exception
        when(mockBlueprintsPersistence.getBlueprintsByAuthor(author)).thenThrow(BlueprintNotFoundException.class);

        // Testing the service
        blueprintsServices.getBlueprintsByAuthor(author);
    }

    @Test
    public void testGetBlueprint() throws BlueprintNotFoundException, BlueprintPersistenceException {
        // Mocking data
        String author = "testAuthor";
        String name = "Blueprint";
        Blueprint expectedBlueprint = new Blueprint(name, author);

        // Mocking behavior of the persistence layer
        when(mockBlueprintsPersistence.getBlueprint(author,name)).thenReturn(expectedBlueprint);

        // Testing the service
        Blueprint result = blueprintsServices.getBlueprint(author,name);

        // Verifying that the expected data was returned
        assertEquals(expectedBlueprint, result);
    }

    @Test(expected = BlueprintNotFoundException.class)
    public void testGetBlueprintNotFound() throws BlueprintNotFoundException {
        // Mocking data
        String author = "nonExistentAuthor";
        String name = "nonExistentName";

        // Mocking behavior of the persistence layer to throw an exception
        when(mockBlueprintsPersistence.getBlueprint(author,name)).thenThrow(BlueprintNotFoundException.class);

        // Testing the service
        blueprintsServices.getBlueprint(author,name);
    }

    @Test
    public void testAddNewBlueprint() throws BlueprintPersistenceException {
        // Crear un blueprint de prueba
        Blueprint blueprint = new Blueprint("TestBlueprint", "TestAuthor");

        // Configurar el comportamiento simulado: cuando se llame a saveBlueprint con el blueprint de prueba,
        // no hacer nada (simular el almacenamiento exitoso)
        doNothing().when(mockBlueprintsPersistence).saveBlueprint(blueprint);
        // Llamar al método bajo prueba
        try {
            blueprintsServices.addNewBlueprint(blueprint);
        } catch (BlueprintPersistenceException e) {
            // Debería pasar sin lanzar una excepción
            fail("Exception when adding");
        }

        // Verificar que se llamó a saveBlueprint con el blueprint de prueba
        verify(mockBlueprintsPersistence).saveBlueprint(blueprint);
    }

    @Test
    public void testGetAllBlueprints() throws BlueprintNotFoundException {
        // Crear una lista de blueprints simulados
        Set<Blueprint> expectedBlueprints = new HashSet<>();
        expectedBlueprints.add(new Blueprint("Blueprint1", "Author1"));
        expectedBlueprints.add(new Blueprint("Blueprint2", "Author2"));

        // Configurar el comportamiento simulado: cuando se llame a getAllBlueprints,
        // devolver la lista de blueprints simulados
        when(mockBlueprintsPersistence.getAllBlueprints()).thenReturn(expectedBlueprints);

        // Llamar al método bajo prueba
        Set<Blueprint> result = blueprintsServices.getAllBlueprints();

        // Verificar si el resultado es el esperado
        assertEquals(expectedBlueprints, result);
    }

    //@Ignore
    @Test
    public void blueprintFilterRedundancy() throws BlueprintPersistenceException, BlueprintNotFoundException {

        Point po1 = new Point(1, 1);
        Point po2 = new Point(1, 1);
        Point po3 = new Point(1, 2);
        Point po4 = new Point(2, 2);

        Point[] points = {po1, po2, po3, po4};
        String author = "Sebastian";
        String name = "First";
        Blueprint bp = new Blueprint(author, name, points);
        blueprintsServices.addNewBlueprint(bp);
        when(mockBlueprintsPersistence.getBlueprint(author,name)).thenReturn(bp);
        assertEquals(blueprintsServices.getBlueprint(author, name).getPoints().size(),
                3);
    }

    @Ignore
    @Test
    public void blueprintFilterSubsampling() throws BlueprintPersistenceException, BlueprintNotFoundException {

        Point po1 = new Point(1, 1);
        Point po2 = new Point(1, 1);
        Point po3 = new Point(1, 2);
        Point po4 = new Point(2, 2);

        Point[] points = {po1, po2, po3, po4};
        String author = "Sebastian";
        String name = "First";
        Blueprint bp = new Blueprint(author, name, points);
        blueprintsServices.addNewBlueprint(bp);
        when(mockBlueprintsPersistence.getBlueprint(author,name)).thenReturn(bp);
        assertEquals(blueprintsServices.getBlueprint(author, name).getPoints().size(),
                2);
    }

}

