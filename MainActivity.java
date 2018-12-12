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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    // server setting
    private ServerSocket serverSocket;
    private Socket socket;
    private int player_num;
    public static String msg;
    public static StringBuilder serverMsg = new StringBuilder();
    public static StringBuilder clientMsgBuilder = new StringBuilder();
    public static Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>();

    // client setting
    private Socket clientSocket;
    private DataInputStream clientIn;
    public static DataOutputStream clientOut;
    public static String clientMsg;
    public static String nickName;

    public void clientSend() {
        String msg = nickName + " : " + "test!\n"; //transClientText.getText() + "\n";
//        clientMsgBuilder.append(msg);
//        clientText.setText(clientMsgBuilder.toString());
        try {
            clientOut.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverSend() {
        String msg = "Player1 : " + clientsMap.size() + " test!\n"; //transServerText.getText().toString() + "\n";
        serverMsg.append(msg);
        sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msgg) {
            super.handleMessage(msgg);
            switch (msgg.what) {
                case SERVER_TEXT_UPDATE: { // server (Player1) Update Handler
                    serverMsg.append(msg);
                    ///////////////// Refresh Part /////////////////
                    RoomFragment.test_update_display(serverMsg.toString());
                    // serverText.setText(serverMsg.toString());
                }
                break;
                case CLIENT_TEXT_UPDATE: { // client (Player234) Update Handler
                    if (clientMsg.charAt(clientMsg.lastIndexOf('(') + 6) == 'c') { // client name (TAG : (info_client)) // 파싱방식 수정 필요
                        if (nickName == "client") {
                            nickName = "Player" + Character.toString(clientMsg.charAt(clientMsg.lastIndexOf('(') - 1));
                        }
                    }
                    else if (clientMsg.charAt(clientMsg.lastIndexOf('(') + 6) == 'g') { // game status (TAG : (info_game))
                        // TODO board update
                    }
                    else if (clientMsg.charAt(clientMsg.lastIndexOf('(') + 6) == 'm') { // chat status (TAG : (info_mesg))
                        // TODO chat update
                    }
                    else if (clientMsg.charAt(clientMsg.lastIndexOf('(') + 6) == '-') { // other status (Add by needs)
                        // TODO
                    }
                    clientMsgBuilder.append(clientMsg); // MSG LOG DISPLAY (TEST)
                    ///////////////// Refresh Part /////////////////
                    RoomFragment.test_update_display(clientMsgBuilder.toString());
                    // clientText.setText(clientMsgBuilder.toString()); // 메세지 갱신
                }
                break;
            }
        }
    };

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

    public void serverCreate() {
        Collections.synchronizedMap(clientsMap);
        nickName = "Player1";
        try {
            serverSocket = new ServerSocket(port);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        /** XXX 01. 첫번째. 서버가 할일 분담. 계속 접속받는것. */
                        Log.v("", "waiting...");
                        try {
                            socket = serverSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.v("", socket.getInetAddress() + " in.");
                        msg = socket.getInetAddress() + " in.\n";
                        msg += Integer.toString(clientsMap.size());
                        msg += "players";
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
                                    //player_num = clientsMap.size() + 1;
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

    public void sendMessage(String msg) {
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
}
