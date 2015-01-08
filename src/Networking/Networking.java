package Networking;



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

public final class Networking {
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
    
    public static Networking getInstance(){
        if (instance == null){
            instance = new Networking();
        }
        return instance;
    }

    private Networking() {
        this(Constants.DEFAULT_IFCE, Constants.DEFAULT_MODERAOTR_PORT);
    }

    private Networking(String netIntfceName, int port) {
        this.netIntfceName = netIntfceName;
        tcpModeratorPort = port;
        udpOutData = new byte[1024];
        try {
            UDPSocket = new DatagramSocket();
            moderatorServerSocket = new ServerSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("exception while creating UDP socket");
        } catch (IOException ex) {
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("exception while creating TCP server socket");
        }
        modServer = new ModeratorServer(moderatorServerSocket);
        serverThread = new Thread(modServer);
        serverThread.start();
        //get nessesary information about the network from Network Interface
        setUpByInfceName(netIntfceName); 
        
        
        //broadcast initializing message with port lissened by server socket inside the network
        callPlayers();
        
    }
    
    public void setUpDefault(){
        setUpByInfceName(Constants.DEFAULT_IFCE);
        setModeratorPort(Constants.DEFAULT_MODERAOTR_PORT);
    }
    
    public void setUpByInfceName(String netIntfceName){
        NetworkInterface netIntfce;
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
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("excepton while setting network on interface " + netIntfceName);
        }
    }

    public void setModeratorPort(int tcpModeratorPort) {
        this.tcpModeratorPort = tcpModeratorPort;
    }
    
    private void sendBroadcastUDP(String msg){
        udpOutData = msg.getBytes();
        DatagramPacket sendPacket = 
                new DatagramPacket(udpOutData, udpOutData.length, broadcastAddr, Constants.PEER_UDP_PORT);
        try {
            UDPSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("exception while broadcasting UDP");
        }
        
    }
    
    public void callPlayers(){
        sendBroadcastUDP(BapMessages.MSG_CALL_FOR_PLAYERS + ":PORT-" + tcpModeratorPort);   
    }

    public String getNetIntfceName() {
        return netIntfceName;
    }

    public int getModeratorPort() {
        return tcpModeratorPort;
    }

    
}
