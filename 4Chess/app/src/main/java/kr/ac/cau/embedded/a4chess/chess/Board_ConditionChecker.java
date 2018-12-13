package kr.ac.cau.embedded.a4chess.chess;

import java.util.List;
import java.util.ArrayList;
import android.util.Log;

import kr.ac.cau.embedded.a4chess.GameFragment;
import kr.ac.cau.embedded.a4chess.chess.pieces.Piece;
import kr.ac.cau.embedded.a4chess.chess.pieces.King;
import kr.ac.cau.embedded.a4chess.chess.pieces.Rook;

public class Board_ConditionChecker {

    public static String checkPlayerCondition(String PlayerId) {
        if(Game.isAlivePlayer(PlayerId)) {
            if (isPlyaerChecked(PlayerId)) {
                if (isCheckMated(PlayerId))
                    return "checkmate";
                else
                    return "check";
            } else {
                if (isStaleMated(PlayerId))
                    return "stalemate";
                else
                    return "none";
            }
        }
        return "none";
    }

    public static boolean isPlyaerChecked(String PlayerId) {

        for(String elem : Game.deadPlayers)
        {
            if(elem.equals(PlayerId))
            {
                return false;
            }
        }

        Coordinate currentPlayerKingCoordinate = getKingCoordinate(PlayerId).position;

        //check all enemy piece's possiblemoves.
        //If possiblemoves list contains currentplayerking coordinate, current player is determined
        //being under check condition.

        String myId = Game.myPlayerId;
        Integer a = new Integer(myId);
        a = (a+3)%4;
        String prevId = a.toString();

        for(int i = 0 ; i < Board.getBoardSize(); i ++) {
            for(int j = 0; j < Board.getBoardSize(); j++) {
                if(Board.getPiece(new Coordinate(i,j)) == null){
                    continue;
                }
                if( !Game.sameTeam(Board.getPiece(new Coordinate(i, j)).getPlayerId(), PlayerId)
                        && Board.getPiece(new Coordinate(i, j)).getPossiblePositions().contains(currentPlayerKingCoordinate)){
                        return true;
                }
            }
        }

        return false;
    }

