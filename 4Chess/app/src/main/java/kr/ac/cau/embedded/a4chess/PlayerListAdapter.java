package kr.ac.cau.embedded.a4chess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.cau.embedded.a4chess.chess.Player;

public class PlayerListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Player> playerItems;
    private int layout;

    public PlayerListAdapter(Context context, int layout, ArrayList<Player> playerItems) {
        this.context = context;
        this.layout = layout;
        this.playerItems = playerItems;
        this.layoutInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return playerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return playerItems.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if(convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);
        }
        TextView idText = (TextView)convertView.findViewById(R.id.player_id);
        idText.setText(playerItems.get(pos).name);
        TextView ipText = (TextView)convertView.findViewById(R.id.player_ip);
        ipText.setText(playerItems.get(pos).ip);

        return convertView;
    }
}
