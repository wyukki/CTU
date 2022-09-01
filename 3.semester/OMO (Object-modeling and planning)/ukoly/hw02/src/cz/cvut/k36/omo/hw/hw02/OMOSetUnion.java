package cz.cvut.k36.omo.hw.hw02;

import java.util.HashSet;

public class OMOSetUnion implements OMOSetView {
    private OMOSetView setA;
    private OMOSetView setB;

    public OMOSetUnion(OMOSetView setA, OMOSetView setB) {
        this.setA = setA;
        this.setB = setB;
    }

    @Override
    public boolean contains(int element) {
        return (setA.contains(element) || setB.contains(element));
    }

    @Override
    public int[] toArray() {
        HashSet<Integer> arr = new HashSet<>();
        for (int a: setA.toArray()) {
            arr.add(a);
        }
        for (int b : setB.toArray()){
            arr.add(b);
        }
        return arr.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public OMOSetView copy() {
        OMOSet newSet = new OMOSet();
        for (int i : toArray()) {
            newSet.add(i);
        }
        return newSet;
    }
}
