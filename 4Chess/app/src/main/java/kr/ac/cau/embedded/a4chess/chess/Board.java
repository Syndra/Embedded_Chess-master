package kr.ac.cau.embedded.a4chess.chess;

import android.util.Log;
import android.util.Pair;

import kr.ac.cau.embedded.a4chess.GameFragment;
import kr.ac.cau.embedded.a4chess.chess.pieces.Bishop;
import kr.ac.cau.embedded.a4chess.chess.pieces.DownPawn;
import kr.ac.cau.embedded.a4chess.chess.pieces.King;
import kr.ac.cau.embedded.a4chess.chess.pieces.Knight;
import kr.ac.cau.embedded.a4chess.chess.pieces.LeftPawn;
import kr.ac.cau.embedded.a4chess.chess.pieces.Pawn;
import kr.ac.cau.embedded.a4chess.chess.pieces.Piece;
import kr.ac.cau.embedded.a4chess.chess.pieces.Queen;
import kr.ac.cau.embedded.a4chess.chess.pieces.RightPawn;
import kr.ac.cau.embedded.a4chess.chess.pieces.Rook;
import kr.ac.cau.embedded.a4chess.device.LcdPrintTurn;

public class Board {
    private static Piece[][] BoardState;
    private static final int boardSize = 14;

    private static Piece LastDeletes;
    private static Coordinate LastOld;
    private static Coordinate LastNew;

    private Board() { }

    public static int getBoardSize() {
        return boardSize;
    }
    public static Piece getPiece(Coordinate coordinate) {
        return BoardState[coordinate.x][coordinate.y];
    }

    public static int getRotation() {
//        if (Game.match.isLocal) return 0;
//        for (int i = 0; i < 4; i++) {
//            if (Game.players[i].id.equals(Game.myPlayerId))
//                return Game.players.length > 2 ? i : i * 2;
//        }
        return 0;
    }

    public static void newGame(final Player[] players) {
        BoardState = new Piece[boardSize][boardSize];

        // setup player 1 (bottom)
        setupPlayerTopBottom(3, 1, 0, players[0].id);

        // setup player 2 (right)
        setupPlayerLeftRight(boardSize - 2, boardSize - 1, players[1].id);

        // setup player 3 (top)
        setupPlayerTopBottom(3, boardSize - 2, boardSize - 1, players[2].id);

        // setup player 4 (left)
        setupPlayerLeftRight(1, 0, players[3].id);

        LcdPrintTurn.write();
    }

    private static void setupPlayerTopBottom(int xStart, int yStart, int yEnd, final String playerId) {
        for (int x = xStart; x < xStart + 8; x++) {
            BoardState[x][yStart] = yStart == boardSize - 2 ?
                    new DownPawn(new Coordinate(x, yStart), playerId) :
                    new Pawn(new Coordinate(x, yStart), playerId);
        }
        BoardState[xStart][yEnd] = new Rook(new Coordinate(xStart, yEnd), playerId);
        BoardState[xStart + 1][yEnd] = new Knight(new Coordinate(xStart + 1, yEnd), playerId);
        BoardState[xStart + 2][yEnd] = new Bishop(new Coordinate(xStart + 2, yEnd), playerId);
        BoardState[xStart + 3][yEnd] = new Queen(new Coordinate(xStart + 3, yEnd), playerId);
        BoardState[xStart + 4][yEnd] = new King(new Coordinate(xStart + 4, yEnd), playerId);
        BoardState[xStart + 5][yEnd] = new Bishop(new Coordinate(xStart + 5, yEnd), playerId);
        BoardState[xStart + 6][yEnd] = new Knight(new Coordinate(xStart + 6, yEnd), playerId);
        BoardState[xStart + 7][yEnd] = new Rook(new Coordinate(xStart + 7, yEnd), playerId);
    }

    private static void setupPlayerLeftRight(int xStart, int xEnd, final String playerId) {
        for (int y = 3; y < boardSize - 3; y++) {
            BoardState[xStart][y] =
                    xStart == 1 ?
                            new RightPawn(new Coordinate(xStart, y), playerId) :
                            new LeftPawn(new Coordinate(xStart, y), playerId);
        }
        BoardState[xEnd][3] = new Rook(new Coordinate(xEnd, 3), playerId);
        BoardState[xEnd][4] = new Knight(new Coordinate(xEnd, 4), playerId);
        BoardState[xEnd][5] = new Bishop(new Coordinate(xEnd, 5), playerId);
        BoardState[xEnd][6] = new King(new Coordinate(xEnd, 6), playerId);
        BoardState[xEnd][7] = new Queen(new Coordinate(xEnd, 7), playerId);
        BoardState[xEnd][8] = new Bishop(new Coordinate(xEnd, 8), playerId);
        BoardState[xEnd][9] = new Knight(new Coordinate(xEnd, 9), playerId);
        BoardState[xEnd][10] = new Rook(new Coordinate(xEnd, 10), playerId);
    }

