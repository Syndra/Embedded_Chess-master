package kr.ac.cau.embedded.a4chess.chess.pieces;

import java.util.LinkedList;
import java.util.List;

import kr.ac.cau.embedded.a4chess.chess.Board;
import kr.ac.cau.embedded.a4chess.chess.Coordinate;

public class Rook extends Piece {

    public Rook(Coordinate coordinate, String id) {
        super(coordinate, id);
    }

    @Override
    public List<Coordinate> getPossiblePositions() {
        return moveStraight(this);
    }

    public static List<Coordinate> moveStraight(Piece piece) {
        List<Coordinate> possiblePositions = new LinkedList<Coordinate>();

        // move to top
        int x = piece.position.x;
        int y = piece.position.y + 1;
        Coordinate coordinate = new Coordinate(x, y);
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            y++;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        // move to bottom
        y = piece.position.y - 1;
        coordinate = new Coordinate(x, y);
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            y--;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        // move right
        y = piece.position.y;
        x = piece.position.x + 1;
        coordinate = new Coordinate(x, y);
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            x++;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        // move left
        x = piece.position.x - 1;
        coordinate = new Coordinate(x, y);
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            x--;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        return possiblePositions;
    }

    public String getString() {
        return "\u265C";
    }
}
