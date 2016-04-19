/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p> TODO description of Timer
 * @author Vladimir Vorobyev (vorobvla)
 * @created on Sep 7, 2014 at 12:50:59 PM
 */

public class Timer {
    private ScheduledThreadPoolExecutor scheduler;
    private static Timer instance;
    private long start;
    private long updatePeriod;
    private long end;
    private long leftTime;
    private boolean pause;
    private ScheduledFuture schFtr;
    private Runnable task;

    
    private Timer() {
        updatePeriod = 50;
        pause = false;
        scheduler = new ScheduledThreadPoolExecutor(1);
    }
    
    public static Timer getInstance(){
        if (instance == null){
            instance = new Timer();
        }
        return  instance;
    }
    
    public void set(long period, final Runnable onPeriod){
        
        if (schFtr != null){
        System.out.println("DONE: " + schFtr.isDone());
            if (!schFtr.isDone()){
            throw new RuntimeException("attempt to set a set timer");
            }           
        }
        
        pause = false;
        start = System.currentTimeMillis();
        this.end = start + period; 
        leftTime = period;
        System.out.println("Start left " + leftTime);
        task = onPeriod;

        schFtr = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (leftTime <= 0){
                    schFtr.cancel(true);   
                    task.run();
                    //System.out.println(System.currentTimeMillis() - start);
                    //unset
                }
                if (pause){
                    //System.out.println("paused");
                } else {
                    //decrease time
                    
                    leftTime -= updatePeriod;
                    System.out.println("-> " + leftTime);
                }
            }
        }, updatePeriod, updatePeriod, TimeUnit.MILLISECONDS);
    }
    
    public long getLeftTime(){
        return leftTime;
    }
    
    public void skip(){
        if (schFtr.isDone()){
            return;
        }
        schFtr.cancel(true);
        task.run();
    }
    
    public void unset(){
        if ((schFtr != null) && (!schFtr.isDone())){
            schFtr.cancel(true);
        }
    }
    
    public boolean isSet(){
        return  schFtr.isDone();
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
       
}
