package MidTerm_1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
class Triple<T extends Number> {
    T num1, num2, num3;

    public Triple(T num1, T num2, T num3) {
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
    }

    public double max() {
        return Double.max(Double.max(num1.doubleValue(), num2.doubleValue()), num3.doubleValue());
    }

    public double avarage() {
        return (num1.doubleValue()+num2.doubleValue()+num3.doubleValue()) / 3;
    }

    public void sort() {
        ArrayList<T> collect = Stream.of(num1, num2, num3)
                .sorted(Comparator.comparingDouble(Number::doubleValue))
                .collect(Collectors.toCollection(ArrayList::new));
        num1 = collect.get(0);
        num2 = collect.get(1);
        num3 = collect.get(2);
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", num1.doubleValue(), num2.doubleValue(), num3.doubleValue());
    }
}


