package Lab_6;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntegerListTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test standard methods
            int subtest = jin.nextInt();
            if (subtest == 0) {
                IntegerList list = new IntegerList();
                while (true) {
                    int num = jin.nextInt();
                    if (num == 0) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if (num == 1) {
                        list.remove(jin.nextInt());
                    }
                    if (num == 2) {
                        print(list);
                    }
                    if (num == 3) {
                        break;
                    }
                }
            }
            if (subtest == 1) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for (int i = 0; i < n; ++i) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if (k == 1) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if (num == 1) {
                    list.removeDuplicates();
                }
                if (num == 2) {
                    print(list.addValue(jin.nextInt()));
                }
                if (num == 3) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
        if (k == 2) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for (int i = 0; i < n; ++i) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while (true) {
                int num = jin.nextInt();
                if (num == 0) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if (num == 1) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if (num == 2) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if (num == 3) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if (num == 4) {
                    print(list);
                }
                if (num == 5) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if (il.size() == 0) System.out.print("EMPTY");
        for (int i = 0; i < il.size(); ++i) {
            if (i > 0) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }
}

class IntegerList {

    ArrayList<Integer> integerList;

    IntegerList() {
        integerList = new ArrayList<Integer>();
    }

    IntegerList(Integer[] numbers) {
        integerList = new ArrayList<>(Arrays.asList(numbers));
    }

    public void add(int el, int idx) {
        if (idx >= integerList.size()) {
            IntStream.range(integerList.size(), idx).forEach(i -> integerList.add(0));
            integerList.add(el);
        } else {
            integerList.add(idx, el);
        }
    }

    int remove(int idx) {
        if (idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();
        return integerList.remove(idx);
    }

    public void set(int el, int idx) {
        if (idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();
        integerList.set(idx, el);
    }

    public int get(int idx) {
        if (idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();
        return integerList.get(idx);
    }

    public int size() {
        return integerList.size();
    }


    public void shiftLeft(int idx, int k) {
        if (idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();

        // TODO: potential problem
        int new_position = idx - k;
        if (new_position < 0) {
            new_position = integerList.size() + new_position % integerList.size();
        }

        int element = integerList.remove(idx);
        integerList.add(new_position, element);
    }

    public void shiftRight(int idx, int k) {
        if (idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();

        // TODO: potential problem
        int new_position = (idx + k) % integerList.size();
        int element = integerList.remove(idx);
        integerList.add(new_position, element);
    }

    public int sumFirst(int k) {
        if (k > integerList.size())
            k = integerList.size();
        return integerList.stream().limit(k).mapToInt(e -> e).sum();
    }


    public int sumLast(int k) {
        if (k > integerList.size())
            k = integerList.size();
        return IntStream.range(integerList.size() - k, integerList.size()).map(idx -> integerList.get(idx)).sum();
    }

    public void removeDuplicates() {
        Collections.reverse(integerList);
        integerList = new ArrayList<>(new LinkedHashSet<>(integerList));
        Collections.reverse(integerList);
    }

    public IntegerList addValue(int value) {
        IntegerList tempList = new IntegerList();
        for (int i = 0; i < integerList.size(); i++) {
            tempList.add(integerList.get(i) + value, i);
        }
        return tempList;
    }

    public int count(int elements) {
        return (int) integerList.stream().filter(el -> el == elements).count();
    }

    @Override
    public String toString() {
        return integerList.toString();
    }
}