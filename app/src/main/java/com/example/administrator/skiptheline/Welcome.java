package com.example.administrator.skiptheline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class Welcome extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        countMemory();
        showWelcomePage();
        delayAndStart();
    }
    private void countMemory(){
        int MaxMemory=(int)((Runtime.getRuntime().maxMemory()/1024)/1024);
        Log.d("MemoryTest","max memory is:"+MaxMemory);
        //max memory is 192MB on Redmi note3
        int freeMemory=(int)(Runtime.getRuntime().freeMemory()/1024);
        Log.d("MemoryTest","Welcome activity:free runtime memory:"+freeMemory);
    }

    private void showWelcomePage(){
        setContentView(R.layout.welcome);
    }

    private void delayAndStart(){
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(Welcome.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(timerTask,3000);

    }
}
