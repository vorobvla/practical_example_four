/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

import Model.Game;
import Model.GameException;
import Model.Player;
import edu.cvut.vorobvla.bap.BapJSONKeys;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.cvut.vorobvla.bap.BapMessages;
import edu.cvut.vorobvla.bap.PlayerStateEnum;
import java.io.Closeable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * <p> TODO description of Player
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class PlayerPeer implements Runnable, Closeable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String msg;
    private Player player;
    private PeerState state;

    public PlayerPeer(Socket socket) {
        try {
            //fill from socket
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            msg = in.readLine();
            System.out.println("GOT MSG " + msg);
            if (!msg.split(BapMessages.FIELD_DELIM)[0]
                    .matches(BapMessages.MSG_INIT_CONNECTION.split(BapMessages.FIELD_DELIM)[0])){
                state = PeerState.DISCONNECTED;
                throw new IOException("no init msg from player while expected");
            }
            String identity = msg.split(BapMessages.FIELD_DELIM)[1];
            System.out.println("Player " + identity + " have been connected");
            state = PeerState.CONNECTED;
            player = new Player(identity, this);
            out.println(BapMessages.MSG_CONNECTION_EST);
            player.setOnline(true);
        } catch (IOException ex) {
            Logger.getLogger(PlayerPeer.class.getName()).log(Level.SEVERE, null, ex);
            state = PeerState.DISCONNECTED;
            System.err.println("exception in peer constructor");
        }
        //this.identity = identity;
        
    }
    
  /*  public  void terminateConnection(){
        out.println(BapMessages.MSG_CONNECTION_TERM);
    }*/

    @Override
    public void run() {
        
        System.out.println("Peer " + player.getIdentity() + " performs routine");
        while (true){
            try {
                msg = in.readLine();
                switch(msg){
                    case BapMessages.MSG_ANSWER_APPLYING : 
                        player.applyForAnswer();
                        break;
                    case "null" :
                        state = PeerState.DISCONNECTED;
                }
                    
                System.out.println("got msg: '" + msg +  "'" + " from "+ player.getIdentity() + " at " + System.currentTimeMillis());
            } catch (IOException ex) {
                Logger.getLogger(PlayerPeer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
  
    
    public void sendMsg(String msg){
        //sending JSONObject
        /*
        JSONObject obj = Game.getInfoJSON();
        if ((msg.matches(BapMessages.JSON_MSG_ANSWER_ACC)) || 
                (msg.matches(BapMessages.JSON_MSG_ANSWER_ACC))){
            obj.put(BapJSONKeys.KEY_MSG, msg);
        } else {
            obj.put(BapJSONKeys.KEY_MSG, BapMessages.JSON_MSG_GAME_NULL);
        }        */
        out.println(msg);
    }

    @Override
    public void close() throws IOException {
        out.println(BapMessages.MSG_CONNECTION_TERM);
        player.setOnline(false);
    }
    
    
}
