package com.example.vladimir.struct;



import android.content.Context;
import android.content.res.Resources;

import com.example.vladimir.tetris.R;

//Класс-родитель объектов
public abstract class GameObject
{
    private Context context;
    //Матрица содержащая координаты текущего объекта в повернутом состоянии
    protected int [][]objRotation;

    //Флаг фазы поворота объекта. Если 0 то объект не повернут
    protected int rotationOption=0;
    //Матрица координат текущего обьекта вида [x][y]
    int [][]objCoord;

    public  int [][]GetObjectCoord()
    {
        return  objCoord;
    }
    public  void SetObjectCoord(int[][] c)
    {
        for(int i=0;i<c.length;i++)
        {
            objCoord[i][0]=c[i][0];
            objCoord[i][1]=c[i][1];
        }
    }

    public GameObject()
    {

    }
// Сдвигаем координаты объекта вниз
    public void Down()
    {
        for(int i=0;i<objCoord.length;i++)
            objCoord[i][1] += 1;
    }
    // Сдвигаем координаты объекта влево
    public void Left()
    {
        for(int i=0;i<objCoord.length;i++)
            objCoord[i][0] -= 1;
    }
    // Сдвигаем координаты объекта вправо
    public void Right()
    {
        for(int i=0;i<objCoord.length;i++)
            objCoord[i][0] += 1;
    }


//Функция возвращает массив координат текущего объекта в повернутом состоянии
    public int[][]IfRotation()
    {
        return objRotation;
    }
}

