package Lab_7;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}

class TermFrequency {
    TreeMap<String, Integer> word_map;
    TermFrequency(InputStream inputStream, String[] stopWords) {
        word_map = new TreeMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        bufferedReader.lines().forEach(line -> {
            if(!line.isEmpty()) {

                line = line.replaceAll("[.,]", "");

                String [] lineArray = line.split("[\\s+\n]");
                ArrayList<String> filteredWords = new ArrayList<>();

                for (String s : lineArray)
                    if(!s.isEmpty())
                        filteredWords.add(s.toLowerCase());

                filteredWords = filteredWords.stream()
                        .filter(word -> Arrays.stream(stopWords).noneMatch(stopWord -> stopWord.compareTo(word.toLowerCase()) == 0))
                        .collect(Collectors.toCollection(ArrayList::new));

                filteredWords.forEach(word -> {
                    if(word_map.containsKey(word.toLowerCase())) {
                        word_map.replace(word, word_map.get(word) + 1);
                    } else {
                        word_map.put(word, 1);
                    }
                });
            }
        });
    }

    public int countTotal() {
        return word_map.values().stream().mapToInt(val -> val).sum();
    }

    public int countDistinct() {
        return word_map.size();
    }

    public List<String> mostOften(int k){
        return word_map.entrySet().stream().sorted((e1, e2) -> {
            if(Objects.equals(e2.getValue(), e1.getValue()))
                return e1.getKey().compareToIgnoreCase(e2.getKey());
            return e2.getValue().compareTo(e1.getValue());
        }).limit(k).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}