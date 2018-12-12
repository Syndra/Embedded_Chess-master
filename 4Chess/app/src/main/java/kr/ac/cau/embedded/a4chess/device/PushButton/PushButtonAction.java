package kr.ac.cau.embedded.a4chess.device.PushButton;

import java.util.Timer;
import java.util.TimerTask;

import kr.ac.cau.embedded.a4chess.device.DeviceController;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.ActionAdapter;

public class PushButtonAction {

    public ActionAdapter[] actionlist = new ActionAdapter[9];

    public PushButtonAction()
    {

    }

    public void run() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            @Override
            public void run()
            {
                int pushedButton = DeviceController.PushbuttonRead();
                for(int i = 0; i < 9; i++){
                    int isPushed = pushedButton%10;
                    pushedButton /= 10;
                    if(isPushed == 1) {
                        actionlist[i].doAction();
                    }
                }
            }
        };

        timer.schedule(task, 0, 100);
    }

    public void setButtonAction(int button_num, ActionAdapter action)
    {
        actionlist[button_num] = action;
    }
}
