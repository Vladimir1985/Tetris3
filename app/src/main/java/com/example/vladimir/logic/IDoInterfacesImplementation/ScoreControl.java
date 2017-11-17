package com.example.vladimir.logic.IDoInterfacesImplementation;

import com.example.vladimir.struct.CurrentState;


public final class ScoreControl implements IDo {
    //Количество очков добавляемых за одну собранную линию
    final static int  SCORE_STEP=100;
    //Количество очков необходимое для перехода на следующий уровень
    final static int LEVEL_TRESHHOLD=100;
    //Стартовый уровень
    final static int START_LEVEL=1;


    public CurrentState Do(CurrentState state){
       int mScore=state.GetScore();
       int mLevel=state.GetLevel();
        mScore+=SCORE_STEP;
        mLevel=(mScore/LEVEL_TRESHHOLD)+START_LEVEL;

        state.SetScore(mScore);
        state.SetLevel(mLevel);
        return  state;

    }

}
