package kr.ac.cau.embedded.a4chess.device;

import java.util.Timer;
import java.util.TimerTask;

public class BuzzerAlarm {

    static public void run() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            @Override
            public void run()
            {
                DeviceSemaphore.buzzer_init();
                DeviceController.BuzzerWrite(1);
                DeviceSemaphore.buzzer_deinit();
            }
        };

        timer.schedule(task, 10);
    }
}