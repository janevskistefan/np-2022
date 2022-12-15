package np_aud9.audition_problem;

import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Audition {
    HashMap<String, HashSet<Participant>> audition_map = new HashMap<>();
    void addParticipant(String city, String code, String name, int age) {
        audition_map.computeIfAbsent(city, value -> new HashSet<>());
        audition_map.get(city).add(new Participant(code, name, age));
    }

    void listByCity(String city) {
        Set<Participant> sortedSet = new TreeSet<>(Comparator
                .comparing(Participant::getName)
                .thenComparingInt(Participant::getAge)
                .thenComparing(Participant::getCode));

        sortedSet.addAll(audition_map.get(city));
        sortedSet.forEach(System.out::println);
    }
}

class Participant{
    private final String code;
    public final String name;
    public final int age;

    public Participant(String code, String name, int age) {
        this.code = String.copyValueOf(code.toCharArray());
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return code.equals(((Participant) o).code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getCode());
    }

    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}