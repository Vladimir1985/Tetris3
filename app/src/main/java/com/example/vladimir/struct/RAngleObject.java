package com.example.vladimir.struct;

/**
 * Created by Vladimir on 27.07.2017.
 */

public class RAngleObject extends GameObject {

    public  RAngleObject()
    {
        objCoord=new int[4][2];
        objRotation=new int[4][2];

        objCoord[0][0]=6;
        objCoord[0][1]=0;

        objCoord[1][0]=5;
        objCoord[1][1]=0;

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

                objRotation[1][0]=objCoord[2][0]+1;
                objRotation[1][1]=objCoord[2][1];

                objRotation[2][0]=objCoord[2][0];
                objRotation[2][1]=objCoord[2][1];

                objRotation[3][0]=objCoord[2][0]-1;
                objRotation[3][1]=objCoord[2][1];
                rotationOption=1;break;
            case 1:
                objRotation[0][0]=objCoord[2][0]-1;
                objRotation[0][1]=objCoord[2][1]+1;

                objRotation[1][0]=objCoord[2][0];
                objRotation[1][1]=objCoord[2][1]+1;

                objRotation[2][0]=objCoord[2][0];
                objRotation[2][1]=objCoord[2][1];

                objRotation[3][0]=objCoord[2][0];
                objRotation[3][1]=objCoord[2][1]-1;
                rotationOption=2;break;
            case 2:
                objRotation[0][0]=objCoord[2][0]-1;
                objRotation[0][1]=objCoord[2][1]-1;

                objRotation[1][0]=objCoord[2][0]-1;
                objRotation[1][1]=objCoord[2][1];

                objRotation[2][0]=objCoord[2][0];
                objRotation[2][1]=objCoord[2][1];

                objRotation[3][0]=objCoord[2][0]+1;
                objRotation[3][1]=objCoord[2][1];
                rotationOption=3;break;
            case 3:
                objRotation[0][0]=objCoord[2][0]+1;
                objRotation[0][1]=objCoord[2][1]-1;

                objRotation[1][0]=objCoord[2][0];
                objRotation[1][1]=objCoord[2][1]-1;

                objRotation[2][0]=objCoord[2][0];
                objRotation[2][1]=objCoord[2][1];

                objRotation[3][0]=objCoord[2][0];
                objRotation[3][1]=objCoord[2][1]+1;
                rotationOption=0;break;
        }
        return objRotation;
    }
}
