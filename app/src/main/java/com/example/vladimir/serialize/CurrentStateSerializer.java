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
        JsonObject result = new JsonObject();
        result.add("GameObject",context.serialize(src.GetObject()));
        result.add("battlefield",context.serialize(src.GetPanel()));
        result.add("figures",context.serialize(src.GetFigures()));
        result.addProperty("score",src.GetScore());
        result.addProperty("level",src.GetLevel());
        result.addProperty("objecttype",src.GetCurrentFigure());

        return result;
    }
}
