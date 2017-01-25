package com.example.administrator.skiptheline;

import android.util.Log;

/**
 * Created by 黎文彬 on 2017/1/21 0021.
 */
public class TimeThread extends Thread{
    private Object mTimePauseLock;
    private boolean nonEndFlag=false;
    private GameView gameView;
    private boolean pauseFlag;
    public TimeThread(GameView gameView){
        this.gameView=gameView;
        this.mTimePauseLock=new Object();
        this.pauseFlag=false;
        this.nonEndFlag=true;
    }
    public void setnonEndFlag(boolean flag) {
        this.nonEndFlag = flag;
    }
    public void setPauseFlag(boolean flag){
        this.pauseFlag=flag;
    }
    public void resumeThread(){
        synchronized (mTimePauseLock){
            setPauseFlag(false);
            mTimePauseLock.notify();
        }
    }
    @Override
    public void run() {
        while(nonEndFlag) {
            if (pauseFlag) {
                synchronized (mTimePauseLock) {
                    try {
                        mTimePauseLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                    try {
                        gameView.timeCountAdd();
                        } catch (Exception e) {
                         }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        }
                }
        }
        super.run();
    }
}
