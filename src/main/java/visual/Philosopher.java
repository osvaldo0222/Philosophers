package visual;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class Philosopher {
    private int id;
    private int state;
    private Rectangle rectangle;
    private Point point;

    public Philosopher(){
        createRectangle();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Rectangle createRectangle(){
        Rectangle r1 = new Rectangle();
        setRectangle(r1);
        getRectangle().setHeight(70);
        getRectangle().setWidth(70);
        getRectangle().setRotate(45);
        return getRectangle();
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
