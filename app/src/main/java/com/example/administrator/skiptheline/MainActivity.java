package com.example.administrator.skiptheline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private GameView gameView;
    private Context context;
    public Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getApplicationContext();
        typeface=Typeface.createFromAsset(getAssets(),"fonts/wryh.TTF");
        gameView=new GameView(this);
        setContentView(gameView);

    }


    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            Intent intent=new Intent(this,MenuActivity.class);
            gameView.recycleBitmap();
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
