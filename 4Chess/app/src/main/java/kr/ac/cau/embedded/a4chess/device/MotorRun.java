package kr.ac.cau.embedded.a4chess.device;

import java.util.Timer;
import java.util.TimerTask;

public class MotorRun {



    public static void run() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            @Override
            public void run()
            {
                DeviceSemaphore.motor_init();
                DeviceController.MotorWrite(0);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DeviceController.MotorWrite(768);
                DeviceSemaphore.motor_deinit();
            }
        };

        timer.schedule(task, 10);
    }
}
