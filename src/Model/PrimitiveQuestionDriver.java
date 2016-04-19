/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 * <p> Primitive implementation of question driver. Represents the style of the 
 * game when questions are read one-by-one (from the cheapest question in current
 * topic to the most expansive and then question the next topic the same way).
 * Generated questions contain only price, other attributes are set to null.
 * Prices are incremented by the same value that is equal to the price of the 
 * cheapest question.
 * 
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

//primitive implementation of question driver
public class PrimitiveQuestionDriver extends AbstractQuestionDriver{
    /** Number of questions per topic. */
    private final int numberOfQuestions;
    /** Value by which the prices are incremented. */
    private final int priceDifference;
    /** Price of the current question. */
    private int currentPrice;
    /** Number of the current question (in the current topic). */
    private int questionCnt;
    /** Topic number of the current question. */
    private int currentTopicCnt;

    /**
     * Constructs a new {@code PrimitiveQuestionDriver} object with specified 
     * {@code numberOfQuestions} and {@code priceDifference}.
     * @param numberOfQuestions number of questions per topic
     * @param priceDifference value by which the prices are incremented 
     */
    public PrimitiveQuestionDriver(int numberOfQuestions, int priceDifference) {

        this.numberOfQuestions = numberOfQuestions;
        this.priceDifference = priceDifference;
        this.currentPrice = 0;
        questionCnt = 0;
        currentTopicCnt = 1;
    }
    
    /**
     * Increments {@code currentPrice} and resets it if needed.
     */
    private void incPrice() {
        if (questionCnt == numberOfQuestions){
            questionCnt = 0;
            currentPrice = 0;
            currentTopicCnt++;
        }
        questionCnt++;
        currentPrice += priceDifference;
    }

    @Override
    public Question getQuestion() {
        incPrice();
        return new Question(currentPrice, "Topic " + currentTopicCnt, null, null, null);
    }
    

    
}
