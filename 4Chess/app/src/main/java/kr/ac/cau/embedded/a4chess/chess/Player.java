package kr.ac.cau.embedded.a4chess.chess;

import android.util.Pair;

public class Player {
    public final String id;
    public final int team;
    public final int color;
    public final String name;
    public final String ip;
    public Pair<Coordinate, Coordinate> lastMove;

    public Player(String id, int team, int color, String name, String ip) {
        this.id = id;
        this.team = team;
        this.color = color;
        this.name = name;
        this.ip = ip;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Player && ((Player) other).id.equals(id);
    }
}
