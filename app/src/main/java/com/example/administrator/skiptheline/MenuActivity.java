package com.example.administrator.skiptheline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class MenuActivity extends Activity {
    private Button start;
    private Button checkrecord;
    private Button exit;
    private Button about;
    private ImageView menubg;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menulayout);
        countMemory("before add imageview:");
        setBackgroundPic();
        registerWidget();
        countMemory("after add imageview:");
        setWidgetOnclick();

    }
    private void setBackgroundPic(){
        menubg=(ImageView)findViewById(R.id.menuBackground);
        menubg.setImageBitmap(ImageUtils.decodeImageSource(getResources(),
                R.drawable.menubg,1920,1080));
    }
    private void  donothing(){
        //do nothing;
    }
    private void countMemory(String tag){
        int freeMemory=(int)(Runtime.getRuntime().freeMemory()/1024);
        Log.d("MemoryTest",tag+" free runtime memory:"+freeMemory+"k");
    }

    private void registerWidget(){

        start=(Button)findViewById(R.id.startbutton);
        checkrecord=(Button)findViewById(R.id.recordbutton);
        exit=(Button)findViewById(R.id.exitbutton);
        about=(Button)findViewById(R.id.aboutbutton);
        context=this;
    }
    private void setWidgetOnclick(){
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startintent=new Intent(context,MainActivity.class);
                startActivity(startintent);
                System.gc();
                //overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                finish();
            }
        });
        checkrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkrecordintent=new Intent(context,CheckRecord.class);
                startActivity(checkrecordintent);
                System.gc();
                // overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startintent=new Intent(context,AboutActivity.class);
                startActivity(startintent);
                System.gc();
                finish();
            }
        });
    }
}
