/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bapwifiprotomoderator;
import GUI.MainFrame;
import Networking.Networking;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 * <p> Main class of the application.
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class BAPWiFiProtoModerator {

    /**
     * Sets up networking to default and constructs 
     * {@see GUI.MainFrame}.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String networkingErrText = "";
        
        try {
            Networking.getInstance().setUpDefault();
        }
        catch (NullPointerException ex){
            networkingErrText = "Network interface not found or interface not connected to network.";
            Logger.getLogger(BAPWiFiProtoModerator.class.getName())
                    .log(Level.WARNING, networkingErrText, ex);
        }
        catch (RuntimeException ex){
            networkingErrText = "Socket failure, maybe moderator port is occupied. Try other port.";
            Logger.getLogger(BAPWiFiProtoModerator.class.getName())
                    .log(Level.WARNING, "Socket failure -- try other port", ex);
        }
        
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
        if (!"".equals(networkingErrText)){
            JOptionPane.showMessageDialog(frame, networkingErrText);  
        }      
    }
}
