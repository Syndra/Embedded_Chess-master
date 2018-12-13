package kr.ac.cau.embedded.a4chess.device;

import java.util.Timer;
import java.util.TimerTask;

import kr.ac.cau.embedded.a4chess.BoardView;
import kr.ac.cau.embedded.a4chess.MainActivity;
import kr.ac.cau.embedded.a4chess.chess.Board;
import kr.ac.cau.embedded.a4chess.chess.Coordinate;
import kr.ac.cau.embedded.a4chess.chess.Game;

public class SsegPrintTime {

    static volatile int leftTime;
    TimerTask task;

    public SsegPrintTime(int leftTime)
    {
        this.leftTime = leftTime;
    }

    public void run() {
        Timer timer = new Timer();

        task = new TimerTask(){
            @Override
            public void run()
            {
                downTime();
            }
        };

        timer.schedule(task, 2, 1000);
    }

    public void downTime()
    {
        if(this.leftTime > 0)
            this.leftTime--;
        if(this.leftTime == 10 && Game.myPlayerId.equals(Game.currentPlayer()))
            MotorRun.run();
        if(this.leftTime == 0 && Game.currentPlayer().equals(Game.myPlayerId))
        {
            this.leftTime = -10;
            Board.move(Game.myPlayerId, new Coordinate(-1000, 0), new Coordinate(0,0));
            BoardView.procNum++;
            String gameMsg = Integer.toString(BoardView.procNum) + "#";
            gameMsg += Integer.toString(-1000) + "#" + Integer.toString(0) + "#";
            gameMsg += Integer.toString(0) + "#" + Integer.toString(0) + "(info_game)";
            if (MainActivity.nickName == "Player1") {
                MainActivity.serverSend(gameMsg);
            }
            else {
                MainActivity.clientSend(gameMsg);
            }
            //invalidate();
        }
    }

    public void setTime(int leftTime){

       this.leftTime = leftTime;
    }
}
