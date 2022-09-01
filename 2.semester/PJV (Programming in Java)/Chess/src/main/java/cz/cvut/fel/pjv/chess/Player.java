/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

import java.util.ArrayList;

/**
 *
 * @author semenvol, danilrom
 */
public class Player {

    // 0 - black, 1 - white
    private int color = 0;
    private String playerName = null;
    private ArrayList<Piece> myPieces = new ArrayList<Piece>(16);
    private ArrayList<Piece> piecesForManualSetting = new ArrayList<Piece>(6);
    private ArrayList<Field> fieldsForManualSetting = new ArrayList<Field>(6);
    private boolean isMyMove;
    private boolean piecesAreSet = false;
    private int pawns = 0;
    private int rooks = 0;
    private int knights = 0;
    private int bishops = 0;
    private int king = 0;
    private int queen = 0;
    private boolean isFirstMove = true;
    private Client client;

    /**
     * Creates player for local game
     *
     * @param color player's color
     * @param playerName "black" or "white"
     */
    Player(int color, String playerName) {
        this.color = color;
        this.playerName = playerName;
        this.isMyMove = color == 1;
    }

    /**
     * Creates player for server game
     *
     * @param color player's color
     * @param playerName player's nickname
     * @param client client's socket
     */
    Player(int color, String playerName, Client client) {
        this.color = color;
        this.playerName = playerName;
        this.isMyMove = color == 1;
        this.client = client;
    }

    public int getColor() {
        return color;
    }

    /**
     *
     * @return true if pieces are set on the board
     */
    public boolean getPiecesAreSet() {
        return piecesAreSet;
    }

    public void setPiecesAreSet(boolean b) {
        this.piecesAreSet = b;
    }

    /**
     *
     * @return true if is my time to move
     */
    public boolean getIsMyMove() {
        return isMyMove;
    }

    public void setIsMyMove(boolean b) {
        this.isMyMove = b;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setColor(int c) {
        this.color = c;
    }

    /**
     *
     * @return list of player's pieces
     */
    public ArrayList<Piece> getMyPieces() {
        return myPieces;
    }

    /**
     *
     * @return list of player's pieces for manual setting
     */
    public ArrayList<Piece> getPiecesForManualSetting() {
        return piecesForManualSetting;
    }

    /**
     *
     * @return list of player's fields for manual setting
     */
    public ArrayList<Field> getFieldsForManualSetting() {
        return fieldsForManualSetting;
    }

    /**
     * Increments number of set piece
     *
     * @param piece piece name
     */
    public void incrementPieceCounter(String piece) {
        if (piece == null) {
            return;
        }
        switch (piece) {
            case "pw":
            case "pb":
                pawns++;
                break;
            case "rw":
            case "rb":
                rooks++;
                break;
            case "nw":
            case "nb":
                knights++;
                break;
            case "bw":
            case "bb":
                bishops++;
                break;
            case "qw":
            case "qb":
                queen++;
                break;
            case "kw":
            case "kb":
                king++;
                break;
        }
    }

    /**
     *
     * @param piece piece's name
     * @return actual number of set piece
     */
    public int getCounter(String piece) {
        int counter = 0;
        switch (piece) {
            case "pawn":
                counter = pawns;
                break;
            case "rook":
                counter = rooks;
                break;
            case "knight":
                counter = knights;
                break;
            case "bishop":
                counter = bishops;
                break;
            case "queen":
                counter = queen;
                break;
            case "king":
                counter = king;
                break;
        }
        return counter;
    }

    /**
     *
     * @return index of the king in player's pieces list
     */
    public int getKing() {
        int index = 0;
        for (Piece p : myPieces) {
            if (p == null) {
                index++;
                continue;
            }
            if (p.getName().equals("kw") || p.getName().equals("kb")) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     *
     * @param indexInFields index of piece on board
     * @return index of piece in player's pieces list
     */
    public Integer getIndexOfPiece(int indexInFields) {
        int index = 0;
        boolean found = false;
        for (Piece p : myPieces) {
            if (p == null) {
                index++;
                continue;
            }
            if (p.getIndexInFields() == indexInFields) {
                found = true;
                break;
            }
            index++;
        }
        if (!found) {
            return null;
        }
        return index;
    }

    public void setToMyPieces(Piece p, int index) {
        this.myPieces.set(index, p);
    }

    /**
     * Used to start timer
     *
     * @return true if is first move
     */
    public boolean getIsFirstMove() {
        return isFirstMove;
    }

    public void setIsFirstMove(boolean b) {
        this.isFirstMove = b;
    }

    /**
     * Sends message to server
     *
     * @param s message to be sent
     */
    public void writeMSG(String s) {
        client.writeMSG(s);
    }

    /**
     * Waits for reply
     *
     * @return recieved message
     */
    public String waitForMSG() {
        return client.waitForMSG();
    }

    /**
     *
     * @return opponent nickname
     */
    public String getOppPlayerName() {
        return client.getOppPlayerName();
    }

}
