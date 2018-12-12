package kr.ac.cau.embedded.a4chess.chess.pieces;

import java.util.LinkedList;
import java.util.List;

import kr.ac.cau.embedded.a4chess.chess.Board;
import kr.ac.cau.embedded.a4chess.chess.Coordinate;

public class Bishop extends Piece {
    public Bishop(Coordinate coordinate, String id) {
        super(coordinate, id);
    }

    @Override
    public List<Coordinate> getPossiblePositions() {
        return moveDiagonal(this);
    }

    public static List<Coordinate> moveDiagonal(Piece piece) {
        List<Coordinate> possiblePositions = new LinkedList<Coordinate>();
        int x = piece.position.x + 1;
        int y = piece.position.y + 1;
        Coordinate coordinate = new Coordinate(x, y);

        // move to top right
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            y++;
            x++;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        // move to bottom right
        x = piece.position.x + 1;
        y = piece.position.y - 1;
        coordinate = new Coordinate(x, y);
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            y--;
            x++;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        // move top left
        x = piece.position.x - 1;
        y = piece.position.y + 1;
        coordinate = new Coordinate(x, y);
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            x--;
            y++;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        // move bottom left
        x = piece.position.x - 1;
        y = piece.position.y - 1;
        coordinate = new Coordinate(x, y);
        while (coordinate.isValid() && Board.getPiece(coordinate) == null) {
            possiblePositions.add(coordinate);
            x--;
            y--;
            coordinate = new Coordinate(x, y);
        }
        if (coordinate.isValid() && !piece.sameTeam(coordinate)) {
            possiblePositions.add(coordinate);
        }

        return possiblePositions;
    }

    @Override
    public String getString() {
        return "\u265D";
    }
}
