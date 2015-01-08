/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

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
    private String identity;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String msg;

    public PlayerPeer(Socket socket) {
        try {
            //fill from socket
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            msg = in.readLine();
            System.out.println("GOT MSG " + msg);
            if (!msg.split(":")[0].matches(BapMessages.MSG_INIT_CONNECTION.split(":")[0])){
                throw new IOException("no init msg from serwer while expected");
            }
            identity = msg.split(":")[1].split("-")[1];
            System.out.println("Player " + identity + " have been connected");
            out.println(BapMessages.MSG_CONNECTION_EST);
        } catch (IOException ex) {
            Logger.getLogger(PlayerPeer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("exception in peer constructor");
        }
        //this.identity = identity;
        
    }

    @Override
    public void run() {
        System.out.println("Peer " + identity + " performs routine");
    }
    
}
