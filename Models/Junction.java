package Models;

import java.util.ArrayList;

public class Junction implements Comparable<Junction>{
    String id;
    String type;
    double x;
    double y;
    String[] incLanes;
    String[] intLanes;
    public Shape shape;
    ArrayList<Request> requests = new ArrayList<Request>();

    public Junction(String id, String type, String x, String y, String incLanes, String intLanes, String shape, ArrayList<Request> requests) {
        this.id = id;
        this.type = type;
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
        if (incLanes != "") this.incLanes = incLanes.split(" ");
        if (intLanes != "") this.intLanes = intLanes.split(" ");
        if (shape != "") this.shape = Shape.parseShape(shape);
        this.requests = requests;
    }

    @Override
    public int compareTo(Junction that) {
        return this.id.compareTo(that.id);
    } 
}
