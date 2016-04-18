/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Networking;

/**
 * <p> State of the peer from networking point of view
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */
public enum PeerState {
    /** Network communication between moderator application and player state 
     is enabled. */
    CONNECTED, 
    /**  Network communication between moderator application and player state 
     is disabled.  */
    DISCONNECTED
}
