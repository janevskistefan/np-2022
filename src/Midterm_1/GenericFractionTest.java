package MidTerm_1;

import java.util.Scanner;

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch (ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}

class ZeroDenominatorException extends Exception {
    public ZeroDenominatorException(String msg) {
        super(msg);
    }
}

class GenericFraction<T extends Number, U extends Number> {
    T numerator;
    U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if (denominator.doubleValue() == 0)
            throw new ZeroDenominatorException("Denominator cannot be zero");

        this.numerator = numerator;
        this.denominator = denominator;
    }

    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        return new GenericFraction<Double, Double>
            (gf.numerator.doubleValue() * this.denominator.doubleValue() + gf.denominator.doubleValue() * this.numerator.doubleValue(),
            gf.denominator.doubleValue() * this.denominator.doubleValue());
    }

    static double nzd(double a, double b) {
        if (b == 0)
            return a;
        if (a < b)
            return nzd(a, b - a);
        else
            return nzd(b, a - b);
    }

    public double nzd() {
        return nzd(this.numerator.doubleValue(), this.denominator.doubleValue());
    }
    public double toDouble() {
        return numerator.doubleValue()/denominator.doubleValue();
    }

    public String toString() {
        return String.format("%.2f / %.2f", this.numerator.doubleValue() / this.nzd(), this.denominator.doubleValue() / this.nzd());
    }

}