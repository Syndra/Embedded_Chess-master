package kr.ac.cau.embedded.a4chess.device;

import kr.ac.cau.embedded.a4chess.chess.Game;

public class LcdPrintTurn {

    public static void write()
    {
        String currentPlayer = Game.currentPlayer();
        int i = (Game.turns+1)%Game.players.length;
        String nextPlayer;
        while(true) {
            if(Game.isAlivePlayer(Game.players[i].id)) {
                nextPlayer = Game.players[i].id;
                break;
            }
            i = (i+1)%Game.players.length;
        }

        DeviceController.LcdWrite("Current Turn : " + currentPlayer,"Next Turn : " + nextPlayer);
    }
}
