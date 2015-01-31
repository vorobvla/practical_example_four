/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

/**
 * <p> TODO description of Constants
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public abstract class Constants {
    public static final String BTN_OK = "Ok";
    
    
    //--- GAME PANEL --
    public static final String LAB_QUESTION_CONTENT = "QUESTION";    
    
    
    //--- NETWORK SETUP ---
    
    public static final String LAB_NETWORK_INTERFACE = "Interface";
    public static final String LAB_MODERATOR_PORT = "Port";
    
    
    
    
    public static final String ERR_IFCE_NAME(String s){   
        return "can not intarface with name \"" + s + "\"";
    }
}
