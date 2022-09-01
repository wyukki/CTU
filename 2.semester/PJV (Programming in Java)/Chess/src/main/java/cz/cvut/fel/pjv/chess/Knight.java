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
public class Knight extends Piece {

    Knight(final int color, int x, int y, final String name, int index) {
        super(color, x, y, name, index);
    }


    @Override
    public ArrayList<Field> createPossibleMoves(ArrayList<Field> fields, int index) {
        this.possible_moves = new ArrayList<Field>();

        if (index - 17 >= 0 && (fields.get(index - 17).getIsFree()
                || fields.get(index - 17).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 0 && (index - 17) % 8 != 7)) {
            possible_moves.add(fields.get(index - 17));
        }
        if (index - 15 >= 0 && (fields.get(index - 15).getIsFree()
                || fields.get(index - 15).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 7 && (index - 15) % 8 != 0)) {
            possible_moves.add(fields.get(index - 15));
        }
        if (index - 10 >= 0 && (fields.get(index - 10).getIsFree()
                || fields.get(index - 10).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 1 && (index - 10) % 8 != 7) && (index % 8 != 0 && (index - 10) % 8 != 6)) {
            possible_moves.add(fields.get(index - 10));
        }
        if (index - 6 >= 0 && (fields.get(index - 6).getIsFree()
                || fields.get(index - 6).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 7 && (index - 6) % 8 != 1) && (index % 8 != 6 && (index - 6) % 8 != 0)) {
            possible_moves.add(fields.get(index - 6));
        }
        if (index + 6 < 64 && (fields.get(index + 6).getIsFree()
                || fields.get(index + 6).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 0 && (index + 6) % 8 != 6) && (index % 8 != 1 && (index + 6) % 8 != 7)) {
            possible_moves.add(fields.get(index + 6));
        }
        if (index + 10 < 64 && (fields.get(index + 10).getIsFree()
                || fields.get(index + 10).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 6 && (index + 10) % 8 != 0) && (index % 8 != 7 && (index + 10) % 8 != 1)) {
            possible_moves.add(fields.get(index + 10));
        }
        if (index + 15 < 64 && (fields.get(index + 15).getIsFree()
                || fields.get(index + 15).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 0 && (index + 15) % 8 != 7)) {
            possible_moves.add(fields.get(index + 15));
        }
        if (index + 17 < 64 && (fields.get(index + 17).getIsFree()
                || fields.get(index + 17).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 7 && (index + 17) % 8 != 0)) {
            possible_moves.add(fields.get(index + 17));
        }

        return possible_moves;
    }

    @Override
    protected ArrayList<Field> createAttackMoves(ArrayList<Field> fields, int index) {
        this.attackMoves = new ArrayList<Field>();
        if (index - 17 >= 0 && (fields.get(index - 17).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 0 && (index - 17) % 8 != 7)) {
            attackMoves.add(fields.get(index - 17));
        }
        if (index - 15 >= 0 && (fields.get(index - 15).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 7 && (index - 15) % 8 != 0)) {
            attackMoves.add(fields.get(index - 15));
        }
        if (index - 10 >= 0 && (fields.get(index - 10).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 1 && (index - 10) % 8 != 7) && (index % 8 != 0 && (index - 10) % 8 != 6)) {
            attackMoves.add(fields.get(index - 10));
        }
        if (index - 6 >= 0 && (fields.get(index - 6).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 7 && (index - 6) % 8 != 1) && (index % 8 != 6 && (index - 6) % 8 != 0)) {
            attackMoves.add(fields.get(index - 6));
        }
        if (index + 6 < 64 && (fields.get(index + 6).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 0 && (index + 6) % 8 != 6) && (index % 8 != 1 && (index + 6) % 8 != 7)) {
            attackMoves.add(fields.get(index + 6));
        }
        if (index + 10 < 64 && (fields.get(index + 10).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 6 && (index + 10) % 8 != 0) && (index % 8 != 7 && (index + 10) % 8 != 1)) {
            attackMoves.add(fields.get(index + 10));
        }
        if (index + 15 < 64 && (fields.get(index + 15).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 0 && (index + 15) % 8 != 7)) {
            attackMoves.add(fields.get(index + 15));
        }
        if (index + 17 < 64 && (fields.get(index + 17).getPieceColor() == (color + 1) % 2)
                && (index % 8 != 7 && (index + 17) % 8 != 0)) {
            attackMoves.add(fields.get(index + 17));
        }
        return attackMoves;
    }
}
