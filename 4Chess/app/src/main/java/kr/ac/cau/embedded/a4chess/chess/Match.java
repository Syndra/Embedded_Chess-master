package kr.ac.cau.embedded.a4chess.chess;

public class Match {
    private int numPlayers = 4;
    public final String matchId;

    public Match(final String matchId) {
        this.matchId = matchId;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
