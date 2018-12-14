package kr.ac.cau.embedded.a4chess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import kr.ac.cau.embedded.a4chess.chess.Board;
import kr.ac.cau.embedded.a4chess.chess.Board_ConditionChecker;
import kr.ac.cau.embedded.a4chess.chess.Game;
import kr.ac.cau.embedded.a4chess.chess.Player;
import kr.ac.cau.embedded.a4chess.device.DeviceController;
import kr.ac.cau.embedded.a4chess.device.DotPrintCurrentCondition;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private TextView gameStatusView;
    private static Button queenSideCastlingButton;
    private static Button kingSideCastlingButton;

    public static Activity GameActivity = null;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_chess, new ChessFragment())
                .commit();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_chatting, new ChatFragment())
                .commit();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Game.UI = this;
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        GameFragment.GameActivity = getActivity();

        gameStatusView = (TextView) view.findViewById(R.id.game_status);
        queenSideCastlingButton = (Button) view.findViewById(R.id.queen_castling_button);
        queenSideCastlingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO QUUEN SIDE CASTLING
                if(Board_ConditionChecker.getQueenSideCastlingCoordinatePair(Game.myPlayerId) != null){
                    Board.queenSideCastling(Game.myPlayerId);
                    BoardView.view.invalidate();
                    String castMsg = "Q" + Game.myPlayerId + "(info_QKca)";
                    if (MainActivity.nickName == "Player1") {
                        MainActivity.serverSend(castMsg);
                    }
                    else {
                        MainActivity.clientSend(castMsg);
                    }
                }
            }
        });
        kingSideCastlingButton = (Button) view.findViewById(R.id.king_castling_button);
        kingSideCastlingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO KING SIDE CASTLING
                if(Board_ConditionChecker.getKingSideCastlingCoordinatePair(Game.myPlayerId) != null) {
                    Board.kingSideCastling(Game.myPlayerId);
                    BoardView.view.invalidate();
                    String castMsg = "K" + Game.myPlayerId + "(info_QKca)";
                    if (MainActivity.nickName == "Player1") {
                        MainActivity.serverSend(castMsg);
                    }
                    else {
                        MainActivity.clientSend(castMsg);
                    }
                }
            }
        });

        return view;
    }

    public void RunStaleMate() {
        if(GameFragment.GameActivity != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());

                    // set title
                    alertDialogBuilder.setTitle("DRAW! STALEMATE!");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Go To Home?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    ((MainActivity) getActivity()).finish();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });
        }
    }

    public void gameOverLocal(final Player winnerPlayer) {

        int winnerTeam = 0;

        for(Player elem : Game.players)
        {
            if(!Game.deadPlayers.contains(elem))
                winnerTeam = elem.team;
        }

        if(Game.getPlayer(Game.myPlayerId).team != winnerTeam)
        {
            DotPrintCurrentCondition.run(DotPrintCurrentCondition.LOSE);
        }
        else {
            DotPrintCurrentCondition.run(DotPrintCurrentCondition.WIN);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                // set title
                alertDialogBuilder.setTitle("Game Over");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Go To Home?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                ((MainActivity) getActivity()).finish();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    public void updateTurn() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                String current = Game.players[Game.turns % Game.players.length].id;
                for (Player p : Game.players) {
                    stringBuilder.append("<font color='")
                            .append(String.format("#%06X", (0xFFFFFF & Game.getPlayerColor(p.id))))
                            .append("'>");
                    if (p.id.equals(current)) stringBuilder.append("-> ");
                    stringBuilder.append(p.name).append(" [").append(p.team).append("]</font><br />");
                }
                stringBuilder.delete(stringBuilder.lastIndexOf("<br />"), stringBuilder.length());
                gameStatusView.setText(Html.fromHtml(stringBuilder.toString()));
                gameStatusView.postInvalidate();
            }
        });
    }

    // King Castling Button Change Visibility
    public static void changeVisibleKingSideCastling() {
        if(kingSideCastlingButton.getVisibility() == View.INVISIBLE) {
            kingSideCastlingButton.setVisibility(View.VISIBLE);
        } else if(kingSideCastlingButton.getVisibility() == View.VISIBLE) {
            kingSideCastlingButton.setVisibility(View.INVISIBLE);
        }
    }

    // Queen Castling Button Change Visibility
    public static void changeVisibleQueenSideCastling() {
        if(queenSideCastlingButton.getVisibility() == View.INVISIBLE) {
            queenSideCastlingButton.setVisibility(View.VISIBLE);
        } else if(queenSideCastlingButton.getVisibility() == View.VISIBLE) {
            queenSideCastlingButton.setVisibility(View.INVISIBLE);
        }
    }
}
