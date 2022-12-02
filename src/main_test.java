import java.util.Comparator;
import java.util.function.Function;

public class main_test {
    public static void main(String[] args) {
        Comparator<Person> personComparator = (p1, p2) -> p1.name.compareTo(p2.lastName);
    }

}

class Person {
    String name;
    String lastName;

    public Person(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }


}
