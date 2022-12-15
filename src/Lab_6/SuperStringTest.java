package Lab_6;

import java.util.*;

public class SuperStringTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            SuperString s = new SuperString();
            while (true) {
                int command = jin.nextInt();
                if (command == 0) {//append(String s)
                    s.append(jin.next());
                }
                if (command == 1) {//insert(String s)
                    s.insert(jin.next());
                }
                if (command == 2) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if (command == 3) {//reverse()
                    s.reverse();
                }
                if (command == 4) {//toString()
                    System.out.println(s);
                }
                if (command == 5) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if (command == 6) {//end
                    break;
                }
            }
        }
    }
}

class SuperString {
    LinkedList<String> list_of_strings;
    LinkedList<String> last_added_strings;

    SuperString() {
        list_of_strings = new LinkedList<>();
        last_added_strings = new LinkedList<>();
    }

    public void append(String s) {
        last_added_strings.addLast(s);
        list_of_strings.addLast(s);
    }

    public void insert(String s) {
        last_added_strings.addLast(s);
        list_of_strings.addFirst(s);
    }

    public boolean contains(String s) {
        String listString = list_of_strings.stream()
                .reduce("", (elem, elem2) -> elem + elem2);
        return listString.contains(s);
    }

    public void reverse() {

        Collections.reverse(list_of_strings);

        ListIterator<String> temp_iterator = list_of_strings.listIterator();
        extracted(temp_iterator);

        temp_iterator = last_added_strings.listIterator();
        extracted(temp_iterator);

    }

    private static void extracted(ListIterator<String> iterator_stringList) {
        while (iterator_stringList.hasNext()) {
            iterator_stringList
                    .set(new StringBuilder(iterator_stringList.next()).reverse().toString());
        }
    }

    @Override
    public String toString() {
        return list_of_strings.stream().reduce("", (string1, string2) -> string1 + string2);
    }

    public void removeLast(int k) {
        for (int i = 0; i < k; i++) {
            list_of_strings.remove(last_added_strings.removeLast());
        }
    }

}
