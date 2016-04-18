/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 * <p> Classes of this object are thrown by methods of {@see Model.Game} class 
 * (specifically if some method of {@see Model.Game} object is called when 
 * {@see Model.Game} object is in the state when this method can not be
 * executed; in this case the {@see message} field of {@see #GameException} object
 * usually contents {@code "Illegal game state"}).
 * 
 * 
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class GameException extends Exception{

    /**
     * Constructs a {@see #GameException} object with specified {@see message}.
     * @param message @code String} content of  {@see message} field.
     */
    public GameException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Game Model exception: " + super.getMessage();
    }   
}
