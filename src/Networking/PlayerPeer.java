/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

import Model.Game;
import Model.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.cvut.vorobvla.bap.BapMessages;
import static edu.cvut.vorobvla.bap.BapMessages.*;
import edu.cvut.vorobvla.bap.GameStateEnum;
import java.io.Closeable;


/**
 * <p> Provides network communication with an instance of player application (peer)
 * based on TCP protocol. 
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class PlayerPeer implements Runnable, Closeable{
    /** {@code Socket} is used for network communication with peer based on TCP
     * protocol. */
    private Socket socket;
    /** Handles input from {@see #socket}. */
    private BufferedReader in;
    /** Handles output to {@see #socket}. */
    private PrintWriter out;
    /** Used as a buffer for input and output messages. */
    private String msg;
    /** Represents connected player in the context of game. */
    private Player player;
    /** Sate of the peer in the context of network communication. */
    private PeerState state;

    /**
     * Constructs a {@see #PlayerPeer} object with specified socket.
     * After verifying connection (getting 
     * {@see edu.cvut.vorobvla.bap.BapMessages#MSG_INIT_CONNECTION} from
     * peer) and getting from peer the player identity initializes 
     * {@see #player} as a new {@see Model.Player} object with 
     * {@see Model.Player#identity} set to the received value.
     * Then sets {@see #state} to {@see PeerState#CONNECTED} and sends peer conformation of connection 
     * ({@see edu.cvut.vorobvla.bap.BapMessages#MSG_CONNECTION_EST}).
     * @param socket 
     */
    public PlayerPeer(Socket socket) {
        try {
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

    /**
     * Active listens for peer. Calls {@see Player.applyForAnswer} of 
     * {@see #player} when receives
     * {@see edu.cvut.vorobvla.bap.BapMessages#MSG_ANSWER_APPLYING} (sent by
     * player application when player applies for answer). Calls 
     * {@see Model.Game#finishGame()} when receives
     * {@see edu.cvut.vorobvla.bap.BapMessages#MSG_CONNECTION_TERM} (peer
     * terminates connection).
     */
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
                    case BapMessages.MSG_CONNECTION_TERM :
                        state = PeerState.DISCONNECTED;
                        Game.getInstance().finishGame();
                        Player.getAll().remove(player);
                        player = null;
                        return;
                }
                    
                System.out.println("got msg: '" + msg +  "'" + " from "+ player.getIdentity() + " at " + System.currentTimeMillis());
            } catch (IOException ex) {
                Logger.getLogger(PlayerPeer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
  
    /**
     * Sends the specified message to peer.
     * @param msg desired message.
     */
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

    /**
     * Sends {@see edu.cvut.vorobvla.bap.BapMessages#MSG_CONNECTION_TERM}
     * to peer.
     * @throws IOException 
     */
    @Override
    public void close() throws IOException {
        out.println(BapMessages.MSG_CONNECTION_TERM);
        player.setOnline(false);
    }   
    
    public void sendOpts(boolean broadcast){
        String optBroadcast = OPT_ON;
        if (broadcast == false){
            optBroadcast = OPT_OFF;
        }
        sendMsg(MSG_OPT_BROADCAST + FIELD_DELIM + optBroadcast);
    }
}
