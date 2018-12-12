package kr.ac.cau.embedded.a4chess.chess.pieces;

import java.util.List;

import kr.ac.cau.embedded.a4chess.chess.Coordinate;

public class Queen extends Piece {
    public Queen(Coordinate coordinate, String id) {
        super(coordinate, id);
    }

    @Override
    public List<Coordinate> getPossiblePositions() {
        List<Coordinate> possiblePositions = Rook.moveStraight(this);
        possiblePositions.addAll(Bishop.moveDiagonal(this));
        return possiblePositions;
    }

    @Override
    public String getString() {
        return "\u265B";
    }
}
