package np_aud9.names_problem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Names {

    public static Map<String, Integer> getMapFromFile(String path) throws IOException {
        LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<>();
        Files.lines(Paths.get(path))
                .forEach(line -> {
                    String[] lineArr = line.split("\\s");
                    hashMap.put(lineArr[0], Integer.parseInt(lineArr[1]));
                });
        return hashMap;
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> boyMap = getMapFromFile("src/np_aud9/boyNames.txt");
        Map<String, Integer> girlMap = getMapFromFile("src/np_aud9/girlNames.txt");

        Set<String> allNames = new HashSet<>();
        allNames.addAll(boyMap.keySet());
        allNames.addAll(girlMap.keySet());

        //allNames.stream()
        //        .filter(name -> boyMap.containsKey(name) && girlMap.containsKey(name))
        //        .sorted((u1, u2) -> Integer.compare(boyMap.get(u2)+girlMap.get(u2), boyMap.get(u1)+girlMap.get(u1)))
        //        .forEach(name ->
        //                System.out.printf("%s : Male: %d Female: %d Total: %d\n"
        //                        , name
        //                        , boyMap.get(name)
        //                        , girlMap.get(name)
        //                        , boyMap.get(name) + girlMap.get(name))
        //        );

        // Chesto baranje na 2 kolokvium

        Map<String, Integer> uniSex = new HashMap<>();
        allNames.stream()
                .filter(name -> boyMap.containsKey(name) && girlMap.containsKey(name))
                .forEach(name -> uniSex.put(name, boyMap.get(name)+girlMap.get(name)));

        Set<Map.Entry<String, Integer>> entries = uniSex.entrySet();
        entries.stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> System.out.printf("%s : %d\n", entry.getKey(), entry.getValue()));

    }
}
