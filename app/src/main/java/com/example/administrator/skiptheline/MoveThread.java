package com.example.administrator.skiptheline;

import android.util.Log;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class MoveThread extends Thread {
    private final int sleepspan=10;
    private Object mMoveThreadLock;
    private boolean nonEndFlag=true;
    private GameView gameView;
    private int screenheight;
    private boolean pauseFlag;


    public MoveThread(GameView initgameview){
        this.gameView=initgameview;
        this.screenheight=gameView.getScrheigth();
        this.mMoveThreadLock=new Object();
        this.pauseFlag=false;
        this.nonEndFlag=true;
    }
    public void resumeThread(){
        synchronized (mMoveThreadLock){
            this.setPauseFlag(false);
            Log.d("MoveThread:","resuming MoveThread.......");
            mMoveThreadLock.notify();
        }

    }

    @Override
    public void run() {
        while(nonEndFlag) {
            if (pauseFlag) {
                synchronized (mMoveThreadLock) {
                    try {
                        mMoveThreadLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }else {
                Log.d("MoveThread:", " end flag status：" + nonEndFlag);
                try {
                    for (Lines line : this.gameView.lines) {
                        line.move();//调用line自身的move方法使直线往下运动
                        if ((line.getY() >= gameView.getCharacterposY() + Contsance.getInstance().characterSize) && !line.isCountflag()) {
                            //当直线通过character时统计分数
                            gameView.playSound(Contsance.getInstance().through,0);
                            switch (line.type) {
                                case 0:
                                    gameView.addScore(20);
                                    break;
                                case 1:
                                    gameView.addScore(30);
                                    break;
                                case 2:
                                    gameView.addScore(40);
                                    break;
                                case 3:
                                    gameView.addScore(50);
                                    break;
                                case 4:
                                    gameView.addScore(60);
                                    break;
                            }
                            line.setCountflag(true);
                        }
                        if (line.getY() >= screenheight) {
                            gameView.lines.remove(line);//每当一条直线超过屏幕距离的时候删除该直线对象
                        }
//                        if ((gameView.getScore())>=gameView.heighestrecord){
//                            gameView.playSound(Contsance.getInstance().breakrecord,0);
//                            gameView.breakrecordFlag=true;
//                        }

                        Log.d("MoveThread:", "checking collision****");
                        Log.d("MoveThread:", "number of current line in gameView:" + gameView.lines.size());
                        if (line.collision(gameView)) {
                            gameView.endGame();
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    Thread.sleep(sleepspan);
                } catch (Exception e) {
                }
            }
        }
        super.run();
    }

    public void setnonEndFlag(boolean flag) {
        this.nonEndFlag = flag;
    }
    public void setPauseFlag(boolean flag){
        this.pauseFlag=flag;
    }
}
