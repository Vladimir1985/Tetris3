package com.example.vladimir.struct;


public class CubeObject extends GameObject
{

    public CubeObject()
    {
        super();

        //Стартовые координаты текущего объекта
        objCoord=new int[4][2];

        objCoord[0][0]=4;
        objCoord[0][1]=0;

        objCoord[1][0]=5;
        objCoord[1][1]=0;

        objCoord[2][0]=4;
        objCoord[2][1]=1;

        objCoord[3][0]=5;
        objCoord[3][1]=1;
    }

    public int[][]IfRotation()
    {
        return objCoord;
    }

}
