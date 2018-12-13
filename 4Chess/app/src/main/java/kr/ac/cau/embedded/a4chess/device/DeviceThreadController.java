package kr.ac.cau.embedded.a4chess.device;

import java.util.Timer;
import java.util.TimerTask;

import kr.ac.cau.embedded.a4chess.chess.Board_ConditionChecker;
import kr.ac.cau.embedded.a4chess.chess.Game;
import kr.ac.cau.embedded.a4chess.chess.Player;

public class DeviceThreadController {

        public static boolean isAlreadyPrinted = false;

        public static void run()
        {
            Timer timer = new Timer();

            TimerTask task = new TimerTask(){
                @Override
                public void run()
                {
                    //led
                    if(Game.currentPlayer().equals(Game.myPlayerId))
                    {
                        DeviceController.LedWrite(255);
                    }
                    else{
                        DeviceController.LedWrite(0);
                    }

                    //lcd
                    LcdPrintTurn.write();
                    //7segment
                    DeviceController.SSegmentWrite(SsegPrintTime.leftTime);
                    //Condition Check DotMatrix
                }
            };

            timer.schedule(task, 10,100);
        }
}
