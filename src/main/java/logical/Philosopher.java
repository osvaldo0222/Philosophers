package logical;

import javafx.scene.shape.Circle;

import java.awt.*;

public class Philosopher {
    private int id;
    private int state;
    private Circle circle;
    private Point point;

    public Philosopher(){
        createCircle();
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Circle createCircle(){
        Circle r1 = new Circle();
        setCircle(r1);
        getCircle().setRadius(40);
        return getCircle();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
