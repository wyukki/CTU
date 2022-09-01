package cz.cvut.k36.omo.hw.hw02;

import java.util.HashSet;

public class OMOSetComplement implements OMOSetView{

    private OMOSetView setA;
    private OMOSetView setB;

    public OMOSetComplement(OMOSetView setA, OMOSetView setB){
        this.setA = setA;
        this.setB = setB;
    }

    @Override
    public boolean contains(int element) {
        return (setA.contains(element) && !setB.contains(element));
    }

    @Override
    public int[] toArray() {
        HashSet<Integer> arr = new HashSet<>();
        for (int num : setA.toArray()){
            if (contains(num)) {
                arr.add(num);
            }
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
