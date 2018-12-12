package kr.ac.cau.embedded.a4chess.chess;

public class Coordinate {
    public final int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(int x, int y, int rotations) {
        int boardSize = Board.getBoardSize() - 1;
        if(rotations != 0) {
            int temp;
            for(int i = 0; i < rotations; i++) {
                temp = x;
                x = y;
                y = boardSize - temp;
            }
        }
        this.x = x;
        this.y = y;
    }

    public boolean isValid() {
        int boardSize = Board.getBoardSize();

        return (x >= 0 && y >= 0 && x <= boardSize - 1 && y <= boardSize - 1) &&
                !(x <= 2 && y <= 2) && // bottom left
                !(x >= boardSize - 3 && y <= 2) && // bottom right
                !(x <= 2 && y >= boardSize - 3) && // upper left
                !(x >= boardSize - 3 && y >= boardSize - 3); // upper right
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Coordinate &&
                ((Coordinate) other).x == x && ((Coordinate) other).y == y;
    }
}
