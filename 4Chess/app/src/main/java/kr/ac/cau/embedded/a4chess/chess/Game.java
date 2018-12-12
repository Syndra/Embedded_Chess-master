package kr.ac.cau.embedded.a4chess.chess;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import kr.ac.cau.embedded.a4chess.GameFragment;

public class Game {
    public static Match match;
    public static Player[] players;
    public static int turns;
    public static String myPlayerId;

    public static GameFragment UI;

    private static List<String> deadPlayers;

    public static int getPlayerColor(String id) {
        return getPlayer(id).color;
    }

    public static Player getPlayer(final String id) {
        for (Player player : players) {
            if (player.id.equals(id)) {
                return player;
            }
        }
        return null;
    }

    public static boolean isAlivePlayer(String id) {
        if(deadPlayers.contains(id) == false) {
            return true;
        }
        return false;
    }

    public static void newGame(final Match match, final ArrayList<Player> playerList) {
        Game.match = match;
        turns = 0;
        deadPlayers = new LinkedList<String>();
        createPlayers(playerList);
        Board.newGame(players);
    }

    private static void createPlayers(final ArrayList<Player> playerList) {
        int numPlayers = match.getNumPlayers();
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = playerList.get(i);
        }
    }

    public static boolean removePlayer(final String playerId) {
        if (players.length > 2) Board.removePlayer(playerId);
        deadPlayers.add(playerId);
        return isGameOver();
    }

    public static void moved() {
        turns++;
        String next = players[turns % players.length].id;
        while (deadPlayers.contains(next)) {
            turns++; // skip dead players
            next = players[turns % players.length].id;
        }
        if (next.startsWith("AutoMatch_")) {
            next = null;
        }
        if (UI != null) {
            UI.updateTurn();
        }
    }

    public static void over() {
        if (UI != null) {
            UI.gameOverLocal(getWinner());
        }
    }

    public static boolean isGameOver() {
        return (players.length - deadPlayers.size() <= 1) ||
                (deadPlayers.size() == 2 && sameTeam(deadPlayers.get(0), deadPlayers.get(1)));
    }

    public static String currentPlayer() {
        return players[turns % players.length].id;
    }

    public static boolean myTurn() {
        //return match.isLocal || myPlayerId.equals(players[turns % players.length].id);
        return true;
    }

    public static boolean isValidTurn(String PlayerId) {
//        if(PlayerId.equals(Game.currentPlayer())) { // local test
        if(PlayerId.equals(Game.currentPlayer())) { // in Network
            return true;
        }
        return false;
    }

    public static boolean sameTeam(final String id1, final String id2) {
        return getPlayer(id1).team == getPlayer(id2).team;
    }

    private static Player getWinner() {
        for (Player p : players) {
            if (!deadPlayers.contains(p.id)) {
                return p;
            }
        }
        return null;
    }
}
