/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bapwifiprotomoderator;
import GUI.MainFrame;
import GUI.NetworkFrame;
import GUI.NetworkFrame;
import Networking.Networking;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
/**
 * <p> TODO description of BAPWiFiProtoMaster
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class BAPWiFiProtoModerator {

    /**
     * @param args the command line arguments
     * @throws java.net.UnknownHostException
     * @throws java.net.SocketException
     */
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        // TODO code application logic here
        Networking network = new Networking("wlan0", 1111);
        MainFrame frame = new MainFrame();
        NetworkFrame nframe = new NetworkFrame(network);
        //frame.setVisible(true);
        nframe.setVisible(true);
        //
    }
}
