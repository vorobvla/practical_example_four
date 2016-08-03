package Networking;



import Model.Game;
import edu.cvut.vorobvla.bap.BapJSONKeys;
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
import java.io.Closeable;
import org.json.simple.JSONObject;



/**
 * <p> Enables communication of the whole application with
 * player applications via local network. This communication uses
 * both  TCP and UDP protocols. TCP is used for direct communication 
 * with players (from this point of view moderator behaves like a typical 
 * TCP server). UDP is used for broadcasting data that the players need for
 * establishing connection with server and information about the game.
 * <p> The communication protocol is described in documentation
 * of {@code CommonLib} (see {@see edu.cvut.vorobvla.bap.BapMessages}).
 * <p> This class is implemented as a singleton.
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public final class Networking implements Closeable{
    /** Name of network interface connected to the network that is used for
     * communication with player applications. */
    private String netIntfceName;
    /** Network interface connected to the network used for
     * communication with player application. */
    private NetworkInterface netIntfce;
    /** Broadcast address of the local network used for broadcasting
     *  data to player applications. {@code null} before this address is found out
     * and set.
     */
    private InetAddress broadcastAddr = null;
    /** A {@code DatagramSocket} used for network communication based on UDP
     * protocol */
    private DatagramSocket UDPSocket;
    /** Default number of port used by TCP server socket for listening. */
    private int tcpModeratorPort;
    /** A {@code ServerSocket} that listens for clients initiating TCP 
     * connections via local network. */
    private ServerSocket moderatorServerSocket;
    /** A thread which will run the server's active listening for the clients. */
    private Thread serverThread;
    /** A runnable object that implements the {@see #serverThread} routine. */
    private ModeratorServer modServer;
    /** If broadcasting of the game information is enabled. If the value is true
     broadcasting is enabled, otherwise it is disabled. */
    private boolean broadcastGameOpt;
    /** The  single instance of {@see Networking} object (typical for 
     * singleton implementation). */    
    private static Networking instance;
    //private static ListModel listModel;
    
    /**
     * Typical singleton method to get the only instance of this object.
     * @return the only instance of {@see #Networking} object.
     */
    public static Networking getInstance(){
        if (instance == null){
            instance = new Networking();
        }
        return instance;
    }

    /**
     * Constructs the {@see #Networking} object. Sets {@see #netIntfceName}
     * and {@see #tcpModeratorPort} to their default values from 
     * {@see Networking.Constants}. Sets {@see broadcastGameOpt} to
     * {@code false} and initializes {@see #UDPSocket}.
     */
    private Networking() {
        this.netIntfceName = Constants.DEFAULT_IFCE;
        tcpModeratorPort = Constants.DEFAULT_MODERAOTOR_PORT;
        
        try {
            UDPSocket = new DatagramSocket();
            //moderatorServerSocket = new ServerSocket(tcpModeratorPort);
        } catch (SocketException ex) {
            Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("exception while creating UDP socket");
        }
        
        broadcastGameOpt = false;
    }
    
    //deafult setup network
    /**
     * Sets up networking to defaults given in {@see Networking.Constants}.
     * @throws RuntimeException 
     */
    public void setUpDefault() throws RuntimeException{
        setUpByInfceName(Constants.DEFAULT_IFCE);
        setModeratorPort(Constants.DEFAULT_MODERAOTOR_PORT);
    }
    
    //get network interface by name and find a broadcast addres for network  
    /**
     * Sets {@see #netIntfce} to a {@code NetworkInterface} object that 
     * represents network 
     * interface with specified name. Then finds the broadcast address of 
     * network connected with this interface and sets {@see #broadcastAddr} 
     * to this address.
     * @param netIntfceName the name of the interface connected to the network
     * which is supposed to be used for communication.
     * @throws RuntimeException with {@code message} {@code "Network interface setup failure"}
     * if a {@code SocketException} occurred (can be thrown by 
     * {@code NetworkInterface.getByName()}).
     */
    public void setUpByInfceName(String netIntfceName) throws RuntimeException{
        try {
            netIntfce = NetworkInterface.getByName(netIntfceName);
            for (InterfaceAddress interfaceAddress : netIntfce.getInterfaceAddresses()) {
                broadcastAddr = interfaceAddress.getBroadcast();
                if (broadcastAddr != null){
                    break;          
                }
            }
            
            if (broadcastAddr == null){
                throw  new RuntimeException("Failed to setup broadcast Address");
            }
        } catch (SocketException ex) {
            throw new RuntimeException("Network interface setup failure", ex);
        }
    }

    //start TCP server on moderator port
    /**
     * Sets {@see #tcpModeratorPort} to the specified value, 
     * initializes {@see #moderatorServerSocket} with this port and
     * initializes {@see #modServer} with this socket. Then
     * creates and starts {@see #serverThread} that has {@see #modServer}
     * as routine.
     * @param tcpModeratorPort the specified number of port.
     * @throws RuntimeException with {@code message} 
     * {@code "Moderator socket failure"} (can be thrown while initializing 
     * {@see #moderatorServerSocket}).
     */
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
    
    //broadcast data via network
    /**
     * Broadcasts specified data via network. The data is sent to 
     * {@see #broadcastAddr} to the ports with numbers form 
     * {@see edu.cvut.vorobvla.bap.BapPorts.PLAYER_PORT}
     * to {@see edu.cvut.vorobvla.bap.BapPorts.PLAYER_PORT} + 
     * {@see edu.cvut.vorobvla.bap.BapPorts.PLAYER_PORT_RANGE} {@code - 1}.
     * @param data the data to be broadcasted.
     */
    private void sendBroadcastUDP(byte[] data){
        if (broadcastAddr == null){
            throw new RuntimeException("Broadcast address undefined. "
                    + "Possably disconected from network.");
        }
        
        for (int port = BapPorts.PLAYER_PORT; port < BapPorts.PLAYER_PORT + 
                BapPorts.PLAYER_PORT_RANGE; port++) {
            DatagramPacket sendPacket = 
                    new DatagramPacket(data, data.length, broadcastAddr, port);
            try {
            //    System.out.println("Sent UDP on " + netIntfce
           //             + ":" + port);
                UDPSocket.send(sendPacket);
            } catch (IOException ex) {
                Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("exception while broadcasting UDP");
            }
        }
    }
    
    //broadcast information about game
    /**
     * Broadcasts information about the game via network. Gets
     * a {@code JSONObject} with information from the {@see Model.Game} and
     * puts in the object the current timestamp with key
     * {@see edu.cvut.vorobvla.bap.BapJSONKeys#KEY_BROADCAST_TIMESTAMP}.
     * Then transforms the {@code JSONObject} to a string and then to
     * a {@code byte} array that is sent with {@see #sendBroadcastUDP}.
     */    
    public void broadcastGameInfo(){
        JSONObject sendObj = Game.getInfoJSON();
        //System.out.println(sendObj);
        sendObj.put(BapJSONKeys.KEY_BROADCAST_TIMESTAMP, System.currentTimeMillis());
        sendBroadcastUDP(sendObj.toJSONString().getBytes());
    }
    
    //broadcast data needed for players to establish connection
    /**
     * Broadcasts message {@see edu.cvut.vorobvla.bap.BapMessages.MSG_CALL_FOR_PLAYERS}
     * containing value of {@see #tcpModeratorPort}.
     */
    public void callPlayers(){
        String msg = BapMessages.MSG_CALL_FOR_PLAYERS + BapMessages.FIELD_DELIM + tcpModeratorPort;
        System.out.println("'" + new String(msg.getBytes()) + "'");
        sendBroadcastUDP(msg.getBytes());   
    }

    /**
     * Returns {@see #netIntfceName} value.
     * @return {@see #netIntfceName} value.
     */
    public String getNetIntfceName() {
        return netIntfceName;
    }

    /**
     * Returns {@see #tcpModeratorPort} value.
     * @return {@see #tcpModeratorPort} value.
     */
    public int getModeratorPort() {
        return tcpModeratorPort;
    }

    /**
     * Returns {@see #broadcastGameOpt} value.
     * @return {@see #broadcastGameOpt} value.
     */
    public boolean getBroadcastGameOpt() {
        return broadcastGameOpt;
    }

    /**
     * Sets {@see #broadcastGameOpt} tho specified value.
     * @param broadcastGameOpt the desired value of {@see #broadcastGameOpt}.
     */
    public void setBroadcastGameOpt(boolean broadcastGameOpt) {
        this.broadcastGameOpt = broadcastGameOpt;
    }

    @Override
    /**
     * Closes {@see #UDPSocket} and {@see #moderatorServerSocket}.
     */
    public void close() throws IOException {
        //todo kill all peers
        try {
            moderatorServerSocket.close();
            UDPSocket.close();
        } 
        catch(NullPointerException ex) {
            
        }
    }

    @Override
    public String toString() {   
            String db_nw; 
            String db_addr;
            db_nw = (netIntfce == null) ? "null" : netIntfce.toString();
            db_addr = (broadcastAddr == null) ? "null" : broadcastAddr.toString();
            return "NETWORK:\nIFCE:\t"+db_nw+"\nBroadcast ADDR\t"+db_addr;
    }
    
    

}
