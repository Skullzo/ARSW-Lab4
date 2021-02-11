package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext contexto = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices blue = contexto.getBean(BlueprintsServices.class);

        Point[] puntos=new Point[]{new Point(40, 40),new Point(15, 15)};
        Blueprint blueprint=new Blueprint("author", "paint",puntos);
        Point[] puntos2=new Point[]{new Point(40, 40),new Point(15, 15)};
        Blueprint blueprint2=new Blueprint("author", "paint",puntos2);

        try {
            blue.addNewBlueprint(blueprint);
            blue.addNewBlueprint(blueprint);
        } catch (BlueprintPersistenceException e) {
            e.printStackTrace();
        }

    }
}