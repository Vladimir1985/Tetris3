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
    public int level=1;

    //Класс игрового объекта. Это класс родитель.
   public transient GameObject currentObject;

    //Матрица отвечающая за отображение игрового поля. Если элемент матрицы равен "true" то значит в этой ячейке находится объект,
    // если "false" то нет
    public boolean[][] gamePlane;

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
//transient
   public CurrentState()
    {
        gamePlane=new boolean[width][height];
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
        gamePlane[coorX][coorY]=value;
    }

    public boolean GetGamePanel(int coorX,int coorY)
    {
        return  gamePlane[coorX][coorY];
    }

    public boolean[][] GetPanel()
    {
        return  gamePlane;
    }

    public void SetPanel(boolean b[][])
    {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                gamePlane[i][j] = b[i][j];
    }

    public  void IncrementScore()
    {
        score+=scoreStep;
        if(score%levelThreshold==0)
            level++;
    }

    public int GetLevel()
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
        level=score/levelThreshold+1;
    }

    public void SetLevel(int level)
    {
        this.level=level;
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

}
