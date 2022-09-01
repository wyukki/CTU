/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author semenvol, danilrom
 */
public class King extends Piece {

    private boolean check = false;
    private boolean firstMove = true;
    private char castlingSide = '\0';
    private boolean castlingMade = false;
    private int checkByPiece;

    King(final int color, int x, int y, final String name, int index) {
        super(color, x, y, name, index);
    }

    @Override
    public ArrayList<Field> createPossibleMoves(ArrayList<Field> fields, int index) {
        this.possible_moves = new ArrayList<Field>();
        if (!check) {
            if (index + 9 < 64 && (fields.get(index + 9).getIsFree()
                    || fields.get(index + 9).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 7 && ((index + 9) % 8) != 0) {
                possible_moves.add(fields.get(index + 9));
            }
            if (index + 8 < 64 && (fields.get(index + 8).getIsFree()
                    || fields.get(index + 8).getPieceColor() == (color + 1) % 2)) {
                possible_moves.add(fields.get(index + 8));
            }
            if (index + 7 < 64 && (fields.get(index + 7).getIsFree()
                    || fields.get(index + 7).getPieceColor() == (color + 1) % 2)
                    && ((index % 8) != 0) && ((index + 7) % 8) != 7) {
                possible_moves.add(fields.get(index + 7));
            }
            if (index + 1 < 64 && (fields.get(index + 1).getIsFree()
                    || fields.get(index + 1).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 7 && ((index + 1) % 8) != 0) {
                possible_moves.add(fields.get(index + 1));
            }
            if (index - 1 >= 0 && (fields.get(index - 1).getIsFree()
                    || fields.get(index - 1).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 0 && ((index - 1) % 8) != 7) {
                possible_moves.add(fields.get(index - 1));
            }
            if (index - 7 >= 0 && (fields.get(index - 7).getIsFree()
                    || fields.get(index - 7).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 7 && ((index - 7) % 8) != 0) {
                possible_moves.add(fields.get(index - 7));
            }
            if (index - 8 >= 0 && (fields.get(index - 8).getIsFree()
                    || fields.get(index - 8).getPieceColor() == (color + 1) % 2)) {
                possible_moves.add(fields.get(index - 8));
            }
            if (index - 9 >= 0 && (fields.get(index - 9).getIsFree()
                    || fields.get(index - 9).getPieceColor() == (color + 1) % 2)
                    && ((index % 8) != 0) && ((index - 9) % 8) != 7) {
                possible_moves.add(fields.get(index - 9));
            }
            if (possible_moves == null) {
                return null;
            }
        } else {
            int dir = getDir(this.getIndexInFields() - checkByPiece);
            if (index + 9 < 64 && (fields.get(index + 9).getIsFree()
                    || fields.get(index + 9).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 7 && ((index + 9) % 8) != 0 && dir != 9) {
                possible_moves.add(fields.get(index + 9));
            }
            if (index + 8 < 64 && (fields.get(index + 8).getIsFree()
                    || fields.get(index + 8).getPieceColor() == (color + 1) % 2) && dir != 8) {
                possible_moves.add(fields.get(index + 8));
            }
            if (index + 7 < 64 && (fields.get(index + 7).getIsFree()
                    || fields.get(index + 7).getPieceColor() == (color + 1) % 2)
                    && ((index % 8) != 0) && ((index + 7) % 8) != 7 && dir != 7) {
                possible_moves.add(fields.get(index + 7));
            }
            if (index + 1 < 64 && (fields.get(index + 1).getIsFree()
                    || fields.get(index + 1).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 7 && ((index + 1) % 8) != 0 && dir != 1) {
                possible_moves.add(fields.get(index + 1));
            }
            if (index - 1 >= 0 && (fields.get(index - 1).getIsFree()
                    || fields.get(index - 1).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 0 && ((index - 1) % 8) != 7 && dir != -1) {
                possible_moves.add(fields.get(index - 1));
            }
            if (index - 7 >= 0 && (fields.get(index - 7).getIsFree()
                    || fields.get(index - 7).getPieceColor() == (color + 1) % 2)
                    && ((index % 8)) != 7 && ((index - 7) % 8) != 0 && dir != -7) {
                possible_moves.add(fields.get(index - 7));
            }
            if (index - 8 >= 0 && (fields.get(index - 8).getIsFree()
                    || fields.get(index - 8).getPieceColor() == (color + 1) % 2) && dir != -8) {
                possible_moves.add(fields.get(index - 8));
            }
            if (index - 9 >= 0 && (fields.get(index - 9).getIsFree()
                    || fields.get(index - 9).getPieceColor() == (color + 1) % 2)
                    && ((index % 8) != 0) && ((index - 9) % 8) != 7 && dir != -9) {
                possible_moves.add(fields.get(index - 9));
            }
            if (possible_moves == null) {
                return null;
            }
        }
        ArrayList<Piece> opp_pieces = color == 0 ? Board.white.getMyPieces() : Board.black.getMyPieces();
        Iterator<Field> i = possible_moves.iterator();
        while (i.hasNext()) {
            Field kingMove = i.next();
            boolean moveIsDangerous = false;
            for (Piece p : opp_pieces) {
                if (moveIsDangerous) {
                    break;
                }
                if (p == null || p.getName().equals("kw") || p.getName().equals("kb")) {
                    continue;
                }
                if (p.getName().equals("pw") || p.getName().equals("pb")) {
                    for (Field oppPossMove : ((Pawn) p).createAttackMovesForKing(fields, p.getIndexInFields())) {
                        if (oppPossMove.getIndex() == kingMove.getIndex()) {
                            i.remove();
                            moveIsDangerous = true;
                            break;
                        }
                    }
                } else {
                    for (Field oppPossMove : p.createPossibleMoves(fields, p.getIndexInFields())) {
                        if (oppPossMove.getIndex() == kingMove.getIndex()) {
                            i.remove();
                            moveIsDangerous = true;
                            break;
                        }
                    }
                }
            }
        }
        return possible_moves;
    }

    /**
     *
     * @return true if king is under check else false
     */
    public boolean getIsCheck() {
        return check;
    }

    /**
     * Checks if king is under check
     *
     * @param fields actual state of the board
     * @return true king is under check else false
     */
    public boolean isCheck(ArrayList<Field> fields) {
        check = false;
        ArrayList<Piece> opp_pieces = color == 0 ? Board.white.getMyPieces() : Board.black.getMyPieces();
        for (Piece p : opp_pieces) {
            if (check) {
                break;
            }
            if (p == null || p.getName().equals("kw") || p.getName().equals("kb")) {
                continue;
            }
            for (Field move : p.createAttackMoves(fields, p.getIndexInFields())) {
                if (move == null) {
                    continue;
                }
                if (move.getIndex() == getKing()) {
                    check = true;
                    checkByPiece = p.getIndexInFields();
                    break;
                }
            }
        }
        return check;
    }

    /**
     *
     * @return true if king is under checkmate else false
     */
    public boolean isCheckMate() {
        if (!check) {
            return false;
        }
        return check && !saveKing() && (createPossibleMoves(Board.fields, getKing()).isEmpty()
                || createAttackMoves(Board.fields, getKing()).isEmpty());
    }

    @Override
    protected ArrayList<Field> createAttackMoves(ArrayList<Field> fields, int index) {
        this.attackMoves = new ArrayList<>();
        if (index + 9 < 64 && (fields.get(index + 9).getPieceColor() == (color + 1) % 2)
                && ((index % 8)) != 7 && ((index + 9) % 8) != 0) {
            attackMoves.add(fields.get(index + 9));
        }
        if (index + 8 < 64 && (fields.get(index + 8).getPieceColor() == (color + 1) % 2)) {
            attackMoves.add(fields.get(index + 8));
        }
        if (index + 7 < 64 && (fields.get(index + 7).getPieceColor() == (color + 1) % 2)
                && ((index % 8) != 0) && ((index + 7) % 8) != 7) {
            attackMoves.add(fields.get(index + 7));
        }
        if (index + 1 < 64 && (fields.get(index + 1).getPieceColor() == (color + 1) % 2)
                && ((index % 8)) != 7 && ((index + 1) % 8) != 0) {
            attackMoves.add(fields.get(index + 1));
        }
        if (index - 1 >= 0 && (fields.get(index - 1).getPieceColor() == (color + 1) % 2)
                && ((index % 8)) != 0 && ((index - 1) % 8) != 7) {
            attackMoves.add(fields.get(index - 1));
        }
        if (index - 7 >= 0 && (fields.get(index - 7).getPieceColor() == (color + 1) % 2)
                && ((index % 8)) != 7 && ((index - 7) % 8) != 0) {
            attackMoves.add(fields.get(index - 7));
        }
        if (index - 8 >= 0 && (fields.get(index - 8).getPieceColor() == (color + 1) % 2)) {
            attackMoves.add(fields.get(index - 8));
        }
        if (index - 9 >= 0 && (fields.get(index - 9).getPieceColor() == (color + 1) % 2)
                && ((index % 8) != 0) && ((index - 9) % 8) != 7) {
            attackMoves.add(fields.get(index - 9));
        }
        return attackMoves;
    }

    /**
     * Checks if castling is possible
     *
     * @param rook selected rook
     * @return true if yes else false
     */
    public boolean castling(Rook rook) {
        if (castlingMade || !rook.getIsFirstMove() || !firstMove) {
            return false;
        }
        boolean castlingIsValid = true;
        ArrayList<Field> fields = Board.fields;
        if (color == 0) {
            if (this.firstMove && (rook.getIndexInFields() == 0) && rook.getIsFirstMove()) {
                for (int i = 1; i < 4; ++i) {
                    if (!fields.get(i).getIsFree()) {
                        castlingIsValid = false;
                        break;
                    }
                }
                if (castlingIsValid) {
                    Board.fields.get(4).setPiece(null);
                    Board.fields.get(4).setIsFree(true);
                    Board.fields.get(2).setPiece("kb");
                    Board.fields.get(2).setIsFree(false);
                    Board.fields.get(2).setPieceColor(color);
                    castlingIsValid = isCheck(fields) ^ true;
                    Board.fields.get(4).setPiece("kb");
                    Board.fields.get(4).setIsFree(false);
                    Board.fields.get(2).setPiece(null);
                    Board.fields.get(2).setIsFree(true);
                    Board.fields.get(2).setPieceColor(2);
                    if (castlingIsValid) {
                        Board.fields.get(4).setPiece(null);
                        Board.fields.get(4).setIsFree(true);
                        Board.fields.get(3).setPiece("kb");
                        Board.fields.get(3).setIsFree(false);
                        Board.fields.get(3).setPieceColor(color);
                        castlingIsValid = isCheck(fields) ^ true;
                        Board.fields.get(4).setPiece("kb");
                        Board.fields.get(4).setIsFree(false);
                        Board.fields.get(3).setPiece(null);
                        Board.fields.get(3).setIsFree(true);
                        Board.fields.get(3).setPieceColor(2);
                    } else {
                        return castlingIsValid;
                    }
                    if (castlingIsValid) {
                        castlingSide = 'q';
                    }
                }
            } else if (this.firstMove && (rook.getIndexInFields() == 7) && rook.getIsFirstMove()) {
                for (int i = 5; i < 7; ++i) {
                    if (!fields.get(i).getIsFree()) {
                        castlingIsValid = false;
                        break;
                    }
                }
                if (castlingIsValid) {
                    Board.fields.get(4).setPiece(null);
                    Board.fields.get(4).setIsFree(true);
                    Board.fields.get(5).setPiece("kb");
                    Board.fields.get(5).setIsFree(false);
                    Board.fields.get(5).setPieceColor(color);
                    castlingIsValid = isCheck(fields) ^ true;
                    Board.fields.get(4).setPiece("kb");
                    Board.fields.get(4).setIsFree(false);
                    Board.fields.get(5).setPiece(null);
                    Board.fields.get(5).setIsFree(true);
                    Board.fields.get(5).setPieceColor(2);
                    if (castlingIsValid) {
                        Board.fields.get(4).setPiece(null);
                        Board.fields.get(4).setIsFree(true);
                        Board.fields.get(6).setPiece("kb");
                        Board.fields.get(6).setIsFree(false);
                        Board.fields.get(6).setPieceColor(color);
                        castlingIsValid = isCheck(fields) ^ true;
                        Board.fields.get(4).setPiece("kb");
                        Board.fields.get(4).setIsFree(false);
                        Board.fields.get(6).setPiece(null);
                        Board.fields.get(6).setIsFree(true);
                        Board.fields.get(6).setPieceColor(2);
                    } else {
                        return castlingIsValid;
                    }
                    if (castlingIsValid) {
                        castlingSide = 'k';
                    }
                }
            }
        } else {
            if (this.firstMove && (rook.getIndexInFields() == 56) && rook.getIsFirstMove()) {
                for (int i = 57; i < 60; ++i) {
                    if (!fields.get(i).getIsFree()) {
                        castlingIsValid = false;
                        break;
                    }
                }
                if (castlingIsValid) {
                    Board.fields.get(60).setPiece(null);
                    Board.fields.get(60).setIsFree(true);
                    Board.fields.get(58).setPiece("kw");
                    Board.fields.get(58).setIsFree(false);
                    Board.fields.get(58).setPieceColor(color);
                    castlingIsValid = isCheck(fields) ^ true;
                    Board.fields.get(60).setPiece("kw");
                    Board.fields.get(60).setIsFree(false);
                    Board.fields.get(58).setPiece(null);
                    Board.fields.get(58).setIsFree(true);
                    Board.fields.get(58).setPieceColor(2);
                    if (castlingIsValid) {
                        Board.fields.get(60).setPiece(null);
                        Board.fields.get(60).setIsFree(true);
                        Board.fields.get(59).setPiece("kw");
                        Board.fields.get(59).setIsFree(false);
                        Board.fields.get(59).setPieceColor(color);
                        castlingIsValid = isCheck(fields) ^ true;
                        Board.fields.get(60).setPiece("kw");
                        Board.fields.get(60).setIsFree(false);
                        Board.fields.get(59).setPiece(null);
                        Board.fields.get(59).setIsFree(true);
                        Board.fields.get(59).setPieceColor(2);
                    } else {
                        return castlingIsValid;
                    }
                    if (castlingIsValid) {
                        castlingSide = 'q';
                    }
                }
            } else if (this.firstMove && (rook.getIndexInFields() == 63) && rook.getIsFirstMove()) {
                for (int i = 61; i < 63; ++i) {
                    if (!fields.get(i).getIsFree()) {
                        castlingIsValid = false;
                        break;
                    }
                }
                if (castlingIsValid) {
                    Board.fields.get(60).setPiece(null);
                    Board.fields.get(60).setIsFree(true);
                    Board.fields.get(61).setPiece("kw");
                    Board.fields.get(61).setIsFree(false);
                    Board.fields.get(61).setPieceColor(color);
                    castlingIsValid = isCheck(Board.fields) ^ true;
                    Board.fields.get(60).setPiece("kw");
                    Board.fields.get(60).setIsFree(false);
                    Board.fields.get(61).setPiece(null);
                    Board.fields.get(61).setIsFree(true);
                    Board.fields.get(61).setPieceColor(2);
                    if (castlingIsValid) {
                        Board.fields.get(60).setPiece(null);
                        Board.fields.get(60).setIsFree(true);
                        Board.fields.get(62).setPiece("kw");
                        Board.fields.get(62).setIsFree(false);
                        Board.fields.get(62).setPieceColor(color);
                        castlingIsValid = isCheck(fields) ^ true;
                        Board.fields.get(60).setPiece("kw");
                        Board.fields.get(60).setIsFree(false);
                        Board.fields.get(62).setPiece(null);
                        Board.fields.get(62).setIsFree(true);
                        Board.fields.get(62).setPieceColor(2);
                    } else {
                        return castlingIsValid;
                    }
                    if (castlingIsValid) {
                        castlingSide = 'k';
                    }
                }
            }
        }
        if (castlingIsValid) {
            castlingMade = true;
            rook.setIsFirstMove(false);
            setIsFirstMove(false);
        }
        return castlingIsValid;
    }

    /**
     * Sets false after first move
     *
     * @param b
     */
    public void setIsFirstMove(boolean b) {
        this.firstMove = b;
    }

    /**
     *
     * @return 'q' - queen side, 'k' - king side
     */
    public char getCastlingSide() {
        return this.castlingSide;
    }

    /**
     *
     * @return index of piece that creates check
     */
    public int getCheckByPiece() {
        return checkByPiece;
    }

    /**
     *
     * @param diffIndexBetweenKingAndAttackPiece king's index - attacking piece
     * index
     * @return direction of attacking piece move
     */
    private int getDir(int diffIndexBetweenKingAndAttackPiece) {
        int dir = 0;
        if (diffIndexBetweenKingAndAttackPiece > 0) {
            if (diffIndexBetweenKingAndAttackPiece % 8 == 0) {
                dir = 8;
            } else if (diffIndexBetweenKingAndAttackPiece % 9 == 0) {
                dir = 9;
            } else if (diffIndexBetweenKingAndAttackPiece % 7 == 0) {
                dir = 7;
            } else if (diffIndexBetweenKingAndAttackPiece < 8) {
                dir = 1;
            }
        } else {
            if (diffIndexBetweenKingAndAttackPiece % 8 == 0) {
                dir = -8;
            } else if (diffIndexBetweenKingAndAttackPiece % 9 == 0) {
                dir = -9;
            } else if (diffIndexBetweenKingAndAttackPiece % 7 == 0) {
                dir = -7;
            } else if (diffIndexBetweenKingAndAttackPiece > -8) {
                dir = -1;
            }
        }
        return dir;
    }
}
