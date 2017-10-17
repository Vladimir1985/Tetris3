package com.example.vladimir.struct;

public class CurrentState {

    final int width=10;
    final int height=20;

    //Количество прогнозируемых фигур
    public int countFigures=3;
    //Массив следующих трех фигур которые выпадут
    public int[]figures=new int[countFigures];

    //Тип текущего объекта
    public int currentFigure;

    //Текущие очки
    public int score=0;
    //Количество очков добавляемых за одну собранную линию
    public int scoreStep=100;
    //Количество очков необходимое для перехода на следующий уровень
    public int levelThreshold=100;
    //Текущий уровень
    public byte level=1;

    public int [][]coord;

    //Класс игрового объекта. Это класс родитель.
   public transient GameObject currentObject;

    //Матрица отвечающая за отображение игрового поля. Если элемент матрицы равен "true" то значит в этой ячейке находится объект,
    // если "false" то нет
    public boolean[][] gamePlanel;

//transient
   public CurrentState()
    {
        gamePlanel=new boolean[width][height];
    }


    public int [] GetFigures()
    {
        return  figures;
    }

    public void SetFigures(int[]f)
    {
        for(int i=0;i<f.length;i++)
            figures[i]=f[i];
    }
    public void SetGamePanel(int coorX,int coorY,boolean value)
    {
        gamePlanel[coorX][coorY]=value;
    }

    public boolean GetGamePanel(int coorX,int coorY)
    {
        return  gamePlanel[coorX][coorY];
    }

    public boolean[][] GetPanel()
    {
        return  gamePlanel;
    }

    public  void IncrementScore()
    {
        score+=scoreStep;
        if(score%levelThreshold==0)
            level++;
    }

    public byte GetLevel()
    {
        return  level;
    }

    public int GetScore()
    {
        return score;
    }

    public void SetScore(int i)
    {
        score=i;
        level=(byte)(score/levelThreshold+1);
    }

    public int GetCurrentFigure()
    {
        return  currentFigure;
    }

    public void SetCurrentFigure(int figure)
    {
        currentFigure=figure;
    }

    public GameObject GetObject()
    {
        return currentObject;
    }

    public  void SetObject(GameObject gO)
    {
        currentObject=gO;

    }

     public void SetCoords(int[][] coord)
     {
         this.coord=coord;
     }
}
