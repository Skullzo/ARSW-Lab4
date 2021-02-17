package edu.eci.arsw.blueprints.test.persistence.impl;
import edu.eci.arsw.blueprints.filters.RedundancyFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
public class RedundancyFilterTest {
    @Test
    public void filterRepeated() {
        RedundancyFilter prueba = new RedundancyFilter();
        Point puntos[] = {new Point(1, 1), new Point(1, 1), new Point(2, 2), new Point(2, 2), new Point(3, 3), new Point(3, 3)};
        Blueprint blueprint = new Blueprint("mack", "mypaint", puntos);
        blueprint = prueba.filter(blueprint);
        List<Point> resPuntos = new ArrayList<>();
        resPuntos.add(new Point(1, 1));
        resPuntos.add(new Point(2, 2));
        resPuntos.add(new Point(3, 3));
        assertTrue(blueprint.getPoints().size() == resPuntos.size());
        List<Point> res = blueprint.getPoints();
        for (int i = 0; i < res.size(); i++){
            assertEquals(resPuntos.get(i),res.get(i));
        }
    }
}