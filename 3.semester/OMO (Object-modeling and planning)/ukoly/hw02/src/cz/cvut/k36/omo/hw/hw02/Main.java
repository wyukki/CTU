package cz.cvut.k36.omo.hw.hw02;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[10];
        for (int i = 0; i < 10; ++i) {
            arr[i] = i;
        }
        OMOSet A = new OMOSet(arr);
        OMOSetView B = A.copy();
        System.out.println(A.print());
        A.remove(2);
        System.out.println(A.print());
        OMOSetUnion union = new OMOSetUnion(A, B);
    }
}
