package com.example.administrator.skiptheline;

import android.content.Context;
import android.util.Log;

import java.util.Random;

/**
 * Created by 黎文彬 on 2017/1/19 0019.
 */
public class GenerateLineThread extends Thread {
    private boolean nonEndFlag;
    private boolean pauseFlag;
    private GameView gameView;
    private Random random=new Random();
    private int lineType;
    private Object mGenraThreadLock;
    private int generateTotal=0;

    public GenerateLineThread(GameView initgameView){
        this.gameView=initgameView;
        this.mGenraThreadLock=new Object();
        this.nonEndFlag=true;
        this.pauseFlag=false;
        generateTotal=0;
    }

    public void setnonEndFlag(boolean flag){
        this.nonEndFlag=flag;
    }
    public void setPauseFlag(boolean flag) {
        this.pauseFlag = flag;
    }

    public void setGenerateTotal(int generateTotal) {
        this.generateTotal = generateTotal;
    }

    public void resumeThread(){
        synchronized (mGenraThreadLock){
            setPauseFlag(false);
            mGenraThreadLock.notify();
        }

    }
    @Override
    public void run() {
        while(nonEndFlag) {
            if (pauseFlag) {
                synchronized (mGenraThreadLock) {
                    try {
                        mGenraThreadLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                try {
                    if (generateTotal<=15){
                        lineType= (Math.abs(random.nextInt())) % 3;
                    }else {
                        lineType = (Math.abs(random.nextInt())) % 5;
                    }
                    Log.d("GenerateLineThread:", "line type:" + lineType);
                    gameView.lines.add(new Lines(lineType, gameView));
                    generateTotal++;
                } catch (Exception e) {
                }
                try {
                    Thread.sleep(Contsance.getInstance().GenerateLineSpan);
                } catch (Exception e) {
                }
            }
        }
        super.run();
    }
}
