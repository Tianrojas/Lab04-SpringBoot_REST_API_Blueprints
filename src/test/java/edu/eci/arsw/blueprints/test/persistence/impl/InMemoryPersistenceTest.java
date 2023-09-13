/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.test.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.blueprints.persistence.impl.Tuple;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class InMemoryPersistenceTest {
    
    @Test
    public void saveNewAndLoadTest() throws BlueprintPersistenceException, BlueprintNotFoundException{
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);
        
        ibpp.saveBlueprint(bp0);
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        ibpp.saveBlueprint(bp);
        
        assertNotNull("Loading a previously stored blueprint returned null.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()));
        
        assertEquals("Loading a previously stored blueprint returned a different blueprint.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()), bp);
        
    }


    @Test
    public void saveExistingBpTest() {
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        try {
            ibpp.saveBlueprint(bp);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }
        
        Point[] pts2=new Point[]{new Point(10, 10),new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaint",pts2);

        try{
            ibpp.saveBlueprint(bp2);
            fail("An exception was expected after saving a second blueprint with the same name and autor");
        }
        catch (BlueprintPersistenceException ex){
            
        }
                
        
    }

    @Test
    public void getBluePrintTest() throws BlueprintNotFoundException, BlueprintPersistenceException {
        InMemoryBlueprintPersistence ibpp =new InMemoryBlueprintPersistence();

        Blueprint bp1=new Blueprint("Pedro", "First");

        try {
            ibpp.saveBlueprint(bp1);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }

        List<Blueprint> blueprintList = new ArrayList<Blueprint>();
        blueprintList.add(bp1);

        try {
            assertEquals(bp1, ibpp.getBlueprint("Pedro", "First"));
        } catch (BlueprintNotFoundException e) {
            fail("Blueprint persistence failed querying the blueprints");
        }
    }

    @Test
    public void getBlueprintsByAuthorTest() throws BlueprintNotFoundException, BlueprintPersistenceException {
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Blueprint bp1=new Blueprint("Maria", "First");
        Blueprint bp2=new Blueprint("Pedro", "Second");
        Blueprint bp3=new Blueprint("Maria", "Third");

        try {
            ibpp.saveBlueprint(bp1);
            ibpp.saveBlueprint(bp2);
            ibpp.saveBlueprint(bp3);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }

        Set<Blueprint> blueprintList = new HashSet<>();
        blueprintList.add(bp1);
        blueprintList.add(bp3);

        try {
            assertEquals(blueprintList, ibpp.getBlueprintsByAuthor("Maria"));
        } catch (BlueprintNotFoundException e) {
            fail("Blueprint persistence failed querying the blueprints by the current author.");
        }
    }

    @Test
    public void getAllBlueprintsTest()  {
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Blueprint bp1=new Blueprint("Maria", "First");
        Blueprint bp2=new Blueprint("Pedro", "Second");
        Blueprint bp3=new Blueprint("Maria", "Third");
        Point[] pts=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp=new Blueprint("_authorname_", "_bpname_ ",pts);

        try {
            ibpp.saveBlueprint(bp1);
            ibpp.saveBlueprint(bp2);
            ibpp.saveBlueprint(bp3);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }

        Set<Blueprint> blueprintList = new HashSet<>();
        blueprintList.add(bp1);
        blueprintList.add(bp2);
        blueprintList.add(bp3);

        try {
            Assert.assertEquals(blueprintList, ibpp.getAllBlueprints());
        } catch (BlueprintNotFoundException e) {
            fail("Blueprint persistence failed querying the blueprints.");
        }
    }

}
