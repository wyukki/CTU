package cz.cvut.k36.omo.hw.hw02;

abstract class OMOSetBase implements OMOSetView {
    public abstract void add(int element); //přidá prvek "element" do množiny
    public abstract void remove(int element); //odebere prvek "element" z množiny
}
