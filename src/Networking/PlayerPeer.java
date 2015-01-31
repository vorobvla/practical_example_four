/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

import Model.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.cvut.vorobvla.bap.BapMessages;

/**
 * <p> TODO description of Player
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class PlayerPeer implements Runnable{
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
        } catch (IOException ex) {
            Logger.getLogger(PlayerPeer.class.getName()).log(Level.SEVERE, null, ex);
            state = PeerState.DISCONNECTED;
            System.err.println("exception in peer constructor");
        }
        //this.identity = identity;
        
    }

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
    
    
}
