package Lab_1;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}

class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        if (n>=1000)
            return "M"+toRoman(n-1000);
        else if (n>=900)
            return "CM"+toRoman(n-900);
        else if (n>=500)
            return "D"+toRoman(n-500);
        else if (n>=400)
            return "CD"+toRoman(n-400);
        else if (n>=100)
            return "C"+toRoman(n-100);
        else if (n>=90)
            return "XC"+toRoman(n-90);
        else if (n>=50)
            return "L"+toRoman(n-50);
        else if (n>=40)
            return "XL"+toRoman(n-40);
        else if (n>=10)
            return "X"+toRoman(n-10);
        else if (n == 9)
            return "IX"+toRoman(n-9);
        else if (n>=5)
            return "V"+toRoman(n-5);
        else if (n == 4)
            return "IV"+toRoman(n-4);
        else if (n>=1)
            return "I"+toRoman(n-1);
        else
            return "";
    }

}
