package Lab_3;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }
}

interface Item {
    int getPrice();
    boolean equals(Object obj);
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
    }
}
class ExtraItem implements Item {

    String type;
    ExtraItem(String type) throws InvalidExtraTypeException {
        if(!is_type_valid(type))
            throw new InvalidExtraTypeException();
        this.type = type;
    }

    private boolean is_type_valid(String type) {
        return type.equalsIgnoreCase("Coke") || type.equalsIgnoreCase("Ketchup");
    }

    public int getPrice() {
        if ("Ketchup".equals(type)) {
            return 3;
        }
        return 5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return this.type.equals(extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public String toString() {
        return type;
    }
}

class PizzaItem implements Item {

    String type;

    PizzaItem (String type) throws InvalidPizzaTypeException {
        if(!is_type_valid(type))
            throw new InvalidPizzaTypeException();
        this.type = type;
    }

    private boolean is_type_valid(String type) {
        return type.equalsIgnoreCase("Standard") || type.equalsIgnoreCase("Pepperoni") || type.equalsIgnoreCase("Vegetarian");
    }
    @Override
    public int getPrice() {
        switch (type) {
            case "Standard": return 10;
            case "Pepperoni": return 12;
            default: return 8;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return this.type.equals(pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public String toString() {
        return type;
    }

}

class ItemOutOfStockException extends Exception {
    Item item;

    public ItemOutOfStockException(Item item) {
        super();
        this.item = item;
    }
}

class EmptyOrder extends Exception {
    public EmptyOrder(String text) {
        super(text);
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException() {
    }
}

class Order {


    class Order_entry implements Item{

        private Item item;
        private int count;

        public Order_entry(Item item, int count) {
            this.item = item;
            this.count = count;
        }

        @Override
        public int getPrice() {
            return item.getPrice() * count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Order_entry that = (Order_entry) o;
            return Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item);
        }

        public void setCount (int count) {
            this.count = count;
        }

        public String toString() {
            return String.format("%-15sx %d%5s$",item, count,getPrice());
        }
    }

    ArrayList<Order_entry> order_list = new ArrayList<>();
    private boolean lock;
    Order () {
        lock = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(!lock) {
            if (count > 10) {
                throw new ItemOutOfStockException(item);
            }

            if (order_list.size() != 0) {
                List<Order_entry> collect = order_list.stream().filter(entry -> entry.item.equals(item)).collect(Collectors.toList());
                if(collect.size() !=0) {
                    collect.get(0).setCount(count);
                    return;
                }
            }
            order_list.add(new Order_entry(item, count));
        }
        else throw new OrderLockedException();
    }

    public int getPrice() {
        return order_list.stream().mapToInt(Order_entry::getPrice).sum();
    }

    public void displayOrder() {
        AtomicInteger index = new AtomicInteger(1);
        order_list.forEach(order_entry -> {
            System.out.printf("  %d.%s\n", index.getAndIncrement(), order_entry);
        });
        System.out.printf("Total:%21s$\n", getPrice());
    }

    public void removeItem(int idx) throws OrderLockedException {
        if(!lock) {
            if (idx >= order_list.size() || idx<0)
                throw new ArrayIndexOutOfBoundsException(idx);
            order_list.remove(idx);
        }
        else throw new OrderLockedException();
    }

    public void lock() throws EmptyOrder {
        if(order_list.isEmpty()) {
            throw new EmptyOrder("EmptyOrder");
        }
        lock = true;
    }
}