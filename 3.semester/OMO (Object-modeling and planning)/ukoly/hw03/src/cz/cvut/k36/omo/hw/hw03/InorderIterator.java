package cz.cvut.k36.omo.hw.hw03;

import java.util.NoSuchElementException;
import java.util.Stack;

public class InorderIterator implements CustomIterator {

    private Node next;
    private Stack<Node> stack = new Stack<>();
    public InorderIterator(Node root) {
        this.next = root;
        if (root == null) {
            return;
        }
        addToStack(root);
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
        int ret = stack.pop().getContents();
        if (stack.isEmpty()) {
            next = null;
        }
        return ret;
    }

    private void addToStack(Node node) {
        if (node == null) {
            return;
        }
        addToStack(node.getRight());
        stack.push(node);
        addToStack(node.getLeft());
    }
}
