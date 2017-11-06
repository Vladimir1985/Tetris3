package com.example.vladimir.struct;



public class RZObject extends GameObject {

    public RZObject()
    {
        //Стартовые координаты текущего объекта
        objCoord=new int[4][2];
        objRotation=new int[4][2];

        objCoord[0][0]=6;
        objCoord[0][1]=0;

        objCoord[1][0]=6;
        objCoord[1][1]=1;

        objCoord[2][0]=5;
        objCoord[2][1]=1;

        objCoord[3][0]=5;
        objCoord[3][1]=2;



    }

    public int[][]IfRotation()
    {
        switch (rotationOption)
        {
            case 0:
                objRotation[0][0]=objCoord[2][0]+1;
                objRotation[0][1]=objCoord[2][1]+1;

                objRotation[1][0]=objCoord[2][0];
                objRotation[1][1]=objCoord[2][1]+1;

                objRotation[2][0]=objCoord[2][0];
                objRotation[2][1]=objCoord[2][1];

                objRotation[3][0]=objCoord[2][0]-1;
                objRotation[3][1]=objCoord[2][1];
                rotationOption=1;break;
            case 1:
                objRotation[0][0]=objCoord[2][0]+1;
                objRotation[0][1]=objCoord[2][1]-1;

                objRotation[1][0]=objCoord[2][0]+1;
                objRotation[1][1]=objCoord[2][1];

                objRotation[2][0]=objCoord[2][0];
                objRotation[2][1]=objCoord[2][1];

                objRotation[3][0]=objCoord[2][0];
                objRotation[3][1]=objCoord[2][1]+1;
                rotationOption=0;break;

        }
        return objRotation;
    }
}
