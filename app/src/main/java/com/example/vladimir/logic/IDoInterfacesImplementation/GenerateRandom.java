package com.example.vladimir.logic.IDoInterfacesImplementation;
import com.example.vladimir.logic.LogicConst;
import java.util.Random;

public abstract class GenerateRandom implements IDo {

    public int GetRandomNumber(){
        //Генерируем число от 0 до 7
        Random rand = new Random();
        int i = rand.nextInt(LogicConst.COUNT_FIGURES_TYPE);
        return  i;
    }
}
