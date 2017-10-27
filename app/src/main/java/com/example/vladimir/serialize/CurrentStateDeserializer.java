package com.example.vladimir.serialize;

import com.example.vladimir.struct.CubeObject;
import com.example.vladimir.struct.CurrentState;
import com.example.vladimir.struct.GameObject;
import com.example.vladimir.struct.LAngleObject;
import com.example.vladimir.struct.LZObject;
import com.example.vladimir.struct.RAngleObject;
import com.example.vladimir.struct.RZObject;
import com.example.vladimir.struct.StraightObject;
import com.example.vladimir.struct.TObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class CurrentStateDeserializer implements JsonDeserializer<CurrentState> {

    public CurrentState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {

        CurrentState currentState=new CurrentState();

        JsonObject jsonObject = json.getAsJsonObject();

        currentState.SetCurrentFigure(jsonObject.get("objecttype").getAsInt());

        int []i=context.deserialize(jsonObject.get("figures"),int[].class);
        currentState.SetFigures(i);

        boolean [][]coord=context.deserialize(jsonObject.get("battlefield"),boolean[][].class);
        currentState.SetPanel(coord);

        currentState.SetScore(jsonObject.get("score").getAsInt());

        currentState.SetLevel(jsonObject.get("level").getAsInt());

        switch (currentState.GetCurrentFigure())
        {
            case 0:currentState.SetObject((CubeObject) context.deserialize(jsonObject.get("GameObject"), CubeObject.class));break;
            case 1:currentState.SetObject((StraightObject) context.deserialize(jsonObject.get("GameObject"), StraightObject.class));break;
            case 2:currentState.SetObject((TObject) context.deserialize(jsonObject.get("GameObject"), TObject.class));break;
            case 3:currentState.SetObject((RAngleObject) context.deserialize(jsonObject.get("GameObject"), RAngleObject.class));break;
            case 4:currentState.SetObject((LAngleObject) context.deserialize(jsonObject.get("GameObject"), LAngleObject.class));break;
            case 5:currentState.SetObject((LZObject) context.deserialize(jsonObject.get("GameObject"), LZObject.class));break;
            case 6:currentState.SetObject((RZObject) context.deserialize(jsonObject.get("GameObject"), RZObject.class));break;

            default:break;
        }

        return currentState;
    }
}
