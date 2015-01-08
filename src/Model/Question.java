/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 * <p> TODO description of Question
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class Question {
    protected int price;
    protected String topic;
    protected String text;

    public Question(int price, String topic, String text) {
        this.price = price;
        this.topic = topic;
        this.text = text;
    }

    public int getPrice() {
        return price;
    }

    public String getTopic() {
        return topic;
    }

    public String getText() {
        return text;
    }
    
    
}
