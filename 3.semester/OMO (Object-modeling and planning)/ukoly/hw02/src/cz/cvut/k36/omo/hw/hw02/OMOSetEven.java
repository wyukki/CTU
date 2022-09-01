package cz.cvut.k36.omo.hw.hw02;

import java.util.*;

public class OMOSetEven implements OMOSetView{
    private OMOSetView setA;
    
    public OMOSetEven(OMOSetView setA) {
        this.setA = setA;
    }

    @Override
    public boolean contains(int element) {
        return (setA.contains(element) && element % 2 ==0);
    }

    @Override
    public int[] toArray() {
        Set<Integer> arr = new HashSet<Integer>();
        for (int num : setA.toArray()) {
            if (contains(num)){
                arr.add(num);
            }
        }
        return arr.stream().mapToInt(k -> k).toArray();
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
