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
public class TreeImpl implements Tree {

    private int[] tree;
    private NodeImpl root;
    private int len;
    public TreeImpl() {
    }

    @Override
    public void setTree(int[] values) {
        len = values.length;
        tree = values;
        if (len == 0) return;
        root = insert(0, len);
    }

    private NodeImpl insert(int head, int tail) {
        if (head >= tail) return null;
        int midle = (head + tail) / 2;
        NodeImpl node = new NodeImpl(tree[midle], insert(head, midle), insert(midle + 1, tail));
        return node;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public String toString() {
        if(root == null) return "";
        return "- " +root.getValue() + addToString(1, root, "\n");
    }

    private String addToString(int iter, NodeImpl cursor, String s){
        if(cursor == null) return "";
        
        String space = "";
        for (int i = 0; i < iter -1; ++i){
            space += " ";
        }
        if (iter > 1) s = space + "- " + cursor.value + "\n";
        if (cursor.left != null){
            s += addToString(iter+1, cursor.left, s);
        }
        if (cursor.right != null){
           s += addToString(iter+1, cursor.right, s);
        }
        return s;
    }
}
