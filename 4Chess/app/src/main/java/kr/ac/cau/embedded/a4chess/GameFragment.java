package kr.ac.cau.embedded.a4chess;

import android.content.Context;
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
        gameStatusView = (TextView) view.findViewById(R.id.game_status);
        queenSideCastlingButton = (Button) view.findViewById(R.id.queen_castling_button);
        queenSideCastlingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO QUUEN SIDE CASTLING
                if(Board_ConditionChecker.getQueenSideCastlingCoordinatePair(Game.myPlayerId) != null){
                    Board.queenSideCastling(Game.myPlayerId);
                    BoardView.view.invalidate();
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
                }
            }
        });

        updateTurn();

        return view;
    }

    public void gameOverLocal(final Player winnerPlayer) {
        gameStatusView.setText(getString(R.string.gameover) + "\n" +
                "Team " + String.valueOf(winnerPlayer.team));
        getActivity().getSharedPreferences("localMatches", Context.MODE_PRIVATE).edit()
                .remove("match_" + Game.match.matchId).commit();
    }

    public void updateTurn() {
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
