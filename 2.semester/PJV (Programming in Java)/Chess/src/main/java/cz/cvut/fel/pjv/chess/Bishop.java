package cz.cvut.fel.pjv.chess;

import java.util.ArrayList;

/**
 *
 * @author semenvol,danilrom
 */
public class Bishop extends Piece {
    
    Bishop(final int color, int x, int y, final String name, int index) {
        super(color, x, y, name, index);
    }

    @Override
    public ArrayList<Field> createPossibleMoves(ArrayList<Field> fields, int index) {
        this.possible_moves = new ArrayList<Field>();
        int dir = -9;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) >= 0 && (fields.get(index + (i * dir)).getIsFree()
                    || fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2)) {
                if ((index + (i * dir)) % 8 == 7) {
                    break;
                }
                if (previousFieldIsFree(fields, index, dir, i)) {
                    possible_moves.add(fields.get(index + (i * dir)));
                }
            }
        }
        dir = -7;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) >= 0 && (fields.get(index + (i * dir)).getIsFree()
                    || fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2)) {
                if ((index + (i * dir)) % 8 == 0) {
                    break;
                }
                if (previousFieldIsFree(fields, index, dir, i)) {
                    possible_moves.add(fields.get(index + (i * dir)));
                }
            }

        }
        dir = 7;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) < 64 && (fields.get(index + (i * dir)).getIsFree()
                    || fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2)) {
                if ((index + (i * dir)) % 8 == 7) {
                    break;
                }
                if (previousFieldIsFree(fields, index, dir, i)) {
                    possible_moves.add(fields.get(index + (i * dir)));
                }
            }
        }
        dir = 9;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) < 64 && (fields.get(index + (i * dir)).getIsFree()
                    || fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2)) {
                if ((index + (i * dir)) % 8 == 0) {
                    break;
                }
                if (previousFieldIsFree(fields, index, dir, i)) {
                    possible_moves.add(fields.get(index + (i * dir)));
                }
            }
        }
        return possible_moves;
    }

    @Override
    protected ArrayList<Field> createAttackMoves(ArrayList<Field> fields, int index) {
        this.attackMoves = new ArrayList<Field>();
        int dir = -9;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) >= 0) {
                if ((index + (i * dir)) % 8 == 7) {
                    break;
                }
                if (fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2) {

                    if (previousFieldIsFree(fields, index, dir, i)) {
                        attackMoves.add(fields.get(index + (i * dir)));
                    }
                }
            }
        }
        dir = -7;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) >= 0) {
                if ((index + (i * dir)) % 8 == 0) {
                    break;
                }
                if (fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2) {
                    if (previousFieldIsFree(fields, index, dir, i)) {
                        attackMoves.add(fields.get(index + (i * dir)));
                    }
                }
            }
        }
        dir = 7;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) < 64) {
                if ((index + (i * dir)) % 8 == 7) {
                    break;
                }
                if (fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2) {
                    if (previousFieldIsFree(fields, index, dir, i)) {
                        attackMoves.add(fields.get(index + (i * dir)));
                    }
                }
            }
        }
        dir = 9;
        for (int i = 1; i < 8; ++i) {
            if (index + (i * dir) < 64) {
                if ((index + (i * dir)) % 8 == 0) {
                    break;
                }
                if (fields.get(index + (i * dir)).getPieceColor() == (color + 1) % 2) {
                    if (previousFieldIsFree(fields, index, dir, i)) {
                        attackMoves.add(fields.get(index + (i * dir)));
                    }
                }
            }
        }
        return attackMoves;
    }
}
