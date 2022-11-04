package Lab_2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class InsufficientElementsException extends Exception {
    public InsufficientElementsException(String input) {
        super(input);
    }
}

class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException(String input) {
        super(input);
    }
}

class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException(String input) {
        // TODO - Potencijalan problem, mozhda treba neshto da se prati kako argument.
        super(input);
    }
}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }

}

class MatrixReader {
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {

        Scanner sc = new Scanner(input);

        int num_rows = sc.nextInt();
        int num_columns = sc.nextInt();

        ArrayList<Double> temp_list = new ArrayList<>();

        while (sc.hasNextDouble()) {
            temp_list.add(sc.nextDouble());
        }

        return new DoubleMatrix(temp_list.stream().mapToDouble(i -> i).toArray(), num_rows, num_columns);
    }
}

class DoubleMatrix {
    double[][] matrix;

    DoubleMatrix(double[] a, int y, int x) throws InsufficientElementsException {
        matrix = new double[y][x];
        int starting_index = 0;
        if (a.length < x * y)
            throw new InsufficientElementsException("Insufficient number of elements");
        if (a.length > x * y) {
            starting_index = a.length - x * y;
        }
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                matrix[i][j] = a[starting_index++];
            }
        }
    }

    public String getDimensions() {
        return "[" + matrix.length + " x " + matrix[0].length + "]";
    }

    public int rows() {
        return matrix.length;
    }

    public int columns() {
        return matrix[0].length;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        row--;
        if (row < 0 || row >= rows())
            throw new InvalidRowNumberException("Invalid row number");
        return Arrays.stream(matrix[row]).max().getAsDouble();
    }

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        column--;
        if (column < 0 || column >= columns())
            throw new InvalidColumnNumberException("Invalid column number");
        ArrayList<Double> arrayList = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            arrayList.add(matrix[i][column]);
        }
        return arrayList.stream().max(Double::compare).get();
    }

    public double sum() {
        return Arrays.stream(matrix).flatMapToDouble(Arrays::stream).sum();
    }

    public double[] toSortedArray() {
        ArrayList<Double> tempList = new ArrayList<>();
        Arrays.stream(matrix).flatMapToDouble(Arrays::stream).forEach(tempList::add);
        return tempList.stream().sorted(Comparator.reverseOrder()).mapToDouble(element -> element).toArray();
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder tempStringBuilder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (j < matrix[0].length - 1)
                    tempStringBuilder.append(df.format(matrix[i][j])).append("\t");
                else
                    tempStringBuilder.append(df.format(matrix[i][j]));
            }
            if (i < matrix.length - 1)
                tempStringBuilder.append("\n");
        }
        return tempStringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        if (this.getDimensions().equals(that.getDimensions())) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[i][j] != that.matrix[i][j])
                        return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toSortedArray());
    }
}
