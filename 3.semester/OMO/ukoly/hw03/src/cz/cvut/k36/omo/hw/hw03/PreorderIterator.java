package cz.cvut.k36.omo.hw.hw03;

import java.util.NoSuchElementException;

public class PreorderIterator implements CustomIterator {
    private Node next;
    private Node root;
    public PreorderIterator(Node root) {
        this.root = root;
        this.next = root;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public int next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int ret = next.getContents();
        next.setVisited(true);
        if (next.getLeft() != null) {
            next = next.getLeft();
        } else if (next.getRight() != null) {
            next = next.getRight();
        } else {
            while (next.isVisited()) {
                next = next.getParent();
                if (next.getLeft() != null && !next.getLeft().isVisited()) {
                    next = next.getLeft();
                    break;
                } else if (next.getRight() != null && !next.getRight().isVisited()) {
                    next = next.getRight();
                    break;
                }
                if (next == root) {
                    next = null;
                    break;
                }
            }
        }
        return ret;
    }
}
