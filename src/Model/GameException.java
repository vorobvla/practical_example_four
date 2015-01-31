/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 * <p> TODO description of GameException
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class GameException extends Exception{

    public GameException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Game Model exception: " + super.getMessage();
    }   
}
