package kr.ac.cau.embedded.a4chess.chess.pieces;

import java.util.LinkedList;
import java.util.List;

import kr.ac.cau.embedded.a4chess.chess.Coordinate;

public class King extends Piece {

    public King(Coordinate coordinate, String id) {
        super(coordinate, id);
    }

    @Override
    public List<Coordinate> getPossiblePositions() {
        List<Coordinate> possiblePositions = new LinkedList<Coordinate>();
        int x = position.x;
        int y = position.y;
        Coordinate coordinate;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                coordinate = new Coordinate(x + i, y + j);
                if (coordinate.isValid() && !sameTeam(coordinate)) {
                    possiblePositions.add(coordinate);
                }
            }
        }
        return possiblePositions;
    }

    @Override
    public String getString() {
        return "\u265A";
    }
}
