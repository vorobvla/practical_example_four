package Networking;



import Model.Game;
import edu.cvut.vorobvla.bap.BapMessages;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.cvut.vorobvla.bap.BapMessages;
import edu.cvut.vorobvla.bap.BapPorts;
import edu.cvut.vorobvla.bap.GameStateEnum;
import java.io.Closeable;
import javax.swing.ListModel;
import javax.swing.text.Document;
import sun.security.jca.GetInstance;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * <p> Networking for moderator application
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public final class Networking implements Closeable{
    private String netIntfceName;
    private NetworkInterface netIntfce;
    private InetAddress moderatorAddr;
    private InetAddress broadcastAddr = null;
    private byte[] udpOutData;
 //   private byte[] udpInData;
    private DatagramSocket UDPSocket;
    private int tcpModeratorPort;
    private ServerSocket moderatorServerSocket;
    private Thread serverThread;
    private ModeratorServer modServer;
    private static Networking instance;
    //private static ListModel listModel;
    
    public static Networking getInstance(){
        if (instance == null){
            instance = new Networking();
        }
        return instance;
    }


    private Networking() {
        this.netIntfceName = Constants.DEFAULT_IFCE;
        tcpModeratorPort = Constants.DEFAULT_MODERAOTOR_PORT;
        udpOutData = new byte[1024];
        
        try {
            UDPSocket = new DatagramSocket();
            //moderatorServerSocket = new ServerSocket(tcpModeratorPort);
        } catch (SocketException ex) {
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("exception while creating UDP socket");
        }
        
    }
    
    public void setUpDefault() throws IOException{
        setUpByInfceName(Constants.DEFAULT_IFCE);
        setModeratorPort(Constants.DEFAULT_MODERAOTOR_PORT);
    }
    
    public void setUpByInfceName(String netIntfceName) throws RuntimeException{
        try {
            netIntfce = NetworkInterface.getByName(netIntfceName);
            for (InterfaceAddress interfaceAddress : netIntfce.getInterfaceAddresses()) {
                moderatorAddr = interfaceAddress.getAddress();
                broadcastAddr = interfaceAddress.getBroadcast();
                if (broadcastAddr != null){
                    break;          
                }
            }
        } catch (SocketException ex) {
            throw new RuntimeException("UDP socket failure", ex);
        }
    }

    public void setModeratorPort(int tcpModeratorPort) throws RuntimeException {
        try {
            this.tcpModeratorPort = tcpModeratorPort;
            moderatorServerSocket = new ServerSocket(tcpModeratorPort);
            moderatorServerSocket.setReuseAddress(false);
            modServer = new ModeratorServer(moderatorServerSocket);
            //run server
            serverThread = new Thread(modServer);
            serverThread.start();
        } catch (IOException ex) {
            throw new RuntimeException("Moderator socket failure", ex);
        }
    }
    
    
    
    private void sendBroadcastUDP(String msg){
        udpOutData = msg.getBytes();
        for (int port = BapPorts.PLAYER_PORT; port < BapPorts.PLAYER_PORT + 
                BapPorts.PLAYER_PORT_RANGE; port++) {
            DatagramPacket sendPacket = 
                    new DatagramPacket(udpOutData, udpOutData.length, broadcastAddr, port);
            try {
                UDPSocket.send(sendPacket);
            } catch (IOException ex) {
                Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("exception while broadcasting UDP");
            }
        }
    }
    
    public void callPlayers(){
        sendBroadcastUDP(BapMessages.MSG_CALL_FOR_PLAYERS + BapMessages.FIELD_DELIM + tcpModeratorPort);   
    }

    public String getNetIntfceName() {
        return netIntfceName;
    }

    public int getModeratorPort() {
        return tcpModeratorPort;
    }
    
     

    @Override
    public void close() throws IOException {
        //todo kill all peers
//        moderatorServerSocket.close();
        UDPSocket.close();
    }

  /*  public synchronized void broadcastGame(){
        new Thread(){

            @Override
            public void run() {
                while(Game.getState() != GameStateEnum.FINISHED){
                    try {
                        sendBroadcastUDP(Game.getInfoJSON());
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        
        }.start();
    }*/
}
