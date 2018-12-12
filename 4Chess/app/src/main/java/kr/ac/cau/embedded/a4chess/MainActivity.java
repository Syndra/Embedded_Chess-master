package kr.ac.cau.embedded.a4chess;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kr.ac.cau.embedded.a4chess.chess.Game;
import kr.ac.cau.embedded.a4chess.chess.Match;
import kr.ac.cau.embedded.a4chess.device.DeviceController;

public class MainActivity extends AppCompatActivity {

//    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }

    private static final int SERVER_TEXT_UPDATE = 100;
    private static final int CLIENT_TEXT_UPDATE = 200;
    public static final int port = 9988;
    public static String info_name = new String();
    public static String info_ip = new String();
    public static String client_ip = new String();

    // server setting
    private ServerSocket serverSocket;
    private Socket socket;
    private int player_num;
    public static String msg;
    public static StringBuilder serverMsg = new StringBuilder();
    public static StringBuilder clientMsgBuilder = new StringBuilder();
    public static Map<String, DataOutputStream> clientsMap = new HashMap<>();

    // client setting
    private Socket clientSocket;
    private DataInputStream clientIn;
    private int tokenIndex;
    public static DataOutputStream clientOut;
    public static String clientMsg;
    public static String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, new StartFragment())
                .commit();
    }

    public void changeRoomFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, new RoomFragment())
                .commit();
    }

    public void changeGameFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, new GameFragment())
                .commit();
    }

    public static void clientSend(String sendMsg) {
        String msg = nickName + " : " + sendMsg;
//        clientMsgBuilder.append(msg);
//        clientText.setText(clientMsgBuilder.toString());
        try {
            clientOut.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void serverSend(String sendMsg) {
        String msg = nickName + " : " + sendMsg;
        serverMsg.append(msg);
        sendMessage(msg);
    }

    public String getLocalIpAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = String.format("%d.%d.%d.%d"
                , (ip & 0xff)
                , (ip >> 8 & 0xff)
                , (ip >> 16 & 0xff)
                , (ip >> 24 & 0xff));
        return ipAddress;
    }

    public String getTestLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if(inetAddress instanceof Inet4Address) {
                            return ((Inet4Address)inetAddress).getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void serverCreate() {
        Collections.synchronizedMap(clientsMap);
        nickName = "Player1";
        Game.myPlayerId = "0";
        try {
            serverSocket = new ServerSocket(port);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        // client in
                        Log.v("", "waiting...");
                        try {
                            socket = serverSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.v("", socket.getInetAddress() + " in.");
                        msg = socket.getInetAddress().toString();// + " in.\n";
                        //msg += Integer.toString(clientsMap.size());
                        //msg += "players";
                        player_num = clientsMap.size() + 2;
                        handler.sendEmptyMessage(SERVER_TEXT_UPDATE);

                        new Thread(new Runnable() {
                            private DataInputStream in;
                            private DataOutputStream out;
                            private String nick;
                            private String p_num;

                            @Override
                            public void run() {
                                try { // setting
                                    out = new DataOutputStream(socket.getOutputStream());
                                    in = new DataInputStream(socket.getInputStream());
                                    nick = "Player" + Integer.toString(player_num);
                                    addClient(nick, out);
                                    RoomFragment.clientIP = msg;
                                    RoomFragment.playerIn++; // ★★ TODO call update RoomFrag.
                                    p_num = Integer.toString(player_num);
                                    sendMessage(p_num + "(info_client)");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                try { // keep receiving state
                                    while (in != null) {
                                        msg = in.readUTF();
                                        sendMessage(msg);
                                        handler.sendEmptyMessage(SERVER_TEXT_UPDATE);
                                    }
                                } catch (IOException e) {
                                    // disconnect -> removeClient
                                    removeClient(nick);
                                }
                            }
                        }).start();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String nick, DataOutputStream out) throws IOException {
        clientsMap.put(nick, out);
    }

    public void removeClient(String nick) {
        clientsMap.remove(nick);
    }

    public static void sendMessage(String msg) {
        Iterator<String> it = clientsMap.keySet().iterator();
        String key = "";
        while (it.hasNext()) {
            key = it.next();
            try {
                clientsMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void joinServer(final String ipAddress) {
        if(nickName==null){
            nickName="client";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(ipAddress, port);
                    Log.v("", "Client : Connection Complete.");
                    info_name += "(Connected)";

                    clientOut = new DataOutputStream(clientSocket.getOutputStream());
                    clientIn = new DataInputStream(clientSocket.getInputStream());

                    // client name setting
                    clientOut.writeUTF(nickName);
                    Log.v("", "Client : Data Send.");

                    while (clientIn != null) {
                        try {
                            clientMsg = clientIn.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(CLIENT_TEXT_UPDATE);
                    }
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msgg) {
            super.handleMessage(msgg);
            switch (msgg.what) {
                case SERVER_TEXT_UPDATE: { // server (Player1) Update Handler
                    serverMsg.append(msg);
                    tokenIndex = msg.lastIndexOf('(');
                    if (msg.charAt(tokenIndex + 6) == 'g') { // game status (TAG : (info_game))
                        inputMsgGame(msg.substring(msg.lastIndexOf('P'), tokenIndex));
                    }
                    else if (msg.charAt(tokenIndex + 6) == 'm') { // chat status (TAG : (info_mesg))
                        inputMsgChat(msg.substring(msg.lastIndexOf('P'), tokenIndex));
                    }
                    else if (msg.charAt(tokenIndex + 6) == 'Q') { // other status (Add by needs)
                        inputMsgCast(msg.substring(msg.lastIndexOf('P'), tokenIndex));
                    }
                    ///////////////// Refresh Part /////////////////
                    // RoomFragment.test_update_display(serverMsg.toString());
                    // serverText.setText(serverMsg.toString());
                }
                break;
                case CLIENT_TEXT_UPDATE: { // client (Player234) Update Handler
                    tokenIndex = clientMsg.lastIndexOf('(');
                    if (clientMsg.charAt(tokenIndex + 6) == 'c') { // client name (TAG : (info_client))
                        if (clientMsg.charAt(clientMsg.lastIndexOf('(') - 1) - '0' < 5) {
                            RoomFragment.currentPlayerNum = clientMsg.charAt(clientMsg.lastIndexOf('(') - 1) - '0';
                            RoomFragment.playerStatus();
                        }
                        if (nickName == "client") {
                            nickName = "Player" + Character.toString(clientMsg.charAt(clientMsg.lastIndexOf('(') - 1));
                            Game.myPlayerId = Integer.toString(clientMsg.charAt(clientMsg.lastIndexOf('(') - 1) - '1');
                        }
                        else {
                            if (clientMsg.charAt(clientMsg.lastIndexOf('(') - 1) - '0' == 5) {
                                Match match = new Match(String.valueOf(System.currentTimeMillis()));
                                Game.newGame(match, RoomFragment.playerItems);
                                changeGameFragment();
                            }
                        }
                    }
                    else if (clientMsg.charAt(tokenIndex + 6) == 'g') { // game status (TAG : (info_game))
                        inputMsgGame(clientMsg.substring(clientMsg.lastIndexOf('P'), tokenIndex));
                    }
                    else if (clientMsg.charAt(tokenIndex + 6) == 'm') { // chat status (TAG : (info_mesg))
                        inputMsgChat(clientMsg.substring(clientMsg.lastIndexOf('P'), tokenIndex));
                    }
                    else if (clientMsg.charAt(tokenIndex + 6) == 'Q') { // other status (Add by needs)
                        inputMsgCast(clientMsg.substring(clientMsg.lastIndexOf('P'), tokenIndex));
                    }
                    clientMsgBuilder.append(clientMsg); // MSG LOG DISPLAY (TEST)
                    ///////////////// Refresh Part /////////////////
                    // RoomFragment.test_update_display(clientMsgBuilder.toString());
                    // clientText.setText(clientMsgBuilder.toString()); // 메세지 갱신
                }
                break;
            }
        }
    };

    private static void inputMsgGame(String s) {
        int playerNum = s.charAt(s.indexOf('r') + 1) - '0'; // May calculate by procNum
        String[] tokenizedGameInfo = s.substring(10).split("#"); //Player 1 : 1#5#1#5#3
        int[] gameInfo = new int[5];
        for (int i = 0; i < 5; i++) {
            // gameInfo[0] : procNum, gameInfo[1, 2] : beforeX, Y, gameInfo[3, 4] : afterX, Y
            gameInfo[i] = Integer.parseInt(tokenizedGameInfo[i]);
        }
        ChessFragment.currentPlayer = playerNum - 1;
        ChessFragment.beforeX = gameInfo[1];
        ChessFragment.beforeY = gameInfo[2];
        ChessFragment.afterX = gameInfo[3];
        ChessFragment.afterY = gameInfo[4];
    }

    private static void inputMsgChat(String s) {
        int playerNum = s.charAt(s.indexOf('r') + 1) - '0';
        String chatContent = s.substring(10, s.length()); // Player1 : xxx // 5'r' 6'1' 7' ' 8':' 9' ' 10'x'
        // TODO chat update
        ChatFragment.receivedPlayerNum = playerNum;
        ChatFragment.receivedMessage = chatContent;
    }

    private static void inputMsgCast(String s) {
        GameFragment.castIf = true;
        GameFragment.castUpdate = s.substring(10);
    }
}