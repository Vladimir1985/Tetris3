package com.example.vladimir.struct;

import com.example.vladimir.logic.LogicConst;

public class CurrentState {


    boolean endOfTurn=false;
    //Массив следующих трех фигур которые выпадут
    int[]mFigures=new int[LogicConst.COUNT_FIGURES];

    //Тип текущего объекта
    int mCurrentFigure;

    //Текущие очки
    int mScore=0;
    //Текущий уровень
    int mLevel=1;

    //Класс игрового объекта. Это класс родитель.
   transient GameObject mCurrentObject;

    //Матрица отвечающая за отображение игрового поля. Если элемент матрицы равен "true" то значит в этой ячейке находится объект,
    // если "false" то нет
    boolean[][] mGamePlane;

    public void SetEndOfTurn(boolean b){
        endOfTurn=b;
    }

    public boolean GetEndOfTurn(){
        return  endOfTurn;
    }

    public CurrentState(){
        mGamePlane=new boolean[LogicConst.WIDTH_GAMEFIELD][LogicConst.HEIGHT_GAMEFIELD];
    }

    public int [] GetFigures(){
        return  mFigures;
    }

    public void SetFigures(int[]f){
        for(int i=0;i<f.length;i++)
            mFigures[i]=f[i];
    }

    public void SetGamePanel(int coorX,int coorY,boolean value){
        mGamePlane[coorX][coorY]=value;
    }

    public boolean GetGamePanel(int coorX,int coorY){
        return  mGamePlane[coorX][coorY];
    }

    public boolean[][] GetPanel(){
        return  mGamePlane;
    }

    public void SetPanel(boolean b[][]){
        for (int i = 0; i < LogicConst.WIDTH_GAMEFIELD; i++)
            for (int j = 0; j < LogicConst.HEIGHT_GAMEFIELD; j++)
                mGamePlane[i][j] = b[i][j];
    }

    public int GetLevel(){
        return  mLevel;
    }

    public int GetScore(){
        return mScore;
    }

    public void SetScore(int i){
        mScore=i;
    }

    public void SetLevel(int level){
        this.mLevel=level;
    }

    public int GetCurrentFigure(){
        return  mCurrentFigure;
    }

    public void SetCurrentFigure(int figure){
        mCurrentFigure=figure;
    }

    public GameObject GetObject(){

        return mCurrentObject;
    }

    public  void SetObject(GameObject gO){
        mCurrentObject=gO;

    }

}
