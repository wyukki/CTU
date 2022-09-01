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
public class Pawn extends Piece {

    private boolean isFirstMove = true;
    private boolean promotionIsValid = false;
    private boolean moveOnSecondField = false;
    private boolean isEnPassant = false;

    Pawn(final int color, int x, int y, final String name, int index) {
        super(color, x, y, name, index);
    }

    @Override
    public boolean move(ArrayList<Field> fields, int index) {
        boolean ret = false;
        if (possible_moves == null && attackMoves == null) {
            return ret;
        } else if (possible_moves == null && attackMoves.size() > 0) {
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
                isFirstMove = false;
                if (color == 0) {
                    if (move.getIndex() == this.getIndexInFields() + 16) {
                        this.moveOnSecondField = true; // for en Passant
                    }
                    if ((move.getIndex() == this.getIndexInFields() + 9
                            || move.getIndex() == this.getIndexInFields() + 7)
                            && move.getPiece() == null) {
                        isEnPassant = true;
                    }
                } else {
                    if (move.getIndex() == this.getIndexInFields() - 16) {
                        this.moveOnSecondField = true; // for enPassant
                    }
                    if ((move.getIndex() == this.getIndexInFields() - 9
                            || move.getIndex() == this.getIndexInFields() - 7)
                            && move.getPiece() == null) {
                        isEnPassant = true;
                    }
                }
                break;
            }
        }
        return ret;

    }

    @Override
    public ArrayList<Field> createPossibleMoves(ArrayList<Field> fields, int index) {
        this.possible_moves = new ArrayList<Field>();
        if (color == 0) {
            if (index + 8 < 64 && fields.get(index + 8).getIsFree()
                    && fields.get(index + 8).getPiece() == null) {
                possible_moves.add(fields.get(index + 8));
            }
            if (index + 16 < 64 && fields.get(index + 16).getIsFree()
                    && isFirstMove && fields.get(index + 8).getIsFree()) {
                possible_moves.add(fields.get(index + 16));
            }
            if (index + 7 < 64 && fields.get(index + 7).getPieceColor() == 1
                    && ((index % 8 != 0) && ((index + 7) % 8 != 7))) {
                possible_moves.add(fields.get(index + 7));
            }
            if (index + 9 < 64 && fields.get(index + 9).getPieceColor() == 1
                    && ((index % 8 != 7) && ((index + 9) % 8 != 0))) {
                possible_moves.add(fields.get(index + 9));
            }
        } else {
            if (index - 8 >= 0 && fields.get(index - 8).getIsFree()
                    && fields.get(index - 8).getPiece() == null) {
                possible_moves.add(fields.get(index - 8));
            }
            if (index - 16 >= 0 && fields.get(index - 16).getIsFree()
                    && isFirstMove && fields.get(index - 8).getIsFree()) {
                possible_moves.add(fields.get(index - 16));
            }
            if (index - 7 >= 0 && fields.get(index - 7).getPieceColor() == 0
                    && ((index % 8 != 7) && ((index - 7) % 8 != 0))) {
                possible_moves.add(fields.get(index - 7));
            }
            if (index - 9 >= 0 && fields.get(index - 9).getPieceColor() == 0
                    && ((index % 8 != 0) && ((index - 9) % 8 != 7))) {
                possible_moves.add(fields.get(index - 9));
            }
        }
        if (color == 0) {
            if (index > 31 && index < 40) { // black pawn en Passant
                Piece opp_pawn;
                if (!fields.get(index - 1).getIsFree()
                        && fields.get(index + 1).getPieceColor() == 1) {
                    opp_pawn = Board.white.getMyPieces().get(Board.white.getIndexOfPiece(index - 1));
                    if (opp_pawn == null) {
                        return possible_moves;
                    }
                    if (opp_pawn.getName().equals("pw")
                            && opp_pawn.getIndexInFields() == index - 1) {
                        if (((Pawn) opp_pawn).getMoveOnSecondField()) {
                            possible_moves.add(fields.get(index + 7));
                        }
                    }
                }
                if (!fields.get(index + 1).getIsFree()
                        && fields.get(index + 1).getPieceColor() == 1) {
                    opp_pawn = Board.white.getMyPieces().get(Board.white.getIndexOfPiece(index + 1));
                    if (opp_pawn == null) {
                        return possible_moves;
                    }
                    if (opp_pawn.getName().equals("pw")
                            && opp_pawn.getIndexInFields() == index + 1) {
                        if (((Pawn) opp_pawn).getMoveOnSecondField()) {
                            possible_moves.add(fields.get(index + 9));
                        }
                    }
                }
            }
        } else {
            if (index > 23 && index < 32) { // white pawn en Passant
                Piece opp_pawn;
                if (!fields.get(index - 1).getIsFree()
                        && fields.get(index - 1).getPieceColor() == 0) {
                    opp_pawn = Board.black.getMyPieces().get(Board.black.getIndexOfPiece(index - 1));
                    if (opp_pawn == null) {
                        return possible_moves;
                    }
                    if (opp_pawn.getName().equals("pb")
                            && opp_pawn.getIndexInFields() == index - 1) {
                        if (((Pawn) opp_pawn).getMoveOnSecondField()) {
                            possible_moves.add(fields.get(index - 9));
                        }
                    }
                }
                if (!fields.get(index + 1).getIsFree()
                        && fields.get(index + 1).getPieceColor() == 0) {
                    opp_pawn = Board.black.getMyPieces().get(Board.black.getIndexOfPiece(index + 1));
                    if (opp_pawn == null) {
                        return possible_moves;
                    }

                    if (opp_pawn.getName().equals("pb")
                            && opp_pawn.getIndexInFields() == index + 1) {
                        if (((Pawn) opp_pawn).getMoveOnSecondField()) {
                            possible_moves.add(fields.get(index - 7));
                        }
                    }
                }
            }
        }
        return possible_moves;
    }

    /**
     * Creates attack moves for king
     *
     * @param fields actual state of the board
     * @param index index of the selected piece on the board
     * @return list of attack moves for king
     */
    public ArrayList<Field> createAttackMovesForKing(ArrayList<Field> fields, int index) {
        this.attackMoves = new ArrayList<Field>();
        if (color == 0) {
            if (index + 7 < 64
                    && ((index % 8 != 0) && ((index + 7) % 8 != 7))) {
                attackMoves.add(fields.get(index + 7));
            }
            if (index + 9 < 64
                    && ((index % 8 != 7) && ((index + 9) % 8 != 0))) {
                attackMoves.add(fields.get(index + 9));
            }
        } else {
            if (index - 7 >= 0
                    && ((index % 8 != 7) && ((index - 7) % 8 != 0))) {
                attackMoves.add(fields.get(index - 7));
            }
            if (index - 9 >= 0
                    && ((index % 8 != 0) && ((index - 9) % 8 != 7))) {
                attackMoves.add(fields.get(index - 9));
            }
        }
        return attackMoves;
    }

    @Override
    protected ArrayList<Field> createAttackMoves(ArrayList<Field> fields, int index) {
        this.attackMoves = new ArrayList<Field>();
        if (color == 0) {
            if (index + 7 < 64 && fields.get(index + 7).getPieceColor() == 1
                    && ((index % 8 != 0) && ((index + 7) % 8 != 7))) {
                attackMoves.add(fields.get(index + 7));
            }
            if (index + 9 < 64 && fields.get(index + 9).getPieceColor() == 1
                    && ((index % 8 != 7) && ((index + 9) % 8 != 0))) {
                attackMoves.add(fields.get(index + 9));
            }
        } else {
            if (index - 7 >= 0 && fields.get(index - 7).getPieceColor() == 0
                    && ((index % 8 != 7) && ((index - 7) % 8 != 0))) {
                attackMoves.add(fields.get(index - 7));
            }
            if (index - 9 >= 0 && fields.get(index - 9).getPieceColor() == 0
                    && ((index % 8 != 0) && ((index - 9) % 8 != 7))) {
                attackMoves.add(fields.get(index - 9));
            }
        }
        return attackMoves;
    }

    /**
     *
     * @return true if promotion can be done else false
     */
    public boolean promotion() {
        if (color == 0) {
            for (int i = 56; i < 64; ++i) {
                if (this.getIndexInFields() == i) {
                    promotionIsValid = true;
                }
            }
        } else {
            for (int i = 0; i < 8; ++i) {
                if (this.getIndexInFields() == i) {
                    promotionIsValid = true;
                }
            }
        }
        return promotionIsValid;
    }

    /**
     *
     * @return true if pawn moved two fields forward, else false
     */
    public boolean getMoveOnSecondField() {
        return this.moveOnSecondField;
    }

    public void setMoveOnSecondField(boolean b) {
        this.moveOnSecondField = b;
    }
    /**
     * 
     * @return true if enPassant was done
     */
    public boolean getIsEnPassant() {
        return this.isEnPassant;
    }

    public void setIsEnPassant() {
        this.isEnPassant = false;
    }
}
