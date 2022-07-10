package cz.cvut.k36.omo.hw.hw03;

import java.util.NoSuchElementException;
import java.util.Stack;

public class PostorderIterator implements CustomIterator {

    private Node next;
    private Stack<Node> stack = new Stack<>();
    public PostorderIterator(Node root) {
        this.next = root;
        addToStack(root);
        Stack<Node> tmp = new Stack<>();
        int len = stack.size();
        for (int i = 0; i < len; ++i) {
            tmp.push(stack.pop());
        }
        stack = tmp;
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

    private void addToStack(Node node){
        if (node == null) {
            return;
        }
        addToStack(node.getLeft());
        addToStack(node.getRight());
        stack.push(node);
    }
}
