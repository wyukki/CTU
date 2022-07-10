
package cz.cvut.fel.omo;

public class Homework1 {
    private int hCalls = 0;
    private static int iCalls = 0;
    public boolean f() {
        return true;
    }
    public static boolean g(){ 
        return false;
    }
    public int h(){
        return ++hCalls;
    }
    public int i(){
        return ++iCalls;
    }
}
