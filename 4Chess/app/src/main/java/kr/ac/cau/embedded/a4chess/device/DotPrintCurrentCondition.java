package kr.ac.cau.embedded.a4chess.device;

import java.util.Timer;
import java.util.TimerTask;

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
}
