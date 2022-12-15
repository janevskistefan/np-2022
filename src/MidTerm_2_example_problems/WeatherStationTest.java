package MidTerm_2_example_problems;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            ws.addMeasurement(temp, wind, hum, vis, date);
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
            System.out.println(e);
        }
    }
}

@FunctionalInterface
interface DateDifference {
    long calculate(Date d1, Date d2);
}

class WeatherStation {
    ArrayList<Reading> readings;
    int days_in_past;
    final double MS_IN_ONE_DAY = 86400000.0;
    final double MS_IN_TWO_AND_A_HALF_MINUTES = 150000.0;
    Function<Date, ArrayList<Reading>> getReadingsBefore = (Date currentDate) ->
            readings.stream()
                    .filter(reading -> reading.date.getTime() < currentDate.getTime())
                    .collect(Collectors.toCollection(ArrayList::new));

    DateDifference dateDifferenceCalculator = (Date d1, Date d2) ->
            d1.getTime() - d2.getTime();

    BiFunction<Stream<Reading>, Date, Boolean> shouldEntryBeIgnored = (Stream<Reading> s_reading, Date d) -> {
        if(readings.size()>0)
            return s_reading.anyMatch(reading -> Math.abs(reading.getDate().getTime() - d.getTime()) < MS_IN_TWO_AND_A_HALF_MINUTES);
        return false;
    };

    Function<Integer, Double> getMsFromDays = (Integer day) -> day * MS_IN_ONE_DAY;

    public WeatherStation(int days_in_past) {
        this.days_in_past = days_in_past;
        this.readings = new ArrayList<>(days_in_past);
    }

    public void addMeasurement(double temperature, double wind, double humidity, double visibility, Date date) {

        if (!shouldEntryBeIgnored.apply(readings.stream(), date)) {

            getReadingsBefore.apply(date)
                    .stream()
                    .filter(d -> dateDifferenceCalculator.calculate(date, d.date) > getMsFromDays.apply(days_in_past))
                    .forEach(reading -> readings.remove(reading));

            readings.add(new Reading(temperature,wind,humidity,visibility,date));
        }

    }

    public int total() {
        return readings.size();
    }

    public void status(Date from, Date to) {
        ArrayList<Reading> collect = readings.stream()
                .filter(reading -> (reading.getDate().after(from) && reading.getDate().before(to)) || reading.getDate().equals(from) || reading.getDate().equals(to))
                .sorted(Comparator.comparingLong(reading -> reading.getDate().getTime()))
                .collect(Collectors.toCollection(ArrayList::new));
        if(collect.isEmpty())
            throw new RuntimeException();
        else {
            collect.forEach(System.out::println);
            System.out.printf("Average temperature: %.2f\n", collect.stream().mapToDouble(Reading::getTemperature).average().getAsDouble());
        }
    }

    public static void main(String[] args) throws ParseException {
       //WeatherStation ws = new WeatherStation(4);
       //DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("10.12.2013 21:30:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("11.12.2013 22:30:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:30:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:35:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:40:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:40:25"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:39:36"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:45:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:50:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:51:12"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("17.12.2013 23:57:15"));
       //ws.addMeasurment(24.6, 80.2, 28.7,  51.7, df.parse("18.12.2013 00:00:15"));
       //ws.status(df.parse("17.12.2013 23:35:15"), df.parse("17.12.2013 23:50:15"));
    }

}

class Reading {
    double temperature;
    double wind;
    double humidity;
    double visibility;
    Date date;

    public Reading(double temperature, double wind, double humidity, double visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWind() {
        return wind;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String
    toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s", temperature, wind, humidity, visibility, date);
    }
}