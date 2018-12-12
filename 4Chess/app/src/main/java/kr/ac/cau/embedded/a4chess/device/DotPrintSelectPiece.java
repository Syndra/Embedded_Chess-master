package kr.ac.cau.embedded.a4chess.device;

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

public class DotPrintSelectPiece {

    public static void PrintSelectPiece(Piece currentPiece)
    {
        //pawn
        if(currentPiece instanceof Pawn || currentPiece instanceof LeftPawn || currentPiece instanceof RightPawn || currentPiece instanceof DownPawn) {
            DeviceController.DotmatrixWrite(1);
        }
        //bishop
        else if(currentPiece instanceof Bishop) {
            DeviceController.DotmatrixWrite(3);
        }
        //rook
        else if(currentPiece instanceof Rook) {
            DeviceController.DotmatrixWrite(2);
        }
        //knight
        else if(currentPiece instanceof Knight) {
            DeviceController.DotmatrixWrite(4);
        }
        //queen
        else if(currentPiece instanceof Queen) {
            DeviceController.DotmatrixWrite(5);
        }
        //king
        else if(currentPiece instanceof King) {
            DeviceController.DotmatrixWrite(6);
        }
    }
}
