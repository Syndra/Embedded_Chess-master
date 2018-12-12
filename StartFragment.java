package kr.ac.cau.embedded.a4chess;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kr.ac.cau.embedded.a4chess.components.InputDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private Button makeRoomButton, enjoyRoomButton;

    Bundle bundle;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
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
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        makeRoomButton = (Button)view.findViewById(R.id.make_room_button);
        enjoyRoomButton = (Button)view.findViewById(R.id.enjoy_room_button);

        makeRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvName.setText("My IP : ");
                //tvIp.setText(((MainActivity) getActivity()).getLocalIpAddress() + ":9988");
                //bundle = new Bundle(2);
                //bundle.putString("string_name", "My IP : ");
                //bundle.putString("string_ip", ((MainActivity) getActivity()).getLocalIpAddress() + ":9988");
                ((MainActivity) getActivity()).info_name = "My IP : ";
                ((MainActivity) getActivity()).info_ip = ((MainActivity) getActivity()).getLocalIpAddress();

                ((MainActivity) getActivity()).serverCreate();
                ((MainActivity) getActivity()).changeRoomFragment();
            }
        });
        enjoyRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getResources().getString(R.string.input_ip);
                InputDialog dialog = new InputDialog(getContext(), title);
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(((InputDialog)dialog).getInputString() != null) {
                            // TODO Find Room
                            //tvName.setText("Server IP : ");
                            //tvIp.setText(evIp.getText().toString() + ":9988");
                            //bundle = new Bundle(2);
                            //bundle.putString("string_name", "My IP : ");
                            //bundle.putString("string_ip", ((InputDialog) dialog).getInputString() + ":9988");

                            ((MainActivity) getActivity()).info_name = "Server IP : ";
                            ((MainActivity) getActivity()).info_ip = ((InputDialog) dialog).getInputString();

                            ((MainActivity) getActivity()).joinServer(((InputDialog) dialog).getInputString());
                            ((MainActivity) getActivity()).changeRoomFragment();

                        } else {
                            dialog.cancel();
                        }
                    }
                });
            }
        });
        return view;
    }
}
