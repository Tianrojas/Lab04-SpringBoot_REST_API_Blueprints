package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintsFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
public class SubsamplingFilter implements BlueprintsFilter {

    @Override
    public Blueprint blueprintsFilter(Blueprint bp) {
        List<Point> points = new ArrayList<>();
        if(bp.getPoints() != null && !bp.getPoints().isEmpty()){
            for (Integer x = 0; x < bp.getPoints().size(); x++) {
                if (x % 2 == 0) {
                    points.add(bp.getPoints().get(x));
                }
            }
        }
        bp.setPoints(points);
        return bp;
    }
}
