package com.example.vladimir.serialize;

import com.example.vladimir.struct.CurrentState;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class CurrentStateSerializer implements JsonSerializer<CurrentState>
{
    @Override
    public JsonElement serialize(CurrentState src, Type typeOfSrc, JsonSerializationContext context)
    {
//        StringBuilder sb=new StringBuilder();
//        String bf = "";
//        String coord = "";
////        //Переносим данные о состоянии игрового поля со всеми фигурами
//        for(int i=0;i<src.GetPanel()[0].length;i++)
//            for (int j = 0; j < src.GetPanel().length; j++)
//            {
//                if(src.GetPanel()[j][i])
//                    sb.append('1');
//                else
//                    sb.append('0');
//            }
//        sb.append(" ");
//
//        for(int i=0;i<src.GetObject().GetObjectCoord()[0].length;i++)
//            for (int j = 0; j < src.GetObject().GetObjectCoord().length; j++)
//            {
//                sb.append(src.GetObject().GetObjectCoord()[i][j]);
//            }
//
//        sb.append(" ");
//
//        for(int i=0;i<src.countFigures;i++)
//            sb.append(src.GetFigures()[i]);
//
//        sb.append(" ");
//
//        sb.append(src.GetScore());
//
//        sb.append(" ");
//        sb.append(src.GetLevel());
//
//        return new JsonPrimitive(sb.toString() );
        JsonObject result = new JsonObject();
        result.add("GameObject",context.serialize(src.GetObject()));
        result.add("battlefield",context.serialize(src.GetPanel()));
        result.add("figures",context.serialize(src.GetFigures()));
        result.addProperty("score",src.GetScore());
        result.addProperty("level",src.GetLevel());
        return result;
    }
}
