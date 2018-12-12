package kr.ac.cau.embedded.a4chess;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.ac.cau.embedded.a4chess.Chat.ChatMessage;
import kr.ac.cau.embedded.a4chess.Chat.MessageAdapter;
import kr.ac.cau.embedded.a4chess.device.DeviceController;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private ListView listView;
    private Button btnSend;
    private Button btnReceive;

    private List<ChatMessage> chatMessages;
    private ArrayAdapter<ChatMessage> adapter;

    private String message;
    private int oldPlayerNum = -1;
    private String oldMessage = "\0";
    public static int receivedPlayerNum = -1;
    public static String receivedMessage = "\0";

    public ChatFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        final LinearLayout shortcutText = (LinearLayout) view.findViewById(R.id.shortcut_text);
        final Button slideBtn = (Button) view.findViewById(R.id.slide_btn);
        slideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shortcutText.getVisibility() == View.VISIBLE) {
                    shortcutText.setVisibility(View.GONE);
                    slideBtn.setText("SHOW");
                } else {
                    shortcutText.setVisibility(View.VISIBLE);
                    slideBtn.setText("HIDE");
                }
            }
        });
        chatMessages = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.chat_list);
        adapter = new MessageAdapter(getActivity(), R.layout.fragment_chat, chatMessages);
        listView.setAdapter(adapter);

        new Thread(new Runnable() { @Override public void run() {
            while(true){
                try{
                    int clicked_button = DeviceController.PushbuttonRead();
                    sendMessage(clicked_button);
                    Thread.sleep(200);
                }
                catch (Exception e){

                }
                try{
                    receiveMessage();
                }
                catch (Exception e){

                }
            }
        }
        }).start();

        return view;
    }

    private void receiveMessage() {
        if (oldPlayerNum == receivedPlayerNum && oldMessage.equals(receivedMessage)) {
            return;
        }

        Log.d("chat test", "" + oldPlayerNum);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.nickName.charAt(6) != '0' + receivedPlayerNum) {
                    oldPlayerNum = receivedPlayerNum;
                    oldMessage = receivedMessage;
                    ChatMessage chatMessage = new ChatMessage("Player" + Integer.toString(receivedPlayerNum) + " : " + receivedMessage, false);
                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void sendMessage(int macro){

        Log.d("macro", "" + macro);
        if(macro < 1) {
            return ;
        }

        message = new String("");
        if(macro == 1) {
            message = getResources().getString(R.string.macro1);
            message = message.substring(2,message.length());
        }
        else if(macro == 10) {
            message = getResources().getString(R.string.macro2);
            message = message.substring(2,message.length());
        }
        else if(macro == 100) {
            message = getResources().getString(R.string.macro3);
            message = message.substring(2,message.length());
        }
        else if(macro == 1000) {
            message = getResources().getString(R.string.macro4);
            message = message.substring(2,message.length());
        }
        else if(macro == 10000) {
            message = getResources().getString(R.string.macro5);
            message = message.substring(2,message.length());
        }
        else if(macro == 100000) {
            message = getResources().getString(R.string.macro6);
            message = message.substring(2,message.length());
        }
        else if(macro == 1000000) {
            message = getResources().getString(R.string.macro7);
            message = message.substring(2,message.length());
        }
        else if(macro == 10000000) {
            message = getResources().getString(R.string.macro8);
            message = message.substring(2,message.length());
        }
        else if(macro == 100000000) {
            message = getResources().getString(R.string.macro9);
            message = message.substring(2,message.length());
        }

        if (MainActivity.nickName == "Player1") {
            ((MainActivity) getActivity()).serverSend(message + "(info_mesg)");
        }
        else {
            ((MainActivity) getActivity()).clientSend(message + "(info_mesg)");
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatMessage chatMessage = new ChatMessage(MainActivity.nickName + " : " + message, true);
                chatMessages.add(chatMessage);
                adapter.notifyDataSetChanged();
            }
        });

    }
}