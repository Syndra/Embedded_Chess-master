package kr.ac.cau.embedded.a4chess.device.PushButton;

import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button1Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button2Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button3Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button4Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button5Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button6Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button7Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button8Action;
import kr.ac.cau.embedded.a4chess.device.PushButton.PushActions.Button9Action;

public class ButtonActionSetter {

    static PushButtonAction pushButtonAction;

    public static void init(){
        pushButtonAction = new PushButtonAction();

        pushButtonAction.setButtonAction(0, new Button1Action());
        pushButtonAction.setButtonAction(1, new Button2Action());
        pushButtonAction.setButtonAction(2, new Button3Action());
        pushButtonAction.setButtonAction(3, new Button4Action());
        pushButtonAction.setButtonAction(4, new Button5Action());

        pushButtonAction.setButtonAction(5, new Button6Action());
        pushButtonAction.setButtonAction(6, new Button7Action());
        pushButtonAction.setButtonAction(7, new Button8Action());
        pushButtonAction.setButtonAction(8, new Button9Action());

        pushButtonAction.run();
    }
}
