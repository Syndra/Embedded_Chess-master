package kr.ac.cau.embedded.a4chess.chess.pieces;

import java.util.LinkedList;
import java.util.List;

import kr.ac.cau.embedded.a4chess.chess.Coordinate;

public class Knight extends Piece {
    public Knight(Coordinate coordinate, String id){
        super(coordinate, id);
    }

    @Override
    public List<Coordinate> getPossiblePositions() {
        List<Coordinate> possiblePositions = new LinkedList<Coordinate>();
        Coordinate coordinate = new Coordinate(position.x + 2, position.y + 1);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        coordinate = new Coordinate(position.x + 2, position.y - 1);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        coordinate = new Coordinate(position.x - 2, position.y + 1);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        coordinate = new Coordinate(position.x - 2, position.y - 1);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        coordinate = new Coordinate(position.x + 1, position.y + 2);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        coordinate = new Coordinate(position.x - 1, position.y + 2);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        coordinate = new Coordinate(position.x + 1, position.y - 2);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        coordinate = new Coordinate(position.x - 1, position.y - 2);
        if (coordinate.isValid() && !sameTeam(coordinate)) possiblePositions.add(coordinate);

        return possiblePositions;
    }

    @Override
    public String getString() {
        return "\u265E";
    }
}
