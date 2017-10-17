package com.example.vladimir.serialize;

import com.example.vladimir.struct.GameObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GameObjectSerializer implements JsonSerializer<GameObject>
{
    @Override
    public JsonElement serialize(GameObject src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject result = new JsonObject();
        result.add("coord",context.serialize(src.GetObjectCoord()));
        return result;
    }
}
