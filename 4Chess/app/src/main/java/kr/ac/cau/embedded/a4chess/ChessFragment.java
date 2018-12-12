package kr.ac.cau.embedded.a4chess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.cau.embedded.a4chess.chess.Board;
import kr.ac.cau.embedded.a4chess.chess.Coordinate;
import kr.ac.cau.embedded.a4chess.chess.Game;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ChessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChessFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private int beforePlayer = -2;
    public static int currentPlayer = -2;
    public static int beforeX, beforeY;
    public static int afterX, afterY;

    private View boardView;

    public ChessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChessFragment newInstance(String param1, String param2) {
        ChessFragment fragment = new ChessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chess, container, false);
        boardView = (View) view.findViewById(R.id.frag_board);
        new Thread(new Runnable() { @Override public void run() {
            while(true){
                try{
                    board_update_display();
                    Thread.sleep(200);
                }
                catch (Exception e){

                }
            }
        }
        }).start();

        return view;
    }

    private void board_update_display() {
        if (beforePlayer == currentPlayer) {
            return;
        }
        beforePlayer = currentPlayer;
        Coordinate selection = new Coordinate(beforeX, beforeY);
        Coordinate coordinate = new Coordinate(afterX, afterY);
        Board.move(Game.currentPlayer(), selection, coordinate);
    }
}
