package com.example.administrator.skiptheline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.io.BufferedReader;
import java.io.File;
import java.util.Vector;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class CheckRecord extends Activity{
    private ListView recordList;
    private String readFile;
    private String[] record;
    private String[] noRecord={"暂无记录"};
    private ImageButton back;
    private ImageButton menu;
    private Context context;
    private CommuninateWithFile communinateWithFile;
    private PopupMenu popmenu;
    private Menu mymenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.showrecord);
        recordList=(ListView)findViewById(R.id.recordlist);
        back=(ImageButton)findViewById(R.id.backbutton);
        menu=(ImageButton)findViewById(R.id.menubutton);
        context=this;
        popmenu=new PopupMenu(context,menu);
        mymenu=popmenu.getMenu();
        MenuInflater menuInflater=new MenuInflater(context);
        menuInflater.inflate(R.menu.menu,mymenu);


        showRecord();

        popmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.clear:
                        clear();
                        break;
                    case R.id.sortbyscore:
                        sortByScore();
                        break;
                    case R.id.sortbydate:
                        sortByDate();
                        break;
                }
                return false;
            }


        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MenuActivity.class);
                startActivity(intent);
                System.gc();
                finish();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popmenu.show();
            }
        });

    }




    private void showRecord(){
        communinateWithFile=new CommuninateWithFile(Contsance.getInstance().recordFileName,this);

        try {
            readFile=communinateWithFile.readDataFromFile(this);
            record=readFile.split("/");
            Log.d("CheckRecord:","length of array record:"+record.length);
            show(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String[] getFormatResult(String[] recordresult){
        String[] temp;
        String tempString;
        Vector<String> recordShow=new Vector<>();
        String[] result;


        for(int i=0;i<recordresult.length;i++){
            Log.d("CheckRecord:","length of recordresult:"+recordresult.length);
            temp=recordresult[i].split(",");
            Log.d("CheckRecord:",temp[0]+"    "+temp[1]);
            switch (temp[0].length()){
                case 1:
                    tempString="score:"+temp[0]+"       "+"time:"+temp[1]+"    "+temp[2];
                    break;
                case 2:
                    tempString="score:"+temp[0]+"     "+"time:"+temp[1]+"    "+temp[2];
                    break;
                case 3:
                    tempString="score:"+temp[0]+"   "+"time:"+temp[1]+"    "+temp[2];
                    break;
                case 4:
                    tempString="score:"+temp[0]+"  "+"time:"+temp[1]+"    "+temp[2];
                    break;
                case 5:
                    tempString="score:"+temp[0]+" "+"time:"+temp[1]+"    "+temp[2];
                    break;
                case 6:
                    tempString="score:"+temp[0]+""+"time:"+temp[1]+"    "+temp[2];
                    break;
                case 7:
                    tempString="score:"+temp[0]+""+"time:"+temp[1]+"    "+temp[2];
                    break;
                default:
                    tempString="score:"+temp[0]+" "+"time:"+temp[1]+"    "+temp[2];
                    break;
            }
            Log.d("CheckRecord:","length of vector recordShow:"+recordShow.size());
            recordShow.add(tempString);
        }
        result=new String[recordShow.size()];
        for(int i=0;i<recordShow.size();i++){
            result[i]=recordShow.get(i);
        }
        return result;
    }
    public void show(String[] recordresult){//输入的是原始的未提取过的数组

        String[] recordShowinit=getFormatResult(recordresult);

        Log.d("CheckRecord:","length of recordShowinit:"+recordShowinit.length);

        if (recordresult.length==1)  {
            recordList.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,noRecord));
        }else {
            recordList.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, recordShowinit));
        }
    }
    private void clear(){
        String s="";
        CommuninateWithFile clearCommunicate=new CommuninateWithFile(Contsance.getInstance().recordFileName,this.context);
        clearCommunicate.writeDataToFile(s,context);
        recordList.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,noRecord));

    }
    private void sortByDate() {
        String[] recordReverse;
        String[] arrayInit=getFormatResult(record);
        recordReverse=new String[arrayInit.length];

        for(int i=0;i<record.length;i++){
            recordReverse[i]=arrayInit[record.length-i-1];
        }
        recordList.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,recordReverse));

    }

    private void sortByScore() {
        Log.d("CheckRecord:","sort by score method called......");
        int maxRecordindex=0;
        int i;
        String temp;
        String[] recordSorted=new String[record.length];
        String[] display;

        for (int k=0;k<record.length;k++){
            recordSorted[k]=record[k];
        }

        for (int j=0;j<recordSorted.length;j++ ){
            maxRecordindex =j;
            for (i = j; i < recordSorted.length; i++) {
                Log.d("CheckRecord:","1--getScore(recordSorted[i+1]):"+getScore(recordSorted[i]));
                Log.d("CheckRecord:","2--getScore(recordSorted[maxRecordindex]):"+getScore(recordSorted[maxRecordindex]));
                if (getScore(recordSorted[i])>getScore(recordSorted[maxRecordindex])){
                    maxRecordindex=i;
                }
            }
            Log.d("CheckRecord:","max score:"+getScore(recordSorted[maxRecordindex]));
            if (maxRecordindex!=i){
                temp=recordSorted[j];
                recordSorted[j]=recordSorted[maxRecordindex];
                recordSorted[maxRecordindex]=temp;
            }
        }
        display=getFormatResult(recordSorted);
        recordList.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,display));


    }
    private int getScore(String string){
        String[] temp;
        int score;

        temp=string.split(",");
        score=Integer.parseInt(temp[0]);
        return score;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            Intent intent=new Intent(this,MenuActivity.class);
            startActivity(intent);
            System.gc();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
