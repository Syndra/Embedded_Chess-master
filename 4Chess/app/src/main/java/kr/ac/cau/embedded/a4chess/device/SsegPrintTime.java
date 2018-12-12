package kr.ac.cau.embedded.a4chess.device;

import java.util.Timer;
import java.util.TimerTask;

public class SsegPrintTime {

    int leftTime;
    TimerTask task;

    public SsegPrintTime(int leftTime)
    {
        this.leftTime = leftTime;
    }

    public void run(final int leftTime) {
        Timer timer = new Timer();

        task = new TimerTask(){
            @Override
            public void run()
            {
                DeviceController.SSegmentWrite(leftTime);
                downTime();
            }
        };

        timer.schedule(task, 0, 1000);
    }

    public void downTime()
    {
        leftTime--;
        if(leftTime < 0)
            this.task.cancel();
    }
}
