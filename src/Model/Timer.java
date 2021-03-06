/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * En entity for handling timeouts, based on {@link java.util.concurrent.ScheduledThreadPoolExecutor}
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
    private Thread task;

    /**
     * Private constructor -- typical for singleton.
     */
    private Timer() {
        updatePeriod = 50;
        pause = false;
        scheduler = new ScheduledThreadPoolExecutor(1);
    }
    
    /**
     * Typical singleton method for getting instance.
     * @return the only instance of Timer
     */
    public static Timer getInstance(){
        if (instance == null){
            instance = new Timer();
        }
        return  instance;
    }
    
    /**
     * Schedule a {@code Runnable} to be executed on delay (after a time period),
     * @param period the time of delay
     * @param onPeriod the {@code Runnable}  to execute
     */
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
    //    System.out.println("Start left " + leftTime);
        task = new Thread(onPeriod);

        schFtr = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (leftTime <= 0){
                    schFtr.cancel(true);   
                    task.start();
                    //System.out.println(System.currentTimeMillis() - start);
                    //unset
                }
                if (pause){
                    //System.out.println("paused");
                } else {
                    //decrease time                    
                    leftTime -= updatePeriod;
                 //   System.out.println("-> " + leftTime);
                }
            }
        }, updatePeriod, updatePeriod, TimeUnit.MILLISECONDS);
    }
    
    public long getLeftTime(){
        return leftTime;
    }
    
    /**
     * Run the scheduled {@code Runnable} now.
     */
    public void skip(){
        if (schFtr.isDone()){
            return;
        }
        schFtr.cancel(true);
        task.start();
    }
    
    /**
     * Cancel run of the {@code Runnable}.
     */
    public void unset(){
        if ((schFtr != null) && (!schFtr.isDone())){
            schFtr.cancel(true);
            pause = false;
        }
    }
    
    /**
     * Is the {@code Runnable} is going to be ran?
     * @return {@code true} if yes, {@code false} otherwise
     */
    public boolean isSet(){
        return  schFtr.isDone();
    }

    /** 
     * Is the timer paused?
     * @return {@code true} if yes, {@code false} otherwise
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * "Stop" the timer. The time period before execution of the {@code Runnable}
     * doe snot change unless it is unpaused
     * @param pause if {@code true} pause otherwise unpause
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }
       
}
