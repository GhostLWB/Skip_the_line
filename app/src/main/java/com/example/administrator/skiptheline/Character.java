package com.example.administrator.skiptheline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class Character {
    private GameView gameView;
    public boolean isHideFlag;
    public Character(GameView initgameView){
        this.gameView=initgameView;
    }
    public void draw(Canvas canvas,boolean flag){
        if (flag) {
            canvas.drawBitmap(gameView.characterBitmap, gameView.characterposX,gameView.characterposY, new Paint());
            this.isHideFlag=false;
        }
        else
        {
            canvas.drawBitmap(gameView.characterhideBitemap,gameView.characterposX,gameView.characterposY,new Paint());
            this.isHideFlag=true;
        }
    }

    public int getY() {
        return gameView.characterposY+30;
    }


}
