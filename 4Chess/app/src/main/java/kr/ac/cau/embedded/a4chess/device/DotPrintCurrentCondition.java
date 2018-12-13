package kr.ac.cau.embedded.a4chess.device;

import java.util.Timer;
import java.util.TimerTask;

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
        if(Board_ConditionChecker.isPlyaerChecked(Game.myPlayerId)){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    BuzzerAlarm.run();
                    DotPrintCurrentCondition.run(DotPrintCurrentCondition.CHECKED);
                }
            };
            timer.schedule(task, 50);
            DeviceThreadController.isAlreadyPrinted = true;
            return;
        }
        else if(Board_ConditionChecker.isCheckMated(Game.myPlayerId) && DeviceThreadController.isAlreadyPrinted){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    BuzzerAlarm.run();
                    DotPrintCurrentCondition.run(DotPrintCurrentCondition.CHECKMATED);
                }
            };
            timer.schedule(task, 50);
            DeviceThreadController.isAlreadyPrinted = true;
            return;
        }
        else if(Board_ConditionChecker.isStaleMated(Game.myPlayerId) && DeviceThreadController.isAlreadyPrinted){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    BuzzerAlarm.run();
                    DotPrintCurrentCondition.run(DotPrintCurrentCondition.STALEMATEED);
                }
            };
            timer.schedule(task, 50);
            DeviceThreadController.isAlreadyPrinted = true;
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
                DeviceThreadController.isAlreadyPrinted = true;
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
                DeviceThreadController.isAlreadyPrinted = true;
                return;
            }

        }
    }
}
