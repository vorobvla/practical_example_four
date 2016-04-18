/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import edu.cvut.vorobvla.bap.PlayerStateEnum;
import Networking.PlayerPeer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p> Represents player. Contains information about player and a {@see PlayerPeer}
 * object needed to communicate with the corresponding player's application via
 * network.
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */


//represents player
public class Player {
    /** Identifier of the player. Should be unique. */
    private final String identity;
    /** the {@see PlayerPeer} that provides network communication with the player
     application that is used by the player that is represented by this object. */
    private final PlayerPeer peer;
    /** Actual score of this player in the current game. */
    private int currentScore;
    /** Total score of this player from the past games. */
    private int totalScore;
    /** Current state of this player. Determines the result of an attempt to apply
     * for answer. By default is {@see PlayerStateEnum#PASSIVE}. */
    private PlayerStateEnum state;
    /** Collection of all the players that represent players connected to
     this application. Initialized in static constructor. */
    private static ArrayList<Player> all;
    /** How much times has this player applied to answer for current question. */
    private int appliedTimes;
    /** If this player is communicating to this application via network. */
    private boolean online;
    
    static{
        all = new ArrayList<>();
    }
    
    /**
     * Constructs a new {@see Player} object with specified 
     * {@see #identity} and {@see #peer}.
     * @param identity the {@see #identity} of the {@see  #Player} object. 
     * @param peer the {@see #peer} providing network communication with the 
     * player application represented by {@see  #Player} object.
     */
    public Player(String identity, PlayerPeer peer) {
        this.identity = identity;
        this.peer = peer;
        currentScore = 0;
        totalScore = 0;
        state =  PlayerStateEnum.PASSIVE;
        all.add(this);
        appliedTimes = 0;
    }

    /**
     * Returns a static {@code Collection} of all players that are communicating 
     * to this application.
     * @return {@code Collection} of all {@see #Player} objects that represent players 
     * that are online.
     */
    public static ArrayList<Player> getAll() {
        return all;
    }

    /**
     * Returns current {@see #state} of this {@see #Player} object.
     * @return {@see Model.PlayerStateEnum} describing state of the player.
     */
    public PlayerStateEnum getState() {
        return state;
    }

    /**
     * Sets {@see #state} of this {@see #Player} object.
     * @param state the desired {@see #state} of this {@see #Player} object.
     */
    public void setState(PlayerStateEnum state) {
        this.state = state;
    }
    
    /**
     * Resets the {@see #appliedTimes} to {@code 0}.
     */
    public void resetAppliedTimes(){
        appliedTimes = 0;
    }

    /**
     * Returns {@see #identity} of the player. 
     * @return {@code String} representing identity.
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Returns {@see #currentScore} of the player.
     * @return {@code int} value of {@see #currentScore}.
     */
    public int getScore() {
        return currentScore;
    }

    /**
     * Returns {@see #peer}.
     * @return {@see Model.PlayerPeer} that can be used for network communication with the
     * player represented by this {@see Player} object.
     */
    public PlayerPeer getPeer() {
        return peer;
    }
    
    /**
     * Changes the {@see #currentScore} by {@code diff}.
     * @param diff {@code int} value by which the {@see #currentScore} is
     * changed. Use negative value to reduce the {@see #currentScore} and 
     * positive value to enlarge it.
     */
    public void chageScoreBy(int diff){
        currentScore += diff;
    }

    /**
     * Attempts to apply for answer on the part of the represented player.
     * The result depends on the {@see #state}. 
     * <p>Attempt is successful only if
     * {@see #state} is {@see PlayerStateEnum#ACTIVE}. In this case 
     * {@code Game.recieveApplication(this)} is called and the {@see #appliedTimes}
     * is incremented. In other cases nothing is happening. 
     * <p>In future versions negative score change may occur in
     * case the {@see #state} is {@see PlayerStateEnum#FALSEACTIVE}.
     */
    //try to apply for answer. game must recieve the application only if player is in active state
    public void applyForAnswer() {
        try {
            Game.getInstance().log(identity + "[DB] applied for answer at " + System.currentTimeMillis());
            switch (state){
                case ACTIVE :
                    Game.recieveApplication(this);
                    appliedTimes++;/*
                    if (appliedTimes < game.getInstance().getMaximumApplying()){
                    state = PlayerStateEnum.PASSIVE;
                    resetAppliedTimes();
                    }*/
                    break;
                case FALSEACTIVE :
                    // falsestart
                    appliedTimes++;
                    Game.recieveFasleStart(this);
                    Game.getInstance().log(identity + "[DB] state falseactive ");
                    break;
                case PASSIVE :
                    Game.getInstance().log(identity + "[DB]state passive ");
                    // falsestart
                    break;
                case CHOOSEING_QUESTION :
                    // falsestart
                    break;
            }
        } catch (GameException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the value of {@see #appliedTimes}.
     * @return {@code int} value of {@see #appliedTimes}.
     */
    public int getAppliedTimes() {
        return appliedTimes;
    }

    /**
     * Resets {@see #currentScore} to {@code 0} and {@see #state}
     * to {@see edu.cvut.vorobvla.bap.PlayerStateEnum#PASSIVE}.
     */
    public void reset() {
        currentScore = 0;
        state = PlayerStateEnum.PASSIVE;
    }

    /**
     * Adds the value of {@see #currentScore} to
     * {@see #totalScore} and calls {@see  #reset();}
     */
    public void saveScore() {
        totalScore += currentScore;
        reset();
    }

    /**
     * If the player represented by this {@see #Player} object is available
     * for network communication.
     * @return boolean value. In case the player is available return value is
     * {@code true} otherwise {@code false}.
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets value of {@see #online}.
     * @param online boolean value. {@code true} must be set if the player represented by
     * this {@see #Player} object is is available for network communication.
     * Otherwise {@code false} must be set.
     */
    public void setOnline(boolean online) {
        this.online = online;
    }
    
    
        
}
