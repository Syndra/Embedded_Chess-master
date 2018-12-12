package kr.ac.cau.embedded.a4chess.chess.pieces;

import java.util.LinkedList;
import java.util.List;

import kr.ac.cau.embedded.a4chess.chess.Board;
import kr.ac.cau.embedded.a4chess.chess.Coordinate;

public class Pawn extends Piece {
    public Pawn(Coordinate coordinate, String id) {
        super(coordinate, id);
    }

    public List<Coordinate> getPossiblePositions() {
        List<Coordinate> possiblePositions = new LinkedList<Coordinate>();
        Coordinate coordinate;
        int x = position.x;
        int y = position.y;
        coordinate = new Coordinate(x, y + 1);
        if (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
        }

        // can move two squares at the beginning
        if (y == 1 && Board.getPiece(coordinate) == null) {
            coordinate = new Coordinate(x, y + 2);
            if (coordinate.isValid() && Board.getPiece(coordinate) == null) {
                possiblePositions.add(coordinate);
            }
        }

        coordinate = new Coordinate(x + 1, y + 1);
        if (coordinate.isValid() && Board.getPiece(coordinate) != null && !sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }
        coordinate = new Coordinate(x - 1, y + 1);
        if (coordinate.isValid() && Board.getPiece(coordinate) != null && !sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }
        return possiblePositions;
    }

    public String getString() {
        return "\u265F";
    }
}
