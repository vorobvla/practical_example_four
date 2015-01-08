/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p> TODO description of Server
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class ModeratorServer implements Runnable{
    ServerSocket serverSocket;

    public ModeratorServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while(true){            
            try {
                Socket socket = serverSocket.accept();
                PlayerPeer peer = new PlayerPeer(socket);
                (new Thread(peer)).start();
            } catch (IOException ex) {
                Logger.getLogger(ModeratorServer.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("exception while work of server socket");
            }
        }
    }

}
