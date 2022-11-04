package Lab_2;

import java.util.*;
import java.util.stream.Collectors;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                //TODO - exception handling -- see test cases
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                // TODO - exception handling -- see test cases
                collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException() {
        super();
    }
    // TODO - String arg in constructor?
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException() {
        super();
    }
    // TODO - String arg in constructor?
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();
}

class MovablePoint implements Movable {

    private int x, y;
    int xSpeed, ySpeed;

    MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + y + ")";
    }

    @Override
    public void moveUp() {
        y += ySpeed;
    }

    @Override
    public void moveLeft() {
        x -= xSpeed;
    }

    @Override
    public void moveRight() {
        x += xSpeed;
    }

    @Override
    public void moveDown() {
        y -= ySpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }
}

class MovableCircle implements Movable {

    private int radius;
    MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    public String toString() {
        return "Movable circle with center coordinates (" + center.getCurrentXPosition() + center.getCurrentYPosition() + ") and radius " + radius;
    }

    @Override
    public void moveUp() {
        center.moveUp();
    }

    @Override
    public void moveLeft() {
        center.moveLeft();
    }

    @Override
    public void moveRight() {
        center.moveRight();
    }

    @Override
    public void moveDown() {
        center.moveDown();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }


    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    public int getRadius() {
        return radius;
    }
}

class MovablesCollection {
    Movable[] movable;
    static int max_x = 0;
    static int max_y = 0;

    MovablesCollection(int x_MAX, int y_MAX) {
        max_x = x_MAX;
        max_y = y_MAX;
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if(!is_in_bounds(m))
            throw new MovableObjectNotFittableException();
        movable = Arrays.copyOf(movable, movable.length + 1);
        movable[movable.length - 1] = m;

    }

    private boolean is_in_bounds (Movable m) {
       if (m.getClass().toString().compareTo(CirclesTest.class.toString()) == 0) {
           return is_in_bounds_circle((MovableCircle) m);
       }
       return is_in_bounds_point((MovablePoint) m);
    }
    private boolean is_in_bounds_point(MovablePoint m) {
        return m.getCurrentXPosition() >= 0 &&
                m.getCurrentXPosition() <= max_x &&
                m.getCurrentYPosition() >= 0 &&
                m.getCurrentYPosition() <= max_y;
    }

    private boolean is_in_bounds_circle(MovableCircle m) {
        return is_in_bounds_point(new MovablePoint(m.getCurrentXPosition(), m.getCurrentYPosition(), 0, 0)) &&
                is_in_bounds_point(new MovablePoint(m.getCurrentXPosition() - m.getRadius(), m.getCurrentYPosition(), 0, 0)) &&
                is_in_bounds_point(new MovablePoint(m.getCurrentXPosition() + m.getRadius(), m.getCurrentYPosition(), 0, 0)) &&
                is_in_bounds_point(new MovablePoint(m.getCurrentXPosition(), m.getCurrentYPosition() - m.getRadius(), 0, 0)) &&
                is_in_bounds_point(new MovablePoint(m.getCurrentXPosition(), m.getCurrentYPosition() + m.getRadius(), 0, 0));
    }

    private void move_all_circles (DIRECTION direction) {
        List<Movable> circles = Arrays.stream(movable).filter(elem -> elem.getClass().toString().compareTo(MovableCircle.class.toString())==0).collect(Collectors.toList());
        if(direction == DIRECTION.UP){
            circles.forEach(circle -> {
                try {
                    if(is_in_bounds_circle(circle.getCurrentYPosition()))
                } catch (MovableObjectNotFittableException e){
                    e.getMessage();
                }

            });
        }
    }

    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction){

    }
}