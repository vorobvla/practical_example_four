/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import edu.cvut.vorobvla.bap.PlayerStateEnum;
import edu.cvut.vorobvla.bap.GameStateEnum;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p> TODO description of Game
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class Game {
  //  private String name;
    private long timestampStart;
    //add tstamps for touts
    private static long gameTimestamp;
    private static long questionTimestamp;
    private static long processTimestamp;
    private static long questionTimeout;
    private static OutputStream log;
    private static GameStateEnum state;
    private HashSet<Player> players;
    private static Player answeringPlayer;
    private static Game instance;
    private static int maximumAppliing;
  //  private Collection players;

    public static Game getInstance() {
        if (instance == null){
            instance = new Game();
        }
        return instance;
    }
    
    public Game() {
        timestampStart = System.currentTimeMillis();
        state  = GameStateEnum.START;
        players = new HashSet<>();       
        answeringPlayer = null;
        questionTimeout = Constants.DEFAULT_QUESTION_TOUT;
        maximumAppliing = Constants.MAXIMUM_TIMES_TO_APPLY;
    }
            
    private void startStateRoutine(){
        gameTimestamp = System.currentTimeMillis();
        log("game started at " + (new Date(gameTimestamp).toString()));
        String playing = "Players:  ";
        for (Player player: players){
            playing += player.getIdentity() + ", ";
        }
        log(playing.substring(0, playing.length() - 2));
        state = GameStateEnum.COOSING_QUESTION;
    }
    
    private void choosingStateRoutine(){
        
        //choosing question to be implemented here
        setAllPlayersState(PlayerStateEnum.FALSEACTIVE);
        resetAllPlayersAppliedTimes();
        log("Question is chosen");
        state = GameStateEnum.READING_QUESTION;
    }
    
    private void readingStateRoutine(){
        state = GameStateEnum.AWAINTING_ANSWER;
        setAllPlayersState(PlayerStateEnum.ACTIVE); 
        for(Player player: players){
            log("+++++ " + player.getIdentity() + " is " + player.getState().name());
        }
        log("Question is read");
    }
    
    private void awaitingStateRoutine() throws InterruptedException{
        log("Avaiting for answer");
        questionTimestamp = System.currentTimeMillis();
        //active waiting for answer
        long time = questionTimeout;
        synchronized(this){
            while ((state == GameStateEnum.AWAINTING_ANSWER) && (time > 0)){
                wait(time);
                time -= (System.currentTimeMillis() - questionTimestamp);
                log("DEBUG: Threds work");
            }
        state = GameStateEnum.COOSING_QUESTION;
        }
        
        //tout
        //state = GameStateEnum.COOSING_QUESTION;
    }
    private void answerStateRoutine(){
        //answer timeout
        state = GameStateEnum.PROCESSING_ANSWER;
        try {
            proceed();
        } catch (GameException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void processingStateRoutine(){
    }
    private void finishStateRoutine(){
    }
    
    public static void proceed() throws GameException, InterruptedException{
        switch(state){
            case START:
                getInstance().startStateRoutine();
                break;
            case COOSING_QUESTION:
                getInstance().choosingStateRoutine();
                break;               
            case READING_QUESTION:
                getInstance().readingStateRoutine();
                break;
            case AWAINTING_ANSWER:
                getInstance().awaitingStateRoutine();
                break;
            case ANSWER:
                getInstance().answerStateRoutine();
                break;
            case PROCESSING_ANSWER:
                getInstance().processingStateRoutine();
                break;
            case FINISH:
                getInstance().startStateRoutine();
                break;                    
        }
    }
    
    public static synchronized void recieveApplication(Player from){
        try {
            processTimestamp = System.currentTimeMillis();
            state = GameStateEnum.ANSWER;
            answeringPlayer = from;
            getInstance().setAllPlayersState(PlayerStateEnum.PASSIVE);
            log("Recived Application");
            proceed();
            //tout -- deny?
        } catch (GameException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void acceptAnswer() throws GameException{
        if (state == GameStateEnum.PROCESSING_ANSWER) {
            state = GameStateEnum.COOSING_QUESTION;
            synchronized(getInstance()){
                getInstance().notify();
            }
            answeringPlayer.chageScoreBy(10);
            log("answer acepted (Player score change)");
        } else {
            throw new GameException("Illegal game state");
        }              
    }
    
    public static void denyAnswer() throws GameException{
        if (state == GameStateEnum.PROCESSING_ANSWER) {
            //if all aswered state = GameStateEnum.COOSING_QUESTION; else
            
            state = GameStateEnum.AWAINTING_ANSWER;
            answeringPlayer.chageScoreBy(-10);
            getInstance().setAcativePlayers();
            synchronized(getInstance()){
                getInstance().notify();
            }
            //if (...) {chooseQuestion()}
            log("answer denied (Player score change)");
        } else {
            throw new GameException("Illegal game state");
        }              
    }

  /*  public void setPlayers(HashSet<Player> players) {
        //this.players = players;
        for (Player p : players) {
            p.reset();
            this.players.add(p);
        }
    }*/

    public HashSet<Player> getPlayers() {
        return players;
    }
    
    private void setAllPlayersState(PlayerStateEnum state){
        for (Player player : players) {
            player.setState(state);
        }
    }
    
    private void resetAllPlayersAppliedTimes(){
        for (Player player : players) {
            player.resetAppliedTimes();
        }
    }
    
    private void setAcativePlayers(){
        for (Player player : players) {
            if (player.getAppliedTimes() < maximumAppliing){
                player.setState(PlayerStateEnum.ACTIVE);
            }
        }
    }

    public static Player getAnsweringPlayer() throws GameException {
        if ((state == GameStateEnum.ANSWER) || 
                (state == GameStateEnum.PROCESSING_ANSWER)) {
                return answeringPlayer;
        } else {
            throw new GameException("Illegal game state");
        }
    }
    
    public static void log(String msg){
        try {
            msg += "\n";
            log.write((msg).getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setLog(OutputStream log) {
        this.log = log;
    }

    public static GameStateEnum getState() {
        return state;
    }    
    
    public void addPlayer(Player player) throws GameException{
        if (state != GameStateEnum.START){
            throw new GameException("Invalid game state");
        }
        player.reset();
        players.add(player);
    }

    public static void setQuestionTimeout(int questionTimeout) {
        Game.questionTimeout = questionTimeout;
    }

    public static int getMaximumApplying() {
        return maximumAppliing;
    }
    
    public String printState(){   
        String s;
        s = "game state not defined";
        switch(state){
            case BEFORE : 
                s = "game not started";
                break;
            case START : 
                s = "game begins";
                break;
            case COOSING_QUESTION : 
                s = "question is being chosen";
                break;
            case READING_QUESTION : 
                s = "question is being read";
                break;
            case AWAINTING_ANSWER : 
                s = "answer is being awaiting";
                break;
            case ANSWER : 
                s = "player " + answeringPlayer + " answers";
                break;
            case PROCESSING_ANSWER : 
                s = answeringPlayer + "'s answer is being processed";
                break;
            case FINISH : 
                s = "game is finished";
                break;
            case FINISHED : 
                s = "game has been finished";
                break;                
        }
        return s;
    }
    
}
