/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv;

/**
 *
 * @author Uzver
 */
public class NodeImpl implements Node {

    protected NodeImpl left;
    protected NodeImpl right;
    protected int value;

    public NodeImpl(int value, NodeImpl left, NodeImpl right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public Node getLeft() {
        return left;
    }
    @Override
    public Node getRight() {
        return right;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
