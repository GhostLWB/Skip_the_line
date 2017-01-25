package com.example.administrator.skiptheline;

/**
 * Created by 黎文彬 on 2017/1/18 0018.
 */
public class StateMachine {
    public enum stateEnum{
        menu,onGame,result;
    }
    private  stateEnum state;
    private static StateMachine instance=null;

    private StateMachine(){
        state=stateEnum.menu;
    }
    public static StateMachine getInstance(){
        if(instance==null){
            return new StateMachine();
        }
        else
        {
            return instance;
        }
    }

    public stateEnum getState() {
        return state;
    }

    public void setState(stateEnum state) {
        this.state = state;
    }
    public void moveToNextState(stateEnum currentState){
        switch(currentState){
            case menu:
                state=stateEnum.onGame;
                break;
            case onGame:
                state=stateEnum.result;
                break;
            case result:
                state=stateEnum.menu;
                break;
        }
    }
}