    public static boolean move(String PlayerId, final Coordinate oldPosition, final Coordinate newPosition) {
//        if (!Game.myTurn()) {
//            return false;
//        }
        if(!Game.isValidTurn(PlayerId)){
            return false;
        }

        if (!newPosition.isValid()) {
            return false; // not a valid new position
        }

        Piece piece = BoardState[oldPosition.x][oldPosition.y];
        if (!piece.getPossiblePositions().contains(newPosition)) {
            return false; // not possible to move there
        }

        Piece target = BoardState[newPosition.x][newPosition.y];

        // Castling

        // move the piece
        BoardState[newPosition.x][newPosition.y] = BoardState[oldPosition.x][oldPosition.y];
        BoardState[oldPosition.x][oldPosition.y] = null;
        piece.position = newPosition;
        piece.isMovedOnce = true;

        Game.getPlayer(Game.currentPlayer()).lastMove =
                new Pair<Coordinate, Coordinate>(oldPosition, newPosition);

        if (target != null && target instanceof King && Game.removePlayer(target.getPlayerId())) {
            // game ended
            Game.over();
        } else {
            Game.moved();
        }

        for(Player elem : Game.players)
        {
            Log.d("CHECK", "Player " + elem.id +  " is under " + Board_ConditionChecker.checkPlayerCondition(elem.id));
        }

        LcdPrintTurn.write();
//        GameFragment.changeVisibleKingSideCastling();
//        GameFragment.changeVisibleQueenSideCastling();

        return true;
    }

    public static boolean kingSideCastling(String PlayerId) {

        if(!Game.isValidTurn(PlayerId)){
            return false;
        }
        else{
            Coordinate coord[] = new Coordinate[4];
            int i = 0;
            for(Coordinate temp : Board_ConditionChecker.getKingSideCastlingCoordinatePair(PlayerId)) {
                coord[i] = temp;
                i++;
            }

            Piece king = Board.getPiece(coord[0]);
            Piece rook = Board.getPiece(coord[1]);

            BoardState[coord[2].x][coord[2].y] = king;
            BoardState[coord[3].x][coord[3].y] = rook;

            BoardState[coord[0].x][coord[0].y] = null;
            BoardState[coord[1].x][coord[1].y] = null;

            king.position = coord[2];
            rook.position = coord[3];

            Game.moved();
            LcdPrintTurn.write();
        }

        for(Player elem : Game.players)
        {
            Log.d("CHECK", "Player " + elem.id +  " is under " + Board_ConditionChecker.checkPlayerCondition(elem.id));
        }

        return true;
    }

    public static boolean queenSideCastling(String PlayerId) {

        if(!Game.isValidTurn(PlayerId)){
            return false;
        }
        if(Board_ConditionChecker.getQueenSideCastlingCoordinatePair(PlayerId) == null){
            return false;
        }
        else{
            Coordinate coord[] = Board_ConditionChecker.getQueenSideCastlingCoordinatePair(PlayerId);

            Piece king = Board.getPiece(coord[0]);
            Piece rook = Board.getPiece(coord[1]);

            BoardState[coord[2].x][coord[2].y] = king;
            BoardState[coord[3].x][coord[3].y] = rook;

            BoardState[coord[0].x][coord[0].y] = null;
            BoardState[coord[1].x][coord[1].y] = null;

            king.position = coord[2];
            rook.position = coord[3];

            Game.moved();
            LcdPrintTurn.write();
        }

        for(Player elem : Game.players)
        {
            Log.d("CHECK", "Player " + elem.id +  " is under " + Board_ConditionChecker.checkPlayerCondition(elem.id));
        }

        return true;
    }

    //After calls test_move, u must call test_retreat!
    public static void test_move(final Coordinate oldPosition, final Coordinate newPosition) {

        Piece piece = BoardState[oldPosition.x][oldPosition.y];
        Piece target = BoardState[newPosition.x][newPosition.y];

        // move the piece
        BoardState[newPosition.x][newPosition.y] = BoardState[oldPosition.x][oldPosition.y];
        BoardState[oldPosition.x][oldPosition.y] = null;
        piece.position = newPosition;

        LastDeletes = target;
        LastOld = oldPosition;
        LastNew = newPosition;
    }

    public static void test_retreat() {
        Piece piece = BoardState[LastNew.x][LastNew.y];

        BoardState[LastOld.x][LastOld.y] = BoardState[LastNew.x][LastNew.y];
        BoardState[LastNew.x][LastNew.y] = LastDeletes;
        piece.position = LastOld;

        LastDeletes = null;
        LastOld = null;
        LastNew = null;
    }

    public static void removePlayer(final String playerId) {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (BoardState[x][y] != null && playerId.equals(BoardState[x][y].getPlayerId())) {
                    BoardState[x][y] = null;
                }
            }
        }
    }
}
