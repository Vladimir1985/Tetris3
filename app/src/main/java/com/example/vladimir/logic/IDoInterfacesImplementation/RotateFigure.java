package com.example.vladimir.logic.IDoInterfacesImplementation;

import com.example.vladimir.logic.LogicConst;
import com.example.vladimir.struct.CurrentState;


public final class RotateFigure  extends DoMove {

    public CurrentState Do(CurrentState state){
        currentState=state;
        //Поворачиваем текущий объект, и выравниваем его по краю если при повороте он вышел за пределы игрового поля
        int rot[][]= BoundaryAlignment(currentState.GetObject().IfRotation());
        //Проверяем можно ли поместить повернутый объект на игровое поле без пересечений с другими объектами
        if(CheckPosibility(rot))
        {
            //Если поворот возможен то заменяем основную фигуру повернутой и перерисовываем
            setStateCurrentObjectOnPlane(false);
            for (int i = 0; i < currentState.GetObject().GetObjectCoord().length; i++)
                for (int j = 0; j < currentState.GetObject().GetObjectCoord()[0].length; j++)
                    currentState.GetObject().GetObjectCoord()[i][j] = rot[i][j];
            setStateCurrentObjectOnPlane(true);

        }
        return currentState;
    }
    //Проверяем возможность поворота фигуры. Получаем координаты повернутого объекта. Затем проверяем эти координаты в нашем игровом поле
    //если хоть одни из них заняты и это не координаты объекта который мы хотим повернуть значит повернуть фигуру невозможно и мы возвращаем
    //"false" иначе "true". Также мы проверяем не выходят ли точки повернутого объекта за пределы игрового поля снизу
    boolean CheckPosibility(int rot[][])
    {
        boolean cell_employment_flag, cell_belong_flag;
        //Перебираем все ячейки
        for (int i = 0; i < rot.length; i++)
        {
            //Принадлежит ли ячейка нашему игровому полю
            cell_belong_flag=CheckObjectPoints(rot[i][0], rot[i][1], currentState.GetObject().GetObjectCoord());
            //Активна или нет ячейка с этими координатами
            cell_employment_flag=currentState.GetGamePanel(rot[i][0], rot[i][1]);
            if (rot[i][1] >= LogicConst.HEIGHT_GAMEFIELD || (cell_employment_flag && !cell_belong_flag))
                return false;
        }
        return true;
    }
    //В тетрисе, когда фигрура движется вертикально вдоль одной из боковых сторон и приходит сигнал на поворот, она поворачивается, при
    //этом отодвигаясь от края игрового поля на столько на сколько нужно чтобы корректно поместиться в игровом поле. Данная функция
    //получает массив координат фигуры и затем, если требуется,  сдвигает фигуру вправо или влево, пока  она не будет корректно вмещаться
    // в поле.
    int [][] BoundaryAlignment(int rot[][])

    {
        int countLeft=0;
        int countRight=0;
        //Считаем насколько фигуры выходит вправо или влево за пределы игрового поля
        for(int i=0;i<rot.length;i++)
        {
            if (rot[i][0] < 0) {
                countLeft++;
            }
            if (rot[i][0] >LogicConst.WIDTH_GAMEFIELD-1) {
                countRight++;
            }
        }
        //Затем сдвигаем её в нужном направлении
        if(countLeft!=0)
        {
            for(int i=0;i<rot.length;i++)
                rot[i][0]+=countLeft;
        }

        if(countRight!=0)
        {
            for(int i=0;i<rot.length;i++)
                rot[i][0]-=countRight;
        }
        return  rot;

    }
}
