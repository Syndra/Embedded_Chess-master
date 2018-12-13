package kr.ac.cau.embedded.a4chess;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.cau.embedded.a4chess.chess.Game;
import kr.ac.cau.embedded.a4chess.chess.Match;
import kr.ac.cau.embedded.a4chess.chess.Player;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private final static int[] PLAYER_COLOR = {
            Color.parseColor("#FF8800"),
            Color.parseColor("#99CC00"),
            Color.parseColor("#33B5E5"),
            Color.parseColor("#CC0000")
    };

    public static ArrayList<Player> playerItems;
    private static Integer playerNum = 0;
    public static Integer playerIn = 1;
    public static String clientIP;
    public static int currentPlayerNum;

    private static PlayerListAdapter playerListAdapter;
    private ListView listView;
    private static Button startButton;
    private static TextView roomName;

    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance(String param1, String param2) {
        RoomFragment fragment = new RoomFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerItems = new ArrayList<Player>();
        if (MainActivity.nickName == "Player1") {
            Player roomMaster = new Player(playerNum.toString(), playerNum / 2,
                    PLAYER_COLOR[playerNum], MainActivity.nickName, ((MainActivity) getActivity()).info_ip);
            playerItems.add(roomMaster);
            playerNum++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        roomName = (TextView)view.findViewById(R.id.room_name);
        if (MainActivity.nickName == "Player1") {
            roomName.setText(" " + playerItems.get(0).ip);
        }

        playerListAdapter = new PlayerListAdapter(view.getContext(), R.layout.context_player, playerItems);
        listView = (ListView)view.findViewById(R.id.player_list);
        listView.setAdapter(playerListAdapter);

        Button testButton = (Button)view.findViewById(R.id.test_button);

        new Thread(new Runnable() { @Override public void run() {
            while(true){
                try{
                    player_update_display();
                    Thread.sleep(200);
                }
                catch (Exception e){

                }
            }
        }
        }).start();

        testButton.setOnClickListener(new View.OnClickListener() { // Test
            @Override
            public void onClick(View v) { //★★ TODO : May be refresh button OR Update Directly
                //((MainActivity) getActivity()).clientSend("test(info_mesg)"); // simple test
                if(playerItems.size() < 4) {
                    Player player = new Player(playerNum.toString(), playerNum / 2,
                            PLAYER_COLOR[playerNum], "Player" + (++playerNum), clientIP.substring(1));
                    playerItems.add(player);
                    playerListAdapter.notifyDataSetChanged();
                    MainActivity.serverSend(playerNum.toString() + "(info_client)");
                }
                if(playerItems.size() == 4) {
                    startButton.setVisibility(View.VISIBLE);
                }
            }
        });

        startButton = (Button)view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.serverSend("5(info_client)");
                Match match = new Match(String.valueOf(System.currentTimeMillis()));
                Game.newGame(match, playerItems);
                ((MainActivity) getActivity()).changeGameFragment();
            }
        });
        startButton.setVisibility(View.GONE);

        return view;
    }

    private void player_update_display() {
        // TODO update listview
        //Log.d("room test", "" + playerIn + "" + playerNum);
        if (MainActivity.nickName == "Player1") {
            if (playerIn <= playerNum) {
                return;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Player player = new Player(playerNum.toString(), playerNum / 2,
                            PLAYER_COLOR[playerNum], "Player" + (++playerNum), clientIP.substring(1));
                    playerItems.add(player);
                    playerListAdapter.notifyDataSetChanged();
                    MainActivity.serverSend(playerNum.toString() + "(info_client)");

                    if(playerItems.size() == 4 && MainActivity.nickName == "Player1") {
                        startButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        else {

        }
    }

    public static void playerStatus() {
        if(playerItems.size() != currentPlayerNum) {
            for(int i = playerItems.size(); i < currentPlayerNum; i++) {
                Player player = new Player(playerNum.toString(), playerNum / 2,
                        PLAYER_COLOR[playerNum], "Player" + (++playerNum), " ");
                playerItems.add(player);
                playerListAdapter.notifyDataSetChanged();
            }
        }
        roomName.setText(" " + MainActivity.info_ip);
        // roomName.setText(roomName.getText() + " " + Integer.toString(currentPlayerNum) + " Players Now");
    }
}
