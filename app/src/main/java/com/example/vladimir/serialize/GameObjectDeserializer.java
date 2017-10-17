package com.example.vladimir.serialize;


import com.example.vladimir.struct.GameObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;

public class GameObjectDeserializer
{

    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        GameObject gm = null;
        String data = json.getAsString();
        JsonObject jsonObject = json.getAsJsonObject();
        gm=((GameObject) context.deserialize(jsonObject.get("GameObject"), GameObject.class));
       // gm.SetObjectCoord(context.deserialize(data,int[][]));
        return gm;
    }
}
