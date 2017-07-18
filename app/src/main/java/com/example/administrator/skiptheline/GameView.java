package com.example.administrator.skiptheline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private MoveThread moveThread;//直线运动线程
    private DrawThread thread;//刷屏线程
    private GenerateLineThread generateLineThread;//产生直线的线程
    private  TimeThread timeThread;//计时线程
    public Bitmap[] linepicture=new Bitmap[5];
    private Bitmap scoreBitmap;
    private Bitmap timeBitmap;
    private Bitmap pauseButtonBitmap;
    private Bitmap pauseBGBitmap;
    private boolean characterFlag=true;//characterflag为true的时候显示为非隐藏
    private Bitmap[] musicBitmap=new Bitmap[2];
    private int score=0;
    Paint paint;
    Paint textPaint=new Paint();
    Paint textPaintResult=new Paint();
    Bitmap characterBitmap;
    Bitmap characterhideBitemap;
    Bitmap gameoverBitmap;
    Bitmap gamebgBitmap;
    Bitmap[] readyBitmap=new Bitmap[3];
    private WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
    private int scrwidth=wm.getDefaultDisplay().getWidth();
    private int scrheigth=wm.getDefaultDisplay().getHeight();
    ArrayList<Lines> lines=new ArrayList<>();//直线的集合
    private Character character=new Character(this);
    public int timeCount=0;
    Typeface textTypeface;
    private int timeStart=Contsance.getInstance().infinite;
    public boolean pauseFlag;
    private boolean gameoverFlag;
    private boolean readypicRecycleFlag;
    CommuninateWithFile communinateWithFile;
    int systemsecondStart;
    int systemsecondcurrent;
    int heighestrecord;
    int characterposY;
    int characterposX;
    int pausebuttonposX;
    int widgetSize;
