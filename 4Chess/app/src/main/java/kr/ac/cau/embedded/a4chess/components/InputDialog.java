package kr.ac.cau.embedded.a4chess.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.cau.embedded.a4chess.R;

public class InputDialog extends Dialog {

    private TextView titleTextView;
    private EditText editText;
    private Button positiveButton, negativeButton;

    private String title, inputString;

    public InputDialog(Context context, String title) {
        super(context);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_input);

        titleTextView = (TextView)findViewById(R.id.title_text_view);
        titleTextView.setText(title);

        editText = (EditText)findViewById(R.id.edit_text);

        positiveButton = (Button)findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputString = editText.getText().toString();
                if(inputString.length() == 0) {
                    // TODO Alert No Input
                } else {
                    dismiss();
                }
            }
        });
        negativeButton = (Button)findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputString = null;
                dismiss();
            }
        });
    }

    public String getInputString() {
        return inputString;
    }
}
