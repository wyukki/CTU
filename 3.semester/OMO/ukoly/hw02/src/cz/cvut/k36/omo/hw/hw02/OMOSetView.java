package cz.cvut.k36.omo.hw.hw02;

interface OMOSetView {
    boolean contains(int element); // testuje na přítomnost prvku v množině

    int[] toArray(); //vrátí kopii prvků množiny v poli (na pořadí prvků nezáleží)

    OMOSetView copy(); //vrátí kopii množiny
}
