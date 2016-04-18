/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bapwifiprotomoderator;
import GUI.MainFrame;
import GUI.NetworkPanel;
import Networking.Networking;
import java.lang.NullPointerException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            Networking.getInstance().setUpDefault();
        }
        catch (NullPointerException ex){
            Logger.getLogger(BAPWiFiProtoModerator.class.getName())
                    .log(Level.WARNING, "NullPointer -- possibly network interface not found", ex);
        }
        catch (RuntimeException ex){
            Logger.getLogger(BAPWiFiProtoModerator.class.getName())
                    .log(Level.WARNING, "Socket failure -- try other port", ex);
        }
        
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
       
    }
}