//    Bitmap bufferBitmap;
//    Canvas bufferCanvas;
    //private Paint bgPaint=new Paint();
    boolean bgmusicFlag;
    boolean breakrecordFlag;
    SoundPool soundPool;
    HashMap<Integer,Integer> soundPoolMap;

    public GameView(MainActivity activity){
        super(activity);

        getHolder().addCallback(this);//注册接口
        thread=new DrawThread(getHolder(),this);//初始化刷帧线程
        moveThread=new MoveThread(this);//初始化移动的线程,move线程管画面所有的物体的运动
        generateLineThread=new GenerateLineThread(this);//初始化产生直线的线程
        timeThread=new TimeThread(this);//初始化计时线程
        countMemory("before add things in game:");
        textTypeface=activity.typeface;
        textPaint.setTypeface(textTypeface);
        textPaint.setARGB(255,255,255,255);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(70);

        textPaintResult.setTypeface(textTypeface);
        textPaintResult.setARGB(255,255,255,255);
        textPaintResult.setAntiAlias(true);
        textPaintResult.setTextSize(70);

        systemsecondStart=getSystemcurrentsecond();

        pauseFlag=false;
        gameoverFlag=false;
        readypicRecycleFlag=false;
        bgmusicFlag=false;
        breakrecordFlag=false;

        widgetSize=(int)(0.0625*scrheigth);
        Log.d("GameView:","widgetSize:scrwidth"+scrwidth);
        Log.d("GameView:","widgetSize:scrheight"+scrheigth);
        Log.d("GameView:","widgetSize:"+widgetSize);
        characterposX=(scrwidth/2)-(Contsance.getInstance().characterSize/2);
        characterposY=(int)(0.625*scrheigth)-(Contsance.getInstance().characterSize/2);
        pausebuttonposX=scrwidth-widgetSize-20;
        //bgPaint.setARGB(255,22,22,22);

//        bufferBitmap=Bitmap.createBitmap(scrwidth,scrheigth, Bitmap.Config.ARGB_8888);
//        bufferCanvas=new Canvas(bufferBitmap);
        iniBitmap();
        initSounds();
        //heighestrecord=getHeighestrecord();

        communinateWithFile=new CommuninateWithFile(Contsance.getInstance().recordFileName,activity);
    countMemory("after add things to game:");
    }

    public void initSounds(){
        soundPool=new SoundPool(4, AudioManager.STREAM_MUSIC,100);
        soundPoolMap=new HashMap<>();
        soundPoolMap.put(1,soundPool.load(getContext(),R.raw.sound1,1));
        soundPoolMap.put(2,soundPool.load(getContext(),R.raw.touch3,1));
        soundPoolMap.put(3,soundPool.load(getContext(),R.raw.breaktherecord,1));
        soundPoolMap.put(4,soundPool.load(getContext(),R.raw.end,1));
    }
    public void playSound(int sound,int loop){
        AudioManager audioManager=(AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume=streamVolumeCurrent/streamVolumeMax;
        soundPool.play(soundPoolMap.get(sound),volume,volume,1,loop,1);
    }
    private void iniBitmap() {
        paint=new Paint();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 3;
        readyBitmap[0]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ready1),100,270);
        readyBitmap[1]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ready2),180,270);
        readyBitmap[2]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ready3),180,270);
        gamebgBitmap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bg,opts),scrwidth,scrheigth);
        characterBitmap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.character),Contsance.getInstance().characterSize,Contsance.getInstance().characterSize);
        linepicture[0]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.lineblue,opts),scrwidth,20);
        linepicture[1]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.linegreen,opts),scrwidth,20);
        linepicture[2]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.lineorange,opts),scrwidth,20);
        linepicture[3]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.linepink,opts),scrwidth,20);
        linepicture[4]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.linepurle,opts),scrwidth,20);
        scoreBitmap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.score,opts),widgetSize,widgetSize);
        timeBitmap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.time,opts),widgetSize-9,widgetSize+2);
        //musicBitmap[0]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.music,opts),widgetSize,widgetSize);
        //musicBitmap[1]=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.nonmusci,opts),widgetSize,widgetSize);
        characterhideBitemap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.characterhide),Contsance.getInstance().characterSize,Contsance.getInstance().characterSize);
        pauseButtonBitmap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pause,opts),widgetSize-10,widgetSize);
        pauseBGBitmap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.pausebg),scrwidth,scrheigth);
        gameoverBitmap=Ulity.resizeBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.gameover),scrwidth,scrheigth);
    }
    public void onDraw(Canvas canvas){//绘画方法，指定要将哪些元素画在画面上
        drawOnBuffer(canvas);
        //canvas.drawBitmap(bufferBitmap,0,0,new Paint());

        canvas.drawText(Ulity.getTime(timeCount),(int)(((0.333)*scrwidth)+widgetSize+20),(int)(widgetSize/1.1),textPaint);//绘画时间
        canvas.drawText(score+"",widgetSize+70,(int)(widgetSize/1.1),textPaint);//绘画分数

       if((timeCount-timeStart)>=Contsance.getInstance().gap){
           this.setCharacterFlag(true);
           timeStart=Contsance.getInstance().infinite;
       }
        character.draw(canvas,characterFlag);
        //Log.d("GameView:","drawing lines ,number of lines in Gameview:"+lines.size());
        for (Lines l:lines) {
            l.draw(canvas,l.type);
        }

        if(pauseFlag)
            canvas.drawBitmap(pauseBGBitmap,0,0,paint);
        if (gameoverFlag){
            canvas.drawBitmap(gameoverBitmap,0,0,paint);
            canvas.drawText("score:"+this.getScore()+"  time:"+Ulity.getTime(timeCount),(int)(0.25*scrwidth),(int)(0.600*scrheigth),textPaintResult);
        }
        if (systemsecondcurrent<systemsecondStart){
            systemsecondcurrent+=60;
        }
        drawReadyScene(canvas);

    }
    public void drawOnBuffer(Canvas canvas){
        canvas.drawBitmap(gamebgBitmap,0,0,paint);//绘画背景
        //canvas.drawRect(0,0,0,0,bgPaint);
        canvas.drawBitmap(scoreBitmap,50,20,paint);//画分数图标
        canvas.drawBitmap(timeBitmap,(int)(0.333*scrwidth),23,paint);//画计时图标
        //canvas.drawBitmap(musicBitmap[1],800,23,paint);//画背景音乐图标
        canvas.drawBitmap(pauseButtonBitmap,pausebuttonposX,Contsance.getInstance().pauseButtonY,paint);//画暂停按钮图标
    }

    public int getScore() {
        return score;
    }

    public void setCharacterFlag(boolean characterFlag) {
        this.characterFlag = characterFlag;
    }

    public void setPauseFlag(boolean flag){
        this.pauseFlag=flag;
    }

    public int getCharacterposY(){
        return  characterposY;
    }
    public void pause() {
        moveThread.setPauseFlag(true);
        generateLineThread.setPauseFlag(true);
        timeThread.setPauseFlag(true);
        this.setPauseFlag(true);
    }
    public void resume(){
        Log.d("GameView:","method resume called");
        this.generateLineThread.resumeThread();
        this.moveThread.resumeThread();
        this.timeThread.resumeThread();
        this.setPauseFlag(false);

    }

    public void addScore(int score) {
        this.score += score;
    }

    private void countMemory(String tag){
        int freeMemory=(int)(Runtime.getRuntime().freeMemory()/1024);
        Log.d("MemoryTest",tag+" free runtime memory:"+freeMemory+"k");
    }

    /**
     * 当线程的flag为TRUE的时候线程运行
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {//当surfaceview创建的时候，所有的线程开始运行
        if(this.thread!=null) {
            this.thread.setFlag(true);
            this.thread.start();
        }
        setCharacterFlag(true);
    }
    public void threadStart(){
        this.moveThread.setnonEndFlag(true);
        this.moveThread.setPauseFlag(false);
        this.moveThread.start();

        this.generateLineThread.setnonEndFlag(true);
        this.generateLineThread.setPauseFlag(false);
        this.generateLineThread.start();

        this.timeThread.setnonEndFlag(true);
        this.timeThread.setPauseFlag(false);
        this.timeThread.start();


    }
    public String getTimeandDate(){
        SimpleDateFormat   formatter   =   new SimpleDateFormat("MM月dd日HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   timeandDate   =   formatter.format(curDate);

        return timeandDate;
    }
    public int getSystemcurrentsecond(){
        String time=getTimeandDate();
        int secondcurrent;
        String[] timeset=time.split(":");

        secondcurrent=Integer.parseInt(timeset[2]);

        return secondcurrent;
    }
    public void endGame(){
        playSound(Contsance.getInstance().end,0);
        String timeandDate=getTimeandDate();

        String record=score+","+Ulity.getTime(timeCount)+","+timeandDate+"/";
        communinateWithFile.writeDataToFile(record,getContext(),true);

        moveThread.setPauseFlag(true);
        generateLineThread.setPauseFlag(true);
        generateLineThread.setGenerateTotal(0);
        timeThread.setPauseFlag(true);

        this.lines.clear();
        gameoverFlag=true;


    }
    public void restartGame(){
        this.timeCount=0;
        this.score=0;
        systemsecondStart=getSystemcurrentsecond();

        gameoverFlag=false;
        setCharacterFlag(true);
        readypicRecycleFlag=false;
        breakrecordFlag=false;
        //heighestrecord=getHeighestrecord();

        resume();
    }
    public void drawReadyScene(Canvas canvas){
        //playSound(Contsance.getInstance().recount,0);
        if (!readypicRecycleFlag&&(systemsecondcurrent-systemsecondStart)<=3){
            Paint readyPaint=new Paint();
            Log.d("GameView:","start drawing ready picture:");
            if ((systemsecondcurrent-systemsecondStart)<=1){
                Log.d("GameView:","start drawing ready picture  1:");
                canvas.drawBitmap(readyBitmap[2],(scrwidth/2)-90,(scrheigth/2)-135,readyPaint);
            }else if ((systemsecondcurrent-systemsecondStart)<=2){
                Log.d("GameView:","start drawing ready picture  2:");
                canvas.drawBitmap(readyBitmap[1],(scrwidth/2)-90,(scrheigth/2)-135,readyPaint);
            }else if ((systemsecondcurrent-systemsecondStart)<=3){
                Log.d("GameView:","start drawing ready picture  3:");
                canvas.drawBitmap(readyBitmap[0],(scrwidth/2)-90,(scrheigth/2)-135,readyPaint);
            }
        }
        Log.d("GameView:","1--system time start:"+systemsecondStart);
        Log.d("GameView:","2--system time current:"+systemsecondcurrent);
        if ((systemsecondcurrent-systemsecondStart)==4){
            Log.d("GameView:","systemsecondcurrent-systemsecondStart==4");
            if (!readypicRecycleFlag){
                recycleReadyBitmap();
                threadStart();
            }

        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(this.thread!=null) {
            this.thread.setFlag(false);
        }
        moveThread.setnonEndFlag(false);
        moveThread.interrupt();

        generateLineThread.setnonEndFlag(true);
        generateLineThread.interrupt();

        this.setCharacterFlag(false);
        timeThread.setnonEndFlag(false);
        timeThread.interrupt();

        recycleBitmap();
    }

    /**
     * 当触摸屏幕的时候的处理方法
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventtype=event.getAction();
        int touchPosX;
        int touchPosY;



        switch (eventtype){
            case MotionEvent.ACTION_DOWN:
                touchPosX=(int)event.getRawX();
                touchPosY=(int)event.getRawY();

                playSound(Contsance.getInstance().touch,0);
                Log.d("GameView:","touch X position:"+touchPosX);
                Log.d("GameView:","touch Y position:"+touchPosY);
                Log.d("GameView:","pause flag status:"+pauseFlag);
                if (gameoverFlag){
                    restartGame();
                }
                else {
                    if (pauseFlag) {
                        setPauseFlag(false);
                        this.resume();
                    } else {

                        if ((touchPosX <= (Contsance.getInstance().pauseButtonX + widgetSize + 30))
                                && (touchPosX >= (Contsance.getInstance().pauseButtonX))
                                && (touchPosY <= ((Contsance.getInstance().pauseButtonY + widgetSize + 60)))
                                && (touchPosY >= Contsance.getInstance().pauseButtonY)) {
                            this.pause();
                        } else {
                            this.timeStart = this.timeCount;
                            this.setCharacterFlag(false);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;


        }


        return true;
    }

    public int getScrheigth(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }
    public int getScrwidth(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public void timeCountAdd() {
        this.timeCount++;
    }
    public Character getCharacter(){
        return this.character;
    }
    public void recycleBitmap(){
        gameoverBitmap.recycle();
        scoreBitmap.recycle();
        timeBitmap.recycle();
        pauseBGBitmap.recycle();
        gamebgBitmap.recycle();
        characterhideBitemap.recycle();
        characterBitmap.recycle();
        for (Bitmap iterator:linepicture){
            iterator.recycle();
        }
       // bufferBitmap.recycle();
        System.gc();
    }
    public void recycleReadyBitmap(){
        readyBitmap[0].recycle();
        readyBitmap[1].recycle();
        readyBitmap[2].recycle();
        readypicRecycleFlag=true;
        System.gc();
    }
}



/**
 * 这个线程类专门拿来刷画面，跟gameView类里面的onDraw方法一起用
 */
class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean flag;

    public DrawThread(SurfaceHolder surfaceHolder, GameView gameView) {
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas c;
        while (true) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas();
                synchronized (this.surfaceHolder) {
                    gameView.onDraw(c);
                    gameView.systemsecondcurrent=gameView.getSystemcurrentsecond();

                }
            } catch (Exception e) {
            } finally {
                if (c != null)
                    this.surfaceHolder.unlockCanvasAndPost(c);
            }
            try {
                Thread.sleep(Contsance.getInstance().sleepPeriod);
            } catch (Exception e) {
                e.printStackTrace();//打印堆栈信息}

            }
        }
    }
}
