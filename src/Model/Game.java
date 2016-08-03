 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import edu.cvut.vorobvla.bap.BapJSONKeys;
import edu.cvut.vorobvla.bap.PlayerStateEnum;
import edu.cvut.vorobvla.bap.GameStateEnum;
import static edu.cvut.vorobvla.bap.GameStateEnum.COOSING_QUESTION;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 * The class that implements the finite automata, that is the base of game process.
 * Also, handles competition, false start and most part of  other game process aspects.
 * Implemented as singleton.
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
    private static int maximumApplying;
    private static int playersNotAnswered;
    private AbstractQuestionDriver questionDriver;
    private static Question currentQuestion;
    boolean paused;
    private static ScheduledThreadPoolExecutor scheduler;
    private boolean broadcastInfo;
    /** scheduled action for question "timer" (ScheduledThreadPoolExecutor)*/
    private static ScheduledFuture questionFuture;
  //  private Collection players;
/**
 * Usual method for getting singleton instance.
 * @return 
 */
    public static Game getInstance() {
        if (instance == null){
            instance = new Game();
        }
        return instance;
    }
    
    /**
     * Private constructor.
     */
    private Game() {
        timestampStart = System.currentTimeMillis();
        state  = GameStateEnum.START;
        players = new HashSet<>();       
        answeringPlayer = null;
        questionTimeout = Constants.DEFAULT_QUESTION_TOUT;
        maximumApplying = Constants.MAXIMUM_TIMES_TO_APPLY;
        questionDriver = new PrimitiveSequentialQuestionDriver(6, 10);
        paused = false;
        reset();
    }
    
    /**
     * Represents the activity, assigned to the Start Game state (at the very beginning of
     * game process).
     */
    private void startStateRoutine(){
        log("[DB] startStateRoutine @ " + System.currentTimeMillis());
        gameTimestamp = System.currentTimeMillis();
        log("game started at " + (new Date(gameTimestamp).toString()));
        
        String playing = "Players:  ";
        for (Player player: players){
            playing += player.getIdentity() + ", ";
        }
        log(playing.substring(0, playing.length() - 2));
        state = GameStateEnum.COOSING_QUESTION;
    }
    
    /**
     * Represents the activity, assigned to the Choosing Question state
     * (when a new question is obtained).
     */
    private void choosingStateRoutine(){
        log("[DB] choosingStateRoutine @ " + System.currentTimeMillis());
        
        //choosing question to be implemented here
        currentQuestion = questionDriver.getQuestion();
        
        setAllPlayersState(PlayerStateEnum.FALSEACTIVE);
        resetAllPlayersAppliedTimes();
        log("Question is chosen");
        state = GameStateEnum.READING_QUESTION;
        playersNotAnswered = players.size();
    }
    
    /**
     * Represents the activity, assigned to the Reading Question state
     * (when a question is being read and false start is active).
     */
    private void readingStateRoutine(){
        log("[DB] readingStateRoutine @ " + System.currentTimeMillis());
        //display question
        state = GameStateEnum.AWAINTING_ANSWER;
        setAllPlayersState(PlayerStateEnum.ACTIVE); 
        for(Player player: players){
            log("[DB] " + player.getIdentity() + " is " + player.getState().name());
        }
        log("Question is read");
    }
    
    /**
     * Represents the activity, assigned to the Awaiting for answer state
     * (when a player can apply to answer).
     */
    private void awaitingStateRoutine() throws InterruptedException{
        log("[DB] awaitingStateRoutine @ " + System.currentTimeMillis());
        log("Awaiting for answer; skip");
        Timer.getInstance().unset();
        /*synchronized(this){
            while ((state == GameStateEnum.AWAINTING_ANSWER) && (time > 0)){
                wait(time);
                time -= (System.currentTimeMillis() - questionTimestamp);
                log("[DB] Threds work");
            }
        state = GameStateEnum.COOSING_QUESTION;
        }    */    
        //tout
        state = GameStateEnum.COOSING_QUESTION;
    }
    
    /**
     * Represents the activity, assigned to the Answering state
     * (when a player answers). 
     */    
    private void answerStateRoutine(){
        log("[DB] answerStateRoutine @ " + System.currentTimeMillis());
        //answer timeout
        try {
            Thread.sleep(500);
            state = GameStateEnum.PROCESSING_ANSWER;
            proceed();
        } catch (GameException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Represents the activity, assigned to the Processing state
     * (when moderator accepts or denies the answer). 
     */
    private void processingStateRoutine(){
        log("[DB] processingStateRoutine @ " + System.currentTimeMillis());
    }
 
    /**
     * Represents the activity, assigned to the Finish state
     * (when the game is finishing). 
     */
    private void finishStateRoutine(){
        log("[DB] finishStateRoutine @ " + System.currentTimeMillis());
    }
    
    /**
     * Function for transferring the FA to the next state. Before calling
     * this method it is needed to set {@code Game.state} to the desired state.
     * @throws GameException in case of an attempt of transition, that is not 
     * valid for this automata
     * @throws InterruptedException 
     */
    public static void proceed() throws GameException, InterruptedException{
        switch(state){
            case START:
                getInstance().startStateRoutine();
                break;
            case COOSING_QUESTION:
                Timer.getInstance().unset();
                getInstance().choosingStateRoutine();
                break;               
            case READING_QUESTION:
                getInstance().readingStateRoutine();
                //setup question timer
                log("[DB] question timer starts @ " 
                        + System.currentTimeMillis());
                
                Timer.getInstance().set(questionTimeout, new Runnable() {
                    @Override
                    public void run() {              
                        log("[DB] timer skip to choosing question @ "
                                + System.currentTimeMillis());
                        Game.state = COOSING_QUESTION;
                        //TODO what if other state                    
                    }
                });
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
    
    /**
     * Receive applying from the player, who wishes to take part in competition 
     * for answer. After applying the user is processed according the game rules
     * and current context of the game.
     * @param from the player entity, associated with the applied user of player application 
     */
    public static synchronized void recieveApplication(Player from){
        Timer.getInstance().setPause(true);
        log("[DB] recieveApplication @ " + System.currentTimeMillis());
        try {
            if (from.getState() != PlayerStateEnum.ACTIVE){
                throw new GameException( "Not active player calling recieveApplication()");
            }
            playersNotAnswered--;
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
    
    
    
    /**
     * If player applies during Reading question state, a penalty
     * defined by the game rules is issued to him. This method performs the penalty.
     * @param from the player entity, associated with the false started user of player application 
     */
    static void recieveFasleStart(Player from) {
        playersNotAnswered--;
        from.chageScoreBy(-currentQuestion.getPrice());
        from.setState(PlayerStateEnum.PASSIVE);
    }
    
    /**
     * Accept the answer of the current player, increase his score and modify the game context according to rules
     * @throws GameException if called in wrong game context
     */
    public static void acceptAnswer() throws GameException{
        log("[DB] acceptAnswer @ " + System.currentTimeMillis());
        if (state == GameStateEnum.PROCESSING_ANSWER) {
            state = GameStateEnum.COOSING_QUESTION;
            synchronized(getInstance()){
                getInstance().notify();
            }
            answeringPlayer.chageScoreBy(currentQuestion.getPrice());
            Timer.getInstance().setPause(false);
            log("answer acepted (Player score change)");
        } else {
            throw new GameException("Illegal game state");
        }              
    }
    
    /**
     * Deny the answer of the current player, decrease his score and modify the game context according to rules
     * @throws GameException if called in wrong game context
     */
    public static void denyAnswer() throws GameException{
        log("[DB] denyAnswer @ " + System.currentTimeMillis());
        if (state == GameStateEnum.PROCESSING_ANSWER) {
            //if all aswered state = GameStateEnum.COOSING_QUESTION; else
            if (playersNotAnswered == 0){
                state = GameStateEnum.COOSING_QUESTION;
            } else {
                state = GameStateEnum.AWAINTING_ANSWER;
            }
            answeringPlayer.chageScoreBy(-currentQuestion.getPrice());
            getInstance().setAcativePlayers();
            synchronized(getInstance()){
                getInstance().notify();
            }
            Timer.getInstance().setPause(false);
            log("answer denied (Player score change)");
        } else {
            throw new GameException("Illegal game state");
        }              
    }

    /**
     * Get a collection of players who attend this game
     * @return a collection of players who attend this game
     */
    public HashSet<Player> getPlayers() {
        return players;
    }
    
    /**
     * Set all the players who attend the game to specified state
     * @param state the state so set the players
     */
    private void setAllPlayersState(PlayerStateEnum state){
        for (Player player : players) {
            player.setState(state);
        }
    }
    
    /**
     * Set to 0 the number of allyings to all players, who attend the game.
     */
    private void resetAllPlayersAppliedTimes(){
        for (Player player : players) {
            player.resetAppliedTimes();
        }
    }
    
    /**
     * Set all players who may apply to answer to Active state.
     */
    private void setAcativePlayers(){
        for (Player player : players) {
            if (player.getAppliedTimes() < maximumApplying){
                player.setState(PlayerStateEnum.ACTIVE);
            }
        }
    }

    /**
     * Get the player that has the right to answer.
     * @return player that has the right to answer. 
     * @throws GameException if called in wrong game context
     */
    public static Player getAnsweringPlayer() throws GameException {
        if ((state == GameStateEnum.ANSWER) || 
                (state == GameStateEnum.PROCESSING_ANSWER)) {
                return answeringPlayer;
        } else {
            throw new GameException("Illegal game state");
        }
    }
    
    /**
     * Log message.
     * @param msg a message to log
     */
    public static void log(String msg){
        try {
            msg += "\n";
            log.write((msg).getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Set stream for logging
     * @param log {@code OutputStream} for logging
     */
    public void setLog(OutputStream log) {
        this.log = log;
    }

    /**
     * Get current game state.
     * @return current game state
     */
    public static GameStateEnum getState() {
        return state;
    }    
    
    /**
     * Add player to game
     * @param player player to add
     * @throws GameException if called in wrong context
     */
    public void addPlayer(Player player) throws GameException{
        if (state != GameStateEnum.START){
            throw new GameException("Invalid game state: " + state + ", while "
                    + GameStateEnum.START + " expected.");
        }
        player.reset();
        players.add(player);
    }

    public static void setQuestionTimeout(int questionTimeout) {
        Game.questionTimeout = questionTimeout;
    }

    public static int getMaximumApplying() {
        return maximumApplying;
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
                s = "player " + answeringPlayer.getIdentity() + " answers";
                break;
            case PROCESSING_ANSWER : 
                s = answeringPlayer.getIdentity() + "'s answer is being processed";
                break;
            case FINISH : 
          //  gameInfoPanel.getScoreTable().getModel().;
                s = "game is finished";
                break;
            case FINISHED : 
                s = "game has been finished";
                break;                
        }
        if (paused){
            s += " (game paused)";
        }
        return s;
    }
    
    /**
     * End game and transfer it to finish state.
     */
    public void finishGame() {
        state = GameStateEnum.FINISH;
        for (Player player : getInstance().getPlayers()){
               // player.getPeer().sendMsg(BapMessages.MSG_GAME_FIN);
                player.saveScore();
        }       
        scheduler.shutdownNow();
        //state = GameStateEnum.FINISHED;
    }   
    
    /**
     * Get current game information in JSON format.
     * @return current game information in JSON format.
     */
    public static JSONObject getInfoJSON(){
        JSONObject output = new JSONObject();
        output.put(BapJSONKeys.KEY_STATE, Game.getState());
        if (state == GameStateEnum.ANSWER){
            output.put(BapJSONKeys.KEY_ANSWERING_PLAYER, answeringPlayer.getIdentity());
        } else {
            output.put(BapJSONKeys.KEY_ANSWERING_PLAYER, "");
        }
        JSONObject tab = new JSONObject();
        for (Player player : Game.getInstance().getPlayers()) {
            tab.put(BapJSONKeys.KEY_PLAYER_ID, player.getIdentity());
            tab.put(BapJSONKeys.KEY_PLAYER_SCORE, player.getScore());
        }
        output.put(BapJSONKeys.KEY_PLAYERS_TAB, tab);
        
        return output;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void startBroadcast() {
        for (Player p : players){
            p.getPeer().sendOpts(broadcastInfo);
        }
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {                
                Networking.Networking.getInstance().broadcastGameInfo();
            }
        }, 200, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * Signalizes if broadcast is enabled
     * @return {@code true} if yes otherwise {@code false}
     */
    public boolean isBroadcastInfo() {
        return broadcastInfo;
    }

    /**
     * Enable/disable game information broadcast
     * @param broadcastInfo 
     */
    public void setBroadcastInfo(boolean broadcastInfo) {
        this.broadcastInfo = broadcastInfo;
    }
    
    /**
     * Resets game when it is needed to be started after ending.
     */
    public void reset(){        
        scheduler = new ScheduledThreadPoolExecutor(2);
        /*for (Player p : players){
            p.reset();
        }*/
        players.clear();
        state = GameStateEnum.START;
        questionDriver = new PrimitiveSequentialQuestionDriver(6, 10);
    }

    /**
     * Get length of time period for applying for answers.
     * @return 
     */
    public static long getQuestionTimeout() {
        return questionTimeout;
    }
    
    /**
     * Get the time left until question timeout. (How much time is left
     * for this question)
     * @return the time left until question timeout
     */
    public long getQuestionTimeLeft(){
        return Timer.getInstance().getLeftTime();
    }
}
