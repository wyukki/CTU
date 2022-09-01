package cz.cvut.k36.omo.hw.hw02;

import java.util.*;

public class OMOSet extends OMOSetBase implements OMOSetView {
    Set<Integer> numbers;
    private int length;

    public OMOSet() {
        numbers = new HashSet<Integer>();
    }

    public boolean contains(int element) {
        return numbers.contains(element);
    }

    public int[] toArray() {
        int[] arr = new int[numbers.size()];
        int i = 0;
        for (Integer integer : numbers) {
            arr[i] = integer;
            i++;
        }
        return arr;
    }

    public OMOSetView copy() {
        OMOSet newSet = new OMOSet();
        for (int i : toArray()){
            newSet.add(i);
        }
        return newSet;
    }

    public void add(int element) {
        this.numbers.add(element);
        this.length++;
    }

    public void remove(int element) {
        this.numbers.remove(element);
        this.length--;
    }

    public String print(){
        StringBuilder s = new StringBuilder();
        numbers.forEach(s::append);
        return s.toString();
    }

    public int getLength() {
        return length;
    }
}
