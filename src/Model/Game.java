/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
    private static OutputStream log;
    private static GameStateEnum state;
    private HashSet<Player> players;
    private static Player answeringPlayer;
    private static Game instance;
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
    }
    
    private static void gameIsStarted() throws GameException{
        if (state == GameStateEnum.START) {
            state = GameStateEnum.COOSING_QUESTION;
            
            
            //start gametout
            log("gameStarted");
        } else {
            throw new GameException("Illegal game state");
        }       
    }
    
    private static void questionIsChosen() throws GameException{
        if (state == GameStateEnum.COOSING_QUESTION) {
            //control finish conditions.  finish if must.
            state = GameStateEnum.READING_QUESTION;
            //set (almost) all passive
            //(choose question, set all passive)
            log("Question is chosen");
        } else {
            throw new GameException("Illegal game state");
        }       
    }
    
    private static void readQuestion() throws GameException{
        if (state == GameStateEnum.READING_QUESTION) {
            //set all falseactive
            //display question
            //start qtout-2?
            log("Question is read");
        } else {
            throw new GameException("Illegal game state");
        }       
    }
    
    public static void startAwatingAnswer() throws GameException{
        if (state == GameStateEnum.READING_QUESTION) {
            state = GameStateEnum.AWAINTING_ANSWER;
            //start qtout
            //set all active
            log("start waiting for answer to the question");
            watingForAnswer();           
        } else {
            throw new GameException("Illegal game state");
        }       
    }
    
    public static void watingForAnswer() throws GameException{
        if (state == GameStateEnum.AWAINTING_ANSWER) {
            
            //get applying
            //start qtout
            log("waiting for answer");
        } else {
            throw new GameException("Illegal game state");
        }              
    }
    
    public static void gotAnswer() throws GameException{
        if (state == GameStateEnum.AWAINTING_ANSWER) {
            
            //control concurency
            //set current player
            state = GameStateEnum.ANSWER;
            log("palyer " + " answers");
            //start atout
            //...
            //end atout
             state = GameStateEnum.PROCESSING_ANSWER;    
            //start ptout 
        } else {
            throw new GameException("Illegal game state");
        }              
    }
    
    public static void acceptAnswer() throws GameException{
        if (state == GameStateEnum.PROCESSING_ANSWER) {
            answeringPlayer.chageScoreBy(10);
            log("answer acepted (Player score change)");
            questionIsChosen();
        } else {
            throw new GameException("Illegal game state");
        }              
    }
    
    public static void denyAnswer() throws GameException{
        if (state == GameStateEnum.PROCESSING_ANSWER) {
            log("answer denied (Player score change)");
            answeringPlayer.chageScoreBy(-10);
            //if (...) {chooseQuestion()}
            watingForAnswer();
        } else {
            throw new GameException("Illegal game state");
        }              
    }
    
    public static void finshGame() throws GameException{
        if (state == GameStateEnum.COOSING_QUESTION) {
            log("game finished");
            state = GameStateEnum.FINISH;
        } else {
            throw new GameException("Illegal game state");
        }              
    }

    public void setLog(OutputStream log) {
        this.log = log;
    }

    public static GameStateEnum getState() {
        return state;
    }
    
    public static void toNextState() throws GameException{
        switch(state){
            case START:
                //to choosing
                gameIsStarted();
                break;
            case COOSING_QUESTION:
                //to read
                questionIsChosen();
                break;
            case READING_QUESTION:
                //to await
                readQuestion();
                break;
            case AWAINTING_ANSWER:
                
                //to answer
                break;
            case ANSWER:
                //to process
                break;
            case PROCESSING_ANSWER:
                //accept or deny answer
                //to awaiting
                //to choosing
                //default process
                break;
            case FINISH:
                break;                    
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
    
    
}
