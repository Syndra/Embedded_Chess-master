package kr.ac.cau.embedded.a4chess.device;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

import kr.ac.cau.embedded.a4chess.GameFragment;
import kr.ac.cau.embedded.a4chess.MainActivity;
import kr.ac.cau.embedded.a4chess.chess.Board_ConditionChecker;
import kr.ac.cau.embedded.a4chess.chess.Game;

public class DotPrintCurrentCondition {

    public final static int CHECKED = 101;
    public final static int CHECKMATED = 102;
    public final static int STALEMATEED = 103;
    public final static int WIN = 104;
    public final static int LOSE = 105;

    public static void run(final int conditionNum)
    {
        Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            @Override
            public void run()
            {
                DeviceSemaphore.dot_init();
                DeviceController.DotmatrixWrite(conditionNum);
                DeviceSemaphore.dot_deinit();
            }
        };

        timer.schedule(task, 10);
    }

    public static void condCheckAndPrint()
    {
        for(int i = 0; i < Game.players.length; i++)
        {
            if(Board_ConditionChecker.isStaleMated(Game.players[i].id) && !Game.deadPlayers.contains(Game.players[i].id)) {
            //if(true) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        BuzzerAlarm.run();
                        DotPrintCurrentCondition.run(DotPrintCurrentCondition.STALEMATEED);
                    }
                };
                timer.schedule(task, 50);

                Game.UI.RunStaleMate();

                break;
            }
        }


        if(Game.deadPlayers.contains(Game.myPlayerId))
        {
            return;
        }

        if(Board_ConditionChecker.isPlyaerChecked(Game.myPlayerId) && ! Board_ConditionChecker.isCheckMated(Game.myPlayerId)){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    BuzzerAlarm.run();
                    DotPrintCurrentCondition.run(DotPrintCurrentCondition.CHECKED);
                }
            };
            timer.schedule(task, 50);
            return;
        }
        else if(Board_ConditionChecker.isCheckMated(Game.myPlayerId)){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    BuzzerAlarm.run();
                    DotPrintCurrentCondition.run(DotPrintCurrentCondition.CHECKMATED);
                }
            };
            timer.schedule(task, 50);
            return;
        }
        else if(Board_ConditionChecker.isStaleMated(Game.myPlayerId)){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    BuzzerAlarm.run();
                    DotPrintCurrentCondition.run(DotPrintCurrentCondition.STALEMATEED);
                }
            };
            timer.schedule(task, 50);
            return;
        }
        else if(Game.isGameOver()) {
            if(Game.deadPlayers.contains(Game.myPlayerId)) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        DotPrintCurrentCondition.run(DotPrintCurrentCondition.LOSE);
                    }
                };
                timer.schedule(task, 50);
                return;
            }
            else {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        DotPrintCurrentCondition.run(DotPrintCurrentCondition.WIN);
                    }
                };
                timer.schedule(task, 50);
                return;
            }

        }
    }
}
