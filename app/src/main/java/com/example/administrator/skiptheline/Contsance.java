package com.example.administrator.skiptheline;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */

/**
 * 刷屏线程span 5
 * 直线运动span 10
 * 产生直线span 800
 * 计时线程span 1
 *
 */
public class Contsance {
    private  static Contsance instance=null;
    public final int sleepPeriod=1;
    public final int characterSize=250;
    public final int GenerateLineSpan=800;
    //public final int widgetSize=100;
    public final int spanblue=10;
    public final int spangreen=13;
    public final int spanorange=17;
    public final int spanpink=20;
    public final int spanpurple=25;
    public final int gap=160;
    //public final int characterPosY=1200;
    public final int infinite=999999999;
    public final int pauseButtonX=940;
    public final int pauseButtonY=23;
    public final String recordFileName="Skip_the_line.txt";
    public final int through=1;
    public final int touch=2;
    public final int breakrecord=3;
    public final int end=4;




    private Contsance(){
        //init;
    }
  public static Contsance getInstance(){
      if (instance==null)
          return new Contsance();
      else{
          return instance;
      }
  }

}
