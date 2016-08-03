/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 * <p>An abstract class for a component of the
 * {@see Model.Game} class that handles question choosing and switching.
 * Implementations of this class must represent different styles of 
 * choosing questions.
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

//a class that reffered to generate questions
public abstract class AbstractQuestionDriver {
    /**
     * Returns the question that is now chosen or have to be asked by 
     * the sequence (depending on the implemented style of choosing).
     * @return a {@see Model.Question} object representing the question
     * that has to be asked at this moment.
     */
    public abstract Question getQuestion();

}
