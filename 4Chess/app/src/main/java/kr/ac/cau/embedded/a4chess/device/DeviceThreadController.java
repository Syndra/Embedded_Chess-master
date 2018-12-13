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
                    if(!DeviceThreadController.isAlreadyPrinted) {
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
                    else{
                        isAlreadyPrinted = false;
                    }
                }
            };

            timer.schedule(task, 10,100);
        }
}
