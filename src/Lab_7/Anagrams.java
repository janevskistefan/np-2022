package Lab_7;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collector;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter printWriter = new PrintWriter(System.out, true);
        LinkedHashMap<AnagramKey, ArrayList<String>> anagramMap = new LinkedHashMap<>();
        bufferedReader.lines().forEach(line -> {
            AnagramKey temp_key = new AnagramKey(line);
            if (!anagramMap.containsKey(temp_key))
                anagramMap.put(temp_key, new ArrayList<>());
            anagramMap.get(temp_key).add(line);
        });
        System.out.println(anagramMap.values().stream()
                .filter(strings -> strings.size() >= 5)
                .collect(Collector.of(
                        () -> new StringJoiner("\n"),
                        (stringJoiner1, arrayList) -> stringJoiner1.add(getProperFormat(arrayList)),
                        StringJoiner::merge,
                        StringJoiner::toString)));
    }

    public static String getProperFormat(ArrayList<String> list) {
        int len = list.size();
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for(;i<len-1;i++) {
            stringBuilder.append(list.get(i));
            stringBuilder.append(" ");
        }
        stringBuilder.append(list.get(i));
        return stringBuilder.toString();
    }
}


class AnagramKey {

    ArrayList<Character> letters;

    AnagramKey(String input) {
        letters = getArrayListFromString(input);
    }

    private ArrayList<Character> getArrayListFromString(String input_str) {
        ArrayList<Character> temp_set = new ArrayList<>();
        for (char letter : input_str.toCharArray())
            temp_set.add(letter);
        temp_set.sort(Character::compareTo);
        return temp_set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnagramKey that = (AnagramKey) o;

        if (this.letters.size() != that.letters.size())
            return false;

        Iterator<Character> this_iterator = this.letters.iterator();
        Iterator<Character> that_iterator = that.letters.iterator();
        while (this_iterator.hasNext()) {
            if (!this_iterator.next().equals(that_iterator.next()))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letters);
    }
}
