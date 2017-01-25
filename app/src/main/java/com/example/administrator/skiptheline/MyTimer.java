package com.example.administrator.skiptheline;

import java.util.TimerTask;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class MyTimer {
    private final TimerTask task;
    private MyThread thread;
    private final int sleepPeriod;



    private class MyThread extends Thread{
        private boolean running;

        @Override
        public synchronized void start() {
            running=true;
            super.start();
        }
        public boolean isRunning(){
            return running;
        }
        public void pause(){
            running=false;
        }

        @Override
        public void run() {
            while(running) {
                long start = System.currentTimeMillis();
                task.run();
                try {
                    long end = System.currentTimeMillis();
                    if (end - start < sleepPeriod)
                        Thread.sleep(sleepPeriod - (end - start));
                } catch (Exception e) {
                }
            }
        }
    }
    public MyTimer(TimerTask t,int i){
        task=t;
        sleepPeriod=i;
    }
    public void start(){
        //如果线程为空，则new一个线程并开始；
        if(thread==null){
            thread=new MyThread();
            thread.start();
            return;
        }
        //如果线程正在运行，则暂停这个线程，并new一个线程出来开始；
        if(thread.isRunning()){
            thread.pause();
            thread=new MyThread();
            thread.start();
            return;
        }
        //如果不是以上的情况（线程存在而且没有正在运行），就new一个线程来开始；
        thread=new MyThread();
        thread.start();
    }
    public void pause(){
        if(thread!=null)
            thread.pause();
    }
    public boolean isAlive(){
        if(thread==null)
            return false;
        return thread.isAlive();
    }
}