    public static boolean isPlyaerChecked(String PlayerId, Piece[][] board) {

        Coordinate currentPlayerKingCoordinate = getKingCoordinate(PlayerId, board).position;

        //check all enemy piece's possiblemoves.
        //If possiblemoves list contains currentplayerking coordinate, current player is determined
        //being under check condition.
        for(int i = 0 ; i < Board.getBoardSize(); i ++) {
            for(int j = 0; j < Board.getBoardSize(); j++) {
                if(board[i][j] == null){
                    continue;
                }
                if( !Game.sameTeam(board[i][j].getPlayerId(), PlayerId)
                        && board[i][j].getPossiblePositions().contains(currentPlayerKingCoordinate)){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isCheckMated(String PlayerId) {

        /* if Player is not under check condition, return. */
        if(!isPlyaerChecked(PlayerId))
            return false;

        Piece[][] TempBoardState = new Piece[14][14];

        for(int x = 0; x < 14; x ++) {
            for(int y = 0; y < 14; y ++)
            {
                TempBoardState[x][y] = Board.getPiece(new Coordinate(x,y));
            }
        }

        /* Look All possible moves of player */
        boolean isCheckMated = true;

        for (int i = 0; i < Board.getBoardSize(); i++) {
            for (int j = 0; j < Board.getBoardSize(); j++) {
                if(Board.getPiece(new Coordinate(i,j)) == null){
                    continue;
                }
                Coordinate playerPiecePos = new Coordinate(i, j);
                if (Board.getPiece(playerPiecePos).getPlayerId().equals(PlayerId)) {
                    for(Coordinate elem : Board.getPiece(playerPiecePos).getPossiblePositions()){
                        Board.test_move(playerPiecePos, elem, TempBoardState);
                        isCheckMated = isPlyaerChecked(PlayerId, TempBoardState);
                        Board.test_retreat(TempBoardState);
                        if(!isCheckMated)
                            return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean isStaleMated(String PlayerId) {

        /* if Player is not under check condition, return. */
        if(isPlyaerChecked(PlayerId))
            return false;

        Piece[][] TempBoardState = new Piece[14][14];

        for(int x = 0; x < 14; x ++) {
            for(int y = 0; y < 14; y ++)
            {
                TempBoardState[x][y] = Board.getPiece(new Coordinate(x,y));
            }
        }

        /* Look All possible moves of player */
        boolean isStaleMated = true;

        for (int i = 0; i < Board.getBoardSize(); i++) {
            for (int j = 0; j < Board.getBoardSize(); j++) {
                if(Board.getPiece(new Coordinate(i,j)) == null){
                    continue;
                }
                Coordinate playerPiecePos = new Coordinate(i, j);
                if (Board.getPiece(playerPiecePos).getPlayerId().equals(PlayerId)) {
                    for(Coordinate elem : Board.getPiece(playerPiecePos).getPossiblePositions()){
                        Board.test_move(playerPiecePos, elem, TempBoardState);
                        isStaleMated &= isPlyaerChecked(PlayerId, TempBoardState);
                        Board.test_retreat(TempBoardState);
                        if(!isStaleMated)
                            return isStaleMated;
                    }
                }
            }
        }

        return isStaleMated;
    }

   static public int isQueenSideCastlingAvailable(String PlayerId){

        if(isPlyaerChecked(PlayerId))
            return 0;

        int playerPos = 0;

        boolean isRightKing = true;

        for(int i = 0; i < Game.players.length; i++) {
            if(PlayerId == Game.players[i].id){
                playerPos = i;
            }
        }

        if(playerPos == 1 || playerPos == 2)
            isRightKing = false;

        if(isRightKing){
            if(Board.getPiece(new Coordinate(3, 0)) instanceof Rook
                    && !Board.getPiece(new Coordinate(3, 0)).isMovedOnce
                    && Board.getPiece(new Coordinate(4, 0)) == null
                    && Board.getPiece(new Coordinate(5, 0)) == null
                    && Board.getPiece(new Coordinate(6, 0)) == null
                    && Board.getPiece(new Coordinate(7, 0)) instanceof King
                    && !Board.getPiece(new Coordinate(7, 0)).isMovedOnce) {
                return 37;
            }
        }
        else{
            if(Board.getPiece(new Coordinate(10, 0)) instanceof Rook
                    && !Board.getPiece(new Coordinate(10, 0)).isMovedOnce
                    && Board.getPiece(new Coordinate(9, 0)) == null
                    && Board.getPiece(new Coordinate(8, 0)) == null
                    && Board.getPiece(new Coordinate(7, 0)) == null
                    && Board.getPiece(new Coordinate(6, 0)) instanceof King
                    && !Board.getPiece(new Coordinate(6, 0)).isMovedOnce) {
                return 106;
            }
        }

        return 0;
    }

    static public int isKingSideCastlingAvailable(String PlayerId){

        if(isPlyaerChecked(PlayerId))
            return 0;

        int playerPos = 0;

        boolean isRightKing = true;

        for(int i = 0; i < Game.players.length; i++) {
            if(PlayerId == Game.players[i].id){
                playerPos = i;
            }
        }

        if(playerPos == 1 || playerPos == 2)
            isRightKing = false;

        if(isRightKing){
            if(Board.getPiece(new Coordinate(10, 0)) instanceof Rook
                    && !Board.getPiece(new Coordinate(10, 0)).isMovedOnce
                    && Board.getPiece(new Coordinate(9, 0)) == null
                    && Board.getPiece(new Coordinate(8, 0)) == null
                    && Board.getPiece(new Coordinate(7, 0)) instanceof King
                    && !Board.getPiece(new Coordinate(7, 0)).isMovedOnce) {
                return 107;
            }
        }
        else{
            if(Board.getPiece(new Coordinate(3, 0)) instanceof Rook
                    && !Board.getPiece(new Coordinate(3, 0)).isMovedOnce
                    && Board.getPiece(new Coordinate(4, 0)) == null
                    && Board.getPiece(new Coordinate(5, 0)) == null
                    && Board.getPiece(new Coordinate(6, 0)) instanceof King
                    && !Board.getPiece(new Coordinate(6, 0)).isMovedOnce) {
                return 36;
            }
        }

        return 0;
    }

    public static Coordinate[] getKingSideCastlingCoordinatePair(String PlayerId) {
        Coordinate [] result = new Coordinate[4];

        int a = new Integer(PlayerId);
        a = 1-a%2;

        Coordinate KingCoord = Board_ConditionChecker.getKingCoordinate(PlayerId).position;
        int x = KingCoord.x;
        int y = KingCoord.y;

        if(Board.getPiece(new Coordinate(x, y)) instanceof King
                && !Board.getPiece(new Coordinate(x, y)).isMovedOnce
                && Board.getPiece(new Coordinate(x+(1*(a)), y-(1*(1-a)))) == null
                && Board.getPiece(new Coordinate(x+(2*(a)), y-(2*(1-a)))) == null
                && Board.getPiece(new Coordinate(x+(3*(a)), y-(3*(1-a)))) instanceof Rook
                && !Board.getPiece(new Coordinate(x+(3*(a)), y-(3*(1-a)))).isMovedOnce) {

            result[0] = new Coordinate(x, y);
            result[1] = new Coordinate(x+(3*(a)), y-(3*(1-a)));
            result[2] = new Coordinate(x+(2*(a)), y-(2*(1-a)));
            result[3] = new Coordinate(x+(1*(a)), y-(1*(1-a)));

            return result;
        }

        return null;
    }

    public static Coordinate[] getQueenSideCastlingCoordinatePair(String PlayerId) {
        Coordinate [] result = new Coordinate[4];

        int a = new Integer(PlayerId);
        a = 1-a%2;

        Coordinate KingCoord = Board_ConditionChecker.getKingCoordinate(PlayerId).position;
        int x = KingCoord.x;
        int y = KingCoord.y;

        if(Board.getPiece(new Coordinate(x, y)) instanceof King
                && !Board.getPiece(new Coordinate(x, y)).isMovedOnce
                && Board.getPiece(new Coordinate(x-(1*(a)), y+(1*(1-a)))) == null
                && Board.getPiece(new Coordinate(x-(2*(a)), y+(2*(1-a)))) == null
                && Board.getPiece(new Coordinate(x-(3*(a)), y+(3*(1-a)))) == null
                && Board.getPiece(new Coordinate(x-(4*(a)), y+(4*(1-a)))) instanceof Rook
                && !Board.getPiece(new Coordinate(x-(4*(a)), y+(4*(1-a)))).isMovedOnce) {

            result[0] = new Coordinate(x, y);
            result[1] = new Coordinate(x-(4*(a)), y+(4*(1-a)));
            result[2] = new Coordinate(x-(2*(a)), y+(2*(1-a)));
            result[3] = new Coordinate(x-(1*(a)), y+(1*(1-a)));

            return result;
        }

        return null;
    }

    private static Piece getKingCoordinate(String PlayerId){
        //get Player's current king coordinate
        for(int i = 0 ; i < Board.getBoardSize(); i ++) {
            for(int j = 0; j < Board.getBoardSize(); j++) {
                if(Board.getPiece(new Coordinate(i,j)) == null){
                    continue;
                }
                if(Board.getPiece(new Coordinate(i, j)).getPlayerId().equals(PlayerId)
                        && Board.getPiece(new Coordinate(i, j)) instanceof King){
                    return Board.getPiece(new Coordinate(i, j));
                }
            }
        }

        return null;
    }

    private static Piece getKingCoordinate(String PlayerId, Piece[][] board){
        //get Player's current king coordinate
        for(int i = 0 ; i < Board.getBoardSize(); i ++) {
            for(int j = 0; j < Board.getBoardSize(); j++) {
                if(board[i][j] == null){
                    continue;
                }
                if(board[i][j].getPlayerId().equals(PlayerId)
                        && board[i][j] instanceof King){
                    return board[i][j];
                }
            }
        }

        return null;
    }
}
