package Lab_5;

import java.util.*;
import java.util.stream.IntStream;

public class IntegerListTest {

    public static void main(String[] args) {

        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
}

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }
}

class IntegerList {
    List<Integer> integerList;

    public IntegerList() {
        this.integerList = new ArrayList<>();
    }

    IntegerList(Integer[] numbers) {
        this();
        Collections.addAll(integerList, numbers);
    }

    public void add(int el, int idx) {
        if (idx > integerList.size()) {
            ArrayList<Integer> tempList = new ArrayList<>();
            IntStream.range(0, idx - integerList.size()).forEach(i -> tempList.add(0));
            integerList.addAll(tempList);
            integerList.add(el);
        } else if (idx >= 0) {
            integerList.add(idx, el);
        }
    }

    public int remove(int idx) {
        if(idx < 0 || idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();
        return integerList.remove(idx);
    }

    public void set(int el, int idx) {
        if(idx < 0 || idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();
        integerList.set(idx,el);
    }

    public int get(int idx) {
        if(idx < 0 || idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();
        return integerList.get(idx);
    }

    public int size() {
        return integerList.size();
    }

    public int count(int el) {
        return (int) integerList.stream().filter(elem -> elem.equals(el)).count();
    }

    public void removeDuplicates() {
        Collections.reverse(integerList);
        integerList = new ArrayList<Integer>(new LinkedHashSet<Integer>(integerList));
        Collections.reverse(integerList);
    }

    public int sumFirst(int k) {
        if(k<0)
            throw new ArrayIndexOutOfBoundsException();
        if(k > integerList.size())
            k = integerList.size();

        return IntStream.range(0,k)
                .map(index -> integerList.get(index))
                .sum();
    }

    public int sumLast(int k) {
        if(k<0 || k>integerList.size())
            throw new ArrayIndexOutOfBoundsException();

        return IntStream.range(integerList.size() - k, integerList.size())
                .map(index -> integerList.get(index))
                .sum();
    }

    public void shiftRight(int idx, int k) {
        if(idx < 0 || idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();

        int new_index = (idx + k) % integerList.size();
        Integer element_to_add = integerList.remove(idx);
        integerList.add(new_index,element_to_add);
    }

    public void shiftLeft(int idx, int k) {
        if(idx < 0 || idx >= integerList.size())
            throw new ArrayIndexOutOfBoundsException();

        int new_index;

        if(idx - k < 0)
            new_index = integerList.size() - Math.abs(idx-k) % integerList.size();
        else
            new_index = Math.abs(idx - k) % integerList.size();

        Integer element_to_add = integerList.remove(idx);
        integerList.add(new_index,element_to_add);
    }

    public IntegerList addValue(int val){

        Integer[] elements = new Integer[integerList.size()];
        for(int i=0;i< integerList.size();i++)
            elements[i] = integerList.get(i) + val;
        return new IntegerList(elements);
    }
}