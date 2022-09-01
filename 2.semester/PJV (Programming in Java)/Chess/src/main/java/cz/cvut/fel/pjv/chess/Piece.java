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
public abstract class Piece {

    public final int color;
    protected int x;
    protected int y;
    private final String name;
    private int indexInFields;
    protected ArrayList<Field> possible_moves = new ArrayList<Field>();
    protected ArrayList<Field> attackMoves = new ArrayList<Field>();

    Piece(final int color, int x, int y, final String name, int index) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.name = name;
        this.indexInFields = index;
    }

    /**
     * Creates possible moves for selected piece
     *
     * @param fields actual state of the board
     * @param index index of the selected piece on the board
     * @return list of possible moves
     */
    public abstract ArrayList<Field> createPossibleMoves(ArrayList<Field> fields, int index);

    /**
     * Creates attack moves for selected piece
     *
     * @param fields actual state of the board
     * @param index index of the selected piece on the board
     * @return list of attack moves
     */
    protected abstract ArrayList<Field> createAttackMoves(ArrayList<Field> fields, int index);

    /**
     * Checks if move to desired place is possible
     *
     * @param fields actual state of the board
     * @param index index of the desired move
     * @return true if move is possible else false
     */
    protected boolean move(ArrayList<Field> fields, int index) {
        boolean ret = false;
        if (possible_moves == null && attackMoves == null) {
            return ret;
        } else if (possible_moves == null && attackMoves != null) {
            if (!copyAttackMoves(fields, index)) {
                return ret;
            }
        } else if (possible_moves == null) {
            return ret;
        }
        Field wantedMove = fields.get(index);
        int xTo = wantedMove.getX_from();
        int yTo = wantedMove.getY_from();
        for (Field move : possible_moves) {
            if (move.getX_from() == xTo && move.getY_from() == yTo) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    /**
     * Checks how far piece can be moved (e.g. rook can't jump over pawn) Used
     * for queen, rook and bishop
     *
     * @param fields actual state of the board
     * @param indexFrom index of the selected piece on the board
     * @param dir direction of the move
     * @param squareCount quantity of fields till the board bound
     * @return true if move is not blocked by another piece else false
     */
    protected boolean previousFieldIsFree(ArrayList<Field> fields, int indexFrom, int dir, int squareCount) {
        boolean ret = true;
        for (int i = 1; i < squareCount; ++i) {
            if (!ret) {
                break;
            }
            if (!fields.get(indexFrom + dir).getIsFree() || fields.get(indexFrom + dir).getPiece() != null) {
                ret = false;
            }
            indexFrom += dir;
        }
        return ret;
    }

    /**
     * Clears possible and attack moves lists
     */
    public void clearPossibleMoves() {
        this.possible_moves = null;
        this.attackMoves = null;
    }

    public ArrayList<Field> getAttackMoves() {
        return attackMoves;
    }

    /**
     *
     * @return index of King on the board
     */
    protected int getKing() {
        int index = 0;
        for (Field f : Board.fields) {
            if (f.getPiece() == null) {
                index++;
                continue;
            }
            if (color == 0) {
                if (f.getPiece().equals("kb")) {
                    break;
                }
            } else {
                if (f.getPiece().equals("kw")) {
                    break;
                }
            }
            index++;
        }
        return index;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     *
     * @return index of piece on the board
     */
    public int getIndexInFields() {
        return indexInFields;
    }

    public void setIndexInFields(int indexInFields) {
        this.indexInFields = indexInFields;
    }

    /**
     * Copies attack moves list to possible moves list, if possible moves list
     * is free
     *
     * @param fields actual state of the board
     * @param index index of the selected piece on the board
     * @return true if attack moves list is not empty else false
     */
    protected boolean copyAttackMoves(ArrayList<Field> fields, int index) {
        if (attackMoves == null) {
            return false;
        }
        possible_moves = new ArrayList<Field>(attackMoves.size());
        possible_moves.addAll(attackMoves);
        return true;
    }

    /**
     * Clears possible moves list, if one of the moves leads to check or mate
     *
     * @param fields actual state of the board
     * @param index index of the selected piece on the board
     * @return null if one of the moves leads to check or mate, else possible
     * moves list
     */
    protected ArrayList<Field> checkPossibleMoves(ArrayList<Field> fields, int index) {
        if (possible_moves == null) {
            return null;
        }
        ArrayList<Piece> opp_pieces;
        String piece;
        piece = fields.get(index).getPiece();
        fields.get(index).setPiece(null);
        fields.get(index).setIsFree(true);
        boolean moveIsDangerous = false;
        Player player;
        if (Board.state == States.Server) {
            player = Board.sessionPlayer;
            if (Board.sessionPlayer.getColor() == 0) {
                opp_pieces = Board.white.getMyPieces();
            } else {
                opp_pieces = Board.black.getMyPieces();
            }
        } else {
            player = Board.white.getIsMyMove() ? Board.white : Board.black;
            opp_pieces = color == 0 ? Board.white.getMyPieces() : Board.black.getMyPieces();
        }
        Piece king = player.getMyPieces().get(player.getKing());
        for (Piece p : opp_pieces) {
            if (((King) king).isCheck(fields)) {
                possible_moves = null;
                break;
            }
            if (moveIsDangerous) {
                break;
            }
            if (p == null || p.getName().equals("kw") || p.getName().equals("kb")
                    || p.getName().equals("pw") || p.getName().equals("pb")) {
                continue;
            } else {
                for (Field oppPossMove : p.createPossibleMoves(fields, p.getIndexInFields())) {
                    if (oppPossMove == null) {
                        continue;
                    }
                    if (oppPossMove.getIndex() == getKing()) {
                        if (!saveKing()) {
                            possible_moves = null;
                        }
                        moveIsDangerous = true;
                        break;
                    }
                }
            }
        }
        fields.get(index).setPiece(piece);
        fields.get(index).setIsFree(false);
        if (possible_moves == null) {
            if (color == 1) {
                king = Board.white.getMyPieces().get(Board.white.getIndexOfPiece(getKing()));
                if (((King) king).isCheck(fields)) {
                    if (!saveKing()) {
                        this.possible_moves = null;
                    }
                }
            } else {
                king = Board.black.getMyPieces().get(Board.black.getIndexOfPiece(getKing()));
                if (((King) king).isCheck(fields)) {
                    if (!saveKing()) {
                        this.possible_moves = null;
                    }
                }
            }
        }
        return this.possible_moves;
    }

    /**
     * Clears attack moves list, if one of the moves leads to check or mate
     *
     * @param fields actual state of the board
     * @param index index of the selected piece on the board
     * @return null if one of the moves leads to check or mate, else attack
     * moves list
     */
    protected ArrayList<Field> checkAttackMoves(ArrayList<Field> fields, int index) {
        if (attackMoves == null || attackMoves.isEmpty()) {
            return null;
        }
        ArrayList<Piece> opp_pieces;
        opp_pieces = color == 0 ? Board.white.getMyPieces() : Board.black.getMyPieces();
        String oppPiece;
        String myPiece = this.getName();
        int i = attackMoves.get(0).getIndex();
        oppPiece = fields.get(i).getPiece();
        fields.get(index).setPiece(null);
        fields.get(index).setIsFree(true);

        //pretend that on oppPiece position is myPiece
        fields.get(i).setPiece(myPiece);
        fields.get(i).setIsFree(false);

        boolean moveIsDangerous = false;
        for (Piece p : opp_pieces) {
            if (moveIsDangerous) {
                break;
            }
            if (p == null || p.getName().equals("kw") || p.getName().equals("kb")
                    || p.getName().equals("pw") || p.getName().equals("pb") || p.getIndexInFields() == i) {
                continue;
            } else {
                for (Field oppPossMove : p.createPossibleMoves(fields, p.getIndexInFields())) {
                    if (oppPossMove == null) {
                        continue;
                    }
                    if (oppPossMove.getIndex() == getKing()) {
                        attackMoves = null;
                        moveIsDangerous = true;
                        break;
                    }
                }
            }
        }
        fields.get(index).setPiece(myPiece);
        fields.get(index).setIsFree(false);
        fields.get(i).setPiece(oppPiece);
        return this.attackMoves;
    }

    /**
     * If king is under check function checks if there is a piece that can save
     * the king (to serve like an obstacle or kill opp's piece)
     *
     * @return true if king can be saved else false
     */
    public boolean saveKing() {
        boolean canBeSaved = false;
        ArrayList<Field> fields = Board.fields;
        ArrayList<Piece> myPieces;
        ArrayList<Piece> oppPieces;
        Player player;
        Player oppPlayer;

        if (color == 0) {
            myPieces = Board.black.getMyPieces();
            oppPieces = Board.white.getMyPieces();
            player = Board.black;
            oppPlayer = Board.white;

        } else {
            myPieces = Board.white.getMyPieces();
            oppPieces = Board.black.getMyPieces();
            player = Board.white;
            oppPlayer = Board.black;
        }
        Piece king;

        king = myPieces.get(player.getIndexOfPiece(getKing()));
        for (Piece p : myPieces) {
            if (p == null || p.getName().equals("kw") || p.getName().equals("kb")) {
                continue;
            }
            ArrayList<Field> tmp = new ArrayList<Field>();
            for (Field move : p.createPossibleMoves(fields, p.getIndexInFields())) {
                if (move == null) {
                    continue;
                }
                Piece oppPiece = null;
                int checkByPiece = 0;
                String piece = fields.get(move.getIndex()).getPiece();
                fields.get(move.getIndex()).setPiece(p.getName());
                fields.get(move.getIndex()).setIsFree(false);

                if (move.getIndex() == ((King) king).getCheckByPiece()
                        && move.getPieceColor() == (color + 1) % 2) {
                    checkByPiece = oppPlayer.getIndexOfPiece(((King) king).getCheckByPiece());
                    oppPiece = oppPieces.get(checkByPiece);
                    oppPlayer.setToMyPieces(null, checkByPiece);
                }
                if (!((King) king).isCheck(fields)) {
                    if (oppPiece != null) {
                        fields.get(p.getIndexInFields()).setIsFree(true);
                        fields.get(p.getIndexInFields()).setPiece(null);
                        fields.get(p.getIndexInFields()).setPieceColor(2);
                        if (!((King) king).isCheck(fields)) {
                            canBeSaved = true;
                            tmp.add(move);
                        }
                        fields.get(p.getIndexInFields()).setIsFree(false);
                        fields.get(p.getIndexInFields()).setPiece(p.getName());
                        fields.get(p.getIndexInFields()).setPieceColor(color);
                    } else {
                        canBeSaved = true;
                        tmp.add(move);
                    }
                }
                fields.get(move.getIndex()).setPiece(piece);
                fields.get(move.getIndex()).setIsFree(true);
                if (oppPiece != null) {
                    oppPlayer.setToMyPieces(oppPiece, checkByPiece);
                }
            }
            if (!tmp.isEmpty()) {
                p.possible_moves = new ArrayList<Field>();
                p.possible_moves.addAll(tmp);
            } else {
                p.possible_moves = null;
            }
        }
        return canBeSaved;
    }

}
