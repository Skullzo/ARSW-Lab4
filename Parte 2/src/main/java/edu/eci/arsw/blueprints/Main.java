package edu.eci.arsw.blueprints;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class Main {
    public static void main(String[] args) throws BlueprintPersistenceException, BlueprintNotFoundException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bps = ac.getBean(BlueprintsServices.class);
        bps.addNewBlueprint(new Blueprint("Alejandro","bp1"));
        Blueprint bp1=bps.getBlueprint("Alejandro","bp1");
        System.out.println(bp1);
        bps.addNewBlueprint(new Blueprint("David","bp2"));
        System.out.println(bps.getBlueprintsByAuthor("David"));
        Point[] points= new Point[] {new Point(1,2),new Point(3,4),new Point(1,2)};
        bps.addNewBlueprint(new Blueprint("Alejandro","bp3",points));
        System.out.println(bps.getAllBlueprints());
        Blueprint bp3=bps.getBlueprint("Alejandro","bp3");
        System.out.println(bps.filter(bp3).getPoints());
    }
}