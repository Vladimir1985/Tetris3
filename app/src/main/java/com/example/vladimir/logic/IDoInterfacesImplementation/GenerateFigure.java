package com.example.vladimir.logic.IDoInterfacesImplementation;
import com.example.vladimir.struct.CurrentState;

public final class GenerateFigure extends GenerateRandom {

    CurrentState currentState=new CurrentState();

    //Генерация первых трех фигур на старте
    public CurrentState Do(CurrentState state){
        currentState=state;
        for(int i=0;i<currentState.GetFigures().length;i++)
            currentState.GetFigures()[i] = GetRandomNumber();
        return currentState;
    }


}
