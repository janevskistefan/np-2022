package Lab_2;

import java.sql.SQLOutput;
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
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    // TODO - potrebno e da se ispechati MovableCircle with center... can not be fitted into the collection ako ne e ispolnet uslov.
                    System.out.println(e.getMessage());
                }
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
    Movable obj;
    public ObjectCanNotBeMovedException(Movable obj) {
        super();
        this.obj = obj;
    }

   public void point_out_of_bounds(){
       System.out.printf("Point (%d,%d) is out of bounds\n", obj.getCurrentXPosition(), obj.getCurrentYPosition());
   }
}

class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String text) {
        super(text);
    }
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
        return "Movable point with coordinates (" + x + "," + y + ")";
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        y += ySpeed;
        if (MovablesCollection.not_in_bounds(this)) {
            y -= ySpeed;
            throw new ObjectCanNotBeMovedException(new MovablePoint(x,y+ySpeed,xSpeed,ySpeed));
        }
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        x -= xSpeed;
        if (MovablesCollection.not_in_bounds(this)) {
            x += xSpeed;
            throw new ObjectCanNotBeMovedException(new MovablePoint(x-xSpeed, y, xSpeed, ySpeed));
        }
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        x += xSpeed;
        if (MovablesCollection.not_in_bounds(this)) {
            x -= xSpeed;
            throw new ObjectCanNotBeMovedException(new MovablePoint(x+xSpeed, y, xSpeed, ySpeed));
        }
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        y -= ySpeed;
        if (MovablesCollection.not_in_bounds(this)) {
            y += ySpeed;
            throw new ObjectCanNotBeMovedException(new MovablePoint(x,y-ySpeed, xSpeed, ySpeed));
        }
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
        return "Movable circle with center coordinates (" + center.getCurrentXPosition() + "," + center.getCurrentYPosition() + ") and radius " + radius;
    }

    @Override
    public void moveUp() {
        try {
            center.moveUp();
        } catch (ObjectCanNotBeMovedException e) {
            e.point_out_of_bounds();
        }
    }

    @Override
    public void moveLeft() {
        try {
            center.moveLeft();
        } catch (ObjectCanNotBeMovedException e) {
            e.point_out_of_bounds();
        }
    }

    @Override
    public void moveRight() {
        try {
            center.moveRight();
        } catch (ObjectCanNotBeMovedException e) {
            e.point_out_of_bounds();
        }
    }

    @Override
    public void moveDown() {
        try {
            center.moveDown();
        } catch (ObjectCanNotBeMovedException e) {
            e.point_out_of_bounds();
        }
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

        if(m.getClass().toString().compareTo(MovableCircle.class.toString())==0) {
            MovableCircle temp = (MovableCircle) m;
            MovablePoint center = temp.center;
            MovablePoint up = new MovablePoint(center.getCurrentXPosition(), center.getCurrentYPosition()+temp.getRadius(),0,0);
            MovablePoint down = new MovablePoint(center.getCurrentXPosition(), center.getCurrentYPosition()- temp.getRadius(),0,0);
            MovablePoint left = new MovablePoint(center.getCurrentXPosition()- temp.getRadius(), center.getCurrentYPosition(), 0,0);
            MovablePoint right = new MovablePoint(center.getCurrentXPosition()+ temp.getRadius(), center.getCurrentYPosition(), 0,0);
            if(not_in_bounds(center) || not_in_bounds(up) || not_in_bounds(down) || not_in_bounds(left) || not_in_bounds(right))
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", center.getCurrentXPosition(), center.getCurrentYPosition(), temp.getRadius()));
        } else {
            if(not_in_bounds((MovablePoint)m))
                throw new MovableObjectNotFittableException(m.toString());
        }

        if(movable == null) {
            movable = new Movable[1];
            movable[0] = m;
            return;
        }

        Movable[] temp_array = new Movable[movable.length+1];
        System.arraycopy(movable, 0, temp_array, 0, movable.length);
        temp_array[movable.length] = m;
        movable = temp_array;
    }

    public static boolean not_in_bounds(Movable m) {
        return m.getCurrentXPosition() < 0 ||
                m.getCurrentXPosition() >= max_x ||
                m.getCurrentYPosition() < 0 ||
                m.getCurrentYPosition() >= max_y;
    }

    private void move_all_circles(DIRECTION direction) {
        List<Movable> circles = Arrays.stream(movable)
                .filter(elem -> elem.getClass().toString().compareTo(MovableCircle.class.toString()) == 0)
                .collect(Collectors.toList());
        //System.out.println("move_all_circles: " + circles);
        move_list_points(direction, circles);
    }

    private void move_all_points(DIRECTION direction) {
        List<Movable> points = Arrays.stream(movable)
                .filter(element -> element.getClass().toString().compareTo(MovablePoint.class.toString())==0)
                .collect(Collectors.toList());
        //System.out.println("move_all_points: " + points);
        move_list_points(direction, points);
    }

    private void move_list_points(DIRECTION direction, List<Movable> points) {
        points.forEach(point -> {
           if(direction == DIRECTION.UP) {
               try {
                   point.moveUp();
               } catch (ObjectCanNotBeMovedException e) {
                  e.point_out_of_bounds();
               }
           } else if (direction == DIRECTION.DOWN) {
               try {
                   point.moveDown();
               } catch (ObjectCanNotBeMovedException e) {
                    e.point_out_of_bounds();
               }
           } else if (direction == DIRECTION.RIGHT) {
               try {
                   point.moveRight();
               } catch (ObjectCanNotBeMovedException e) {
                    e.point_out_of_bounds();
               }
           } else {
               try {
                   point.moveLeft();
               } catch (ObjectCanNotBeMovedException e) {
                    e.point_out_of_bounds();
               }

           }
        });
    }

    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        if (type == TYPE.CIRCLE) {
            move_all_circles(direction);
        } else {
            move_all_points(direction);
        }
    }

    public static void setxMax(int max_x) {MovablesCollection.max_x = max_x;}
    public static void setyMax(int max_y) {MovablesCollection.max_y = max_y;}
    @Override
    public String toString() {
        StringBuilder tempString = new StringBuilder();
        tempString.append("Collection of movable objects with size ").append(movable.length).append(":\n");
        for(Movable elem: movable)
            tempString.append(elem).append("\n");
        return tempString.toString();
    }
}