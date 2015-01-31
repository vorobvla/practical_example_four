/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import edu.cvut.vorobvla.bap.PlayerStateEnum;
import Networking.PlayerPeer;
import java.util.ArrayList;

/**
 * <p> TODO description of Player
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */



public class Player {
    private final String identity;
    private final PlayerPeer peer;
    private int currentScore;
    private PlayerStateEnum state;
    private static ArrayList<Player> all;
    private Game game;
    private int appliedTimes;
    
    static{
        all = new ArrayList<>();
    }
    

    public Player(String identity, PlayerPeer peer) {
        this.identity = identity;
        this.peer = peer;
        currentScore = 0;
        state =  PlayerStateEnum.PASSIVE;
        all.add(this);
        appliedTimes = 0;
    }

    public static ArrayList<Player> getAll() {
        return all;
    }

    public PlayerStateEnum getState() {
        return state;
    }

    public void setState(PlayerStateEnum state) {
        this.state = state;
    }
    
    public void resetAppliedTimes(){
        appliedTimes = 0;
    }

    public String getIdentity() {
        return identity;
    }

    public int getScore() {
        return currentScore;
    }

    public PlayerPeer getPeer() {
        return peer;
    }
    
    public void chageScoreBy(int diff){
        currentScore += diff;
    }

    public void assignToGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void applyForAnswer() {
        Game.getInstance().log(identity + "-----> applied for answer at " + System.currentTimeMillis());
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
                Game.getInstance().log(identity + "-----> state falseactive ");
                break;
            case PASSIVE :
                Game.getInstance().log(identity + "-----> state passive ");
                // falsestart
                break;
            case CHOOSEING_QUESTION :
                // falsestart
                break;
        }
    }

    public int getAppliedTimes() {
        return appliedTimes;
    }

    void reset() {
        currentScore = 0;
        state = PlayerStateEnum.PASSIVE;
    }
    
    
        
}
