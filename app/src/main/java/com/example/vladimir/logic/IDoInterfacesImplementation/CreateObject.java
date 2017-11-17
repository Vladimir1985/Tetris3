package com.example.vladimir.logic.IDoInterfacesImplementation;

import com.example.vladimir.logic.LogicConst;
import com.example.vladimir.struct.CubeObject;
import com.example.vladimir.struct.CurrentState;
import com.example.vladimir.struct.GameObject;
import com.example.vladimir.struct.LAngleObject;
import com.example.vladimir.struct.LZObject;
import com.example.vladimir.struct.RAngleObject;
import com.example.vladimir.struct.RZObject;
import com.example.vladimir.struct.StraightObject;
import com.example.vladimir.struct.TObject;

/**
 * Created by Vladimir on 16.11.2017.
 */

public final class CreateObject extends GenerateRandom {

    CurrentState currentState=new CurrentState();

    public CurrentState Do(CurrentState state){
        currentState=state;
        //В зависимости от числа мы инициализируем объект родительского класса нужным классом потомком
        InitializeGameObject(currentState.GetFigures()[0]);
        //Номер текущей фигуры
        currentState.SetCurrentFigure(currentState.GetFigures()[0]);
        currentState.SetEndOfTurn(false);
        //Сдвигаем массив предсказанных фигур на 1 элемент
        for (int i = 0; i <currentState.GetFigures().length-1; i++)
            currentState.GetFigures()[i] = currentState.GetFigures()[i+1];

        //Обновляем последнюю сгенерированную фигуру
        currentState.GetFigures()[currentState.GetFigures().length-1]=GetRandomNumber();
        //После создания нового объекта проверяем не заканчивается ли игра с появлением этого объекта на поле
        return currentState;
    }

    private void InitializeGameObject(int i)
    {
        GameObject currentObject;

        switch (i)
        {
            case LogicConst.CUBE:currentObject=new CubeObject();currentState.SetObject(currentObject);break;
            case LogicConst.STRAIGHT:currentObject=new StraightObject();currentState.SetObject(currentObject);break;
            case LogicConst.T_OBJECT:currentObject=new TObject();currentState.SetObject(currentObject);break;
            case LogicConst.R_ANGLE:currentObject=new RAngleObject();currentState.SetObject(currentObject);break;
            case LogicConst.L_ANGLE:currentObject=new LAngleObject();currentState.SetObject(currentObject);break;
            case LogicConst.LZ:currentObject=new LZObject();currentState.SetObject(currentObject);break;
            case LogicConst.RZ:currentObject=new RZObject();currentState.SetObject(currentObject);break;
            default:break;
        }
    }
}
