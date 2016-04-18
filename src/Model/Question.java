/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 * <p> Represents question that is asked to players. 
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

//represents question that is asked to players
public class Question {
    /**
     * Price of the question. Can be added to/subtracted from the score
     * of the player who has answered the question
     */
    protected int price;
    /**
     * Topic of the question. Number of questions of each topic during the game
     * should be the same.
     */
    protected String topic;
    /**
     * Text of the question. 
     */
    protected String text;
    /**
     * Text of the answer. 
     */
    protected String answer;
    /**
     * Text of the notes. 
     */
    protected String notes;
    
    /**
     * Constructs a player with specified {@code price}, {@code topic} and
     * {@code text}.
     * @param price the value of the {@code price} of the constricted {@code Question}.
     * @param topic the {@code topic} of the constricted {@code Question}.
     * @param text the {@code text} of the constricted {@code Question}.
     */
    public Question(int price, String topic, String text, 
            String answer, String notes) {
        this.price = price;
        this.topic = topic;
        this.text = text;
        this.answer = answer;
        this.notes = notes;
    }

    /**
     * Returns the value of {@code price} of the {@code Question}.
     * @return the {@code price} of the {@code Question}
     */
    public int getPrice() {
        return price;
    }

    /**
     * Returns the value of {@code topic} of the {@code Question}.
     * @return the {@code topic} of the {@code Question}
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Returns the value of {@code text} of the {@code Question}.
     * @return the {@code text} of the {@code Question}
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the value of {@code answer} of the {@code Question}.
     * @return the {@code answer} of the {@code Question}
     */
    public String getAnswer() {
        return answer;
    }
    
    /**
     * Returns the value of {@code notes} of the {@code Question}.
     * @return the {@code notes} of the {@code Question}
     */
    public String getNotes() {
        return notes;
    }
    
    
    
}
