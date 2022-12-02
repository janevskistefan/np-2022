package Midterm_1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class WeatherStationTest {

    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}

class WeatherStation {
    List<WeatherReading> weatherReadingList;
    int max_age_reading;

    WeatherStation(int days) {
        weatherReadingList = new ArrayList<WeatherReading>();
        max_age_reading = days;
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date) {

        // Potential problem:
        if(weatherReadingList.stream().noneMatch(reading -> (date.getTime() - reading.date.getTime()) < 2500)) {

            // Potential problem2:
            weatherReadingList = weatherReadingList.stream()
                    .filter(reading -> ((date.getTime() - reading.date.getTime())/1000/60/60/24) < max_age_reading)
                    .collect(Collectors.toList());

            weatherReadingList.add(new WeatherReading(temperature,wind,humidity,visibility,date));
        }
    }

    public int total() {
        return weatherReadingList.size();
    }

    public void status(Date from ,Date to) {

        List<WeatherReading> collect = weatherReadingList.stream()
                .filter(reading -> reading.date.after(from) && reading.date.before(to))
                .sorted(Comparator.comparing(d -> d.date))
                .collect(Collectors.toList());

        if (collect.isEmpty())
            throw new RuntimeException("Chicholina");
        else {
            collect.forEach(System.out::println);
            System.out.printf("Average temperature: %.2f", collect.stream().mapToDouble(reading -> reading.temperature).average().getAsDouble());
        }
    }
}

class WeatherReading{
    float temperature;
    float wind;
    float humidity;
    float visibility;
    Date date;

    public WeatherReading(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km ", temperature, wind, humidity, visibility) + date;
    }
}

interface Compare {

}

interface Compare_child extends Compare {

}

class Box<? extends Compare> {

}