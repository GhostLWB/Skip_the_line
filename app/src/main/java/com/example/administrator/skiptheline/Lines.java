package com.example.administrator.skiptheline;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class Lines {
    int maxHeight;
    int scrwidth;
    private int y=160;
    public int type;
    private int spanY;
    private boolean countflag;
    private GameView gameView;



    public Lines(int initype,GameView gameView){
        this.type=initype;
        this.gameView=gameView;

        this.maxHeight=gameView.getScrheigth();
        this.scrwidth=gameView.getScrwidth();
        this.countflag=false;
        Log.d("line","scrwidth:"+this.scrwidth);
        switch(type){
            case 0:
                spanY=Contsance.getInstance().spanblue;
                break;
            case 1:
                spanY=Contsance.getInstance().spangreen;
                break;
            case 2:
                spanY=Contsance.getInstance().spanorange;
                break;
            case 3:
                spanY=Contsance.getInstance().spanpink;
                break;
            case 4:
                spanY=Contsance.getInstance().spanpurple;
                break;
            default:
                spanY=Contsance.getInstance().spanblue;
                break;
        }

    }


    public void draw(Canvas canvas, int type){
        switch(type){
            case 0:
                canvas.drawBitmap(gameView.linepicture[0],0,y,new Paint());
                break;
            case 1:
                canvas.drawBitmap(gameView.linepicture[1],0,y,new Paint());
                break;
            case 2:
                canvas.drawBitmap(gameView.linepicture[2],0,y,new Paint());
                break;
            case 3:
                canvas.drawBitmap(gameView.linepicture[3],0,y,new Paint());
                break;
            case 4:
                canvas.drawBitmap(gameView.linepicture[4],0,y,new Paint());
                break;
        }
    }
    public void move(){
        switch (type){
            case 0:
                spanY=Contsance.getInstance().spanblue;
                break;
            case 1:
                spanY=Contsance.getInstance().spangreen;
                break;
            case 2:
                spanY=Contsance.getInstance().spanorange;
                break;
            case 3:
                spanY=Contsance.getInstance().spanpink;
                break;
            case 4:
                spanY=Contsance.getInstance().spanpurple;
        }
        if(this.y<=maxHeight){
            y+=spanY;
        }
    }
    public int getY(){
        return y;
    }

    public boolean isCountflag() {
        return countflag;
    }

    public void setCountflag(boolean countflag) {
        this.countflag = countflag;
    }

    public boolean collision(GameView gameView){
    if (!gameView.getCharacter().isHideFlag) {
        if (isCollision(gameView.getCharacter().getY(), this.getY())) {
            StateMachine.getInstance().setState(StateMachine.stateEnum.result);
            Log.d("Lines:","line collisioned,line postionY:"+this.getY()+" while character positionY:"+gameView.getCharacter().getY());
            return true;
        }
    }
       return false;
    }
    private boolean isCollision(int cY,int lY){
        Log.d("Lines:","character position Y:"+cY+" line positionY:"+lY);
        if (((lY)>cY)&&((lY+20)<(cY+Contsance.getInstance().characterSize)-120)){
            return true;
        }else {
            return false;
        }
    }

}
