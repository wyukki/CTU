/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

/**
 *
 * @author semenvol, danilrom
 */
public class Field {

    private final int x_from;
    private final int y_from;
    private final int x_to;
    private final int y_to;
    private int pieceColor; // 0 - black, 1 - white
    private int fieldColor; // 0 - black, 1 - white , 2 - free field
    private String piece; // p - pawn, r - rook, n - knight, b - bishop, q - queen, k - king
    private boolean isSelected = false;
    private boolean isFree = true;
    private int index;

    Field(int x_from, int y_from, int x_to, int y_to, int pieceColor, int fieldColor, int i) {
        this.x_from = x_from;
        this.y_from = y_from;
        this.x_to = x_to;
        this.y_to = y_to;
        this.pieceColor = pieceColor;
        this.fieldColor = fieldColor;
        this.index = i;
    }

    /**
     *
     * @return piece's color
     */
    public int getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(int pieceColor) {
        this.pieceColor = pieceColor;
    }

    /**
     *
     * @return field's color
     */
    public int getFieldColor() {
        return fieldColor;
    }

    /**
     *
     * @return starting x coordinate of the field
     */
    public int getX_from() {
        return x_from;
    }

    /**
     *
     * @return starting y coordinate of the field
     */
    public int getY_from() {
        return y_from;
    }

    /**
     *
     * @return final x coordinate of the field
     */
    public int getX_to() {
        return x_to;
    }

    /**
     *
     * @return final y coordinate of the field
     */
    public int getY_to() {
        return y_to;
    }

    /**
     *
     * @return piece name from field
     */
    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    /**
     *
     * @return true if fields is free
     */
    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean b) {
        isFree = b;
    }

    /**
     *
     * @return true if piece is selected else false
     */
    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean b) {
        this.isSelected = b;
    }

    /**
     *
     * @return index of the selected piece
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
