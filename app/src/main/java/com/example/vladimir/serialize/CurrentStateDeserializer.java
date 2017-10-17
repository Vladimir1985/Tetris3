package com.example.vladimir.serialize;

import com.example.vladimir.struct.CurrentState;
import com.example.vladimir.struct.GameObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Vladimir on 11.10.2017.
 */

public class CurrentStateDeserializer implements JsonDeserializer<CurrentState> {

    public CurrentState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        GameObject gm = null;
        CurrentState cs=new CurrentState();
       // String data = json.getAsString();
        JsonObject jsonObject = json.getAsJsonObject();
        cs.SetObject((GameObject) context.deserialize(jsonObject.get("GameObject"), GameObject.class));
        //gm=((GameObject) context.deserialize(jsonObject.get("GameObject"), GameObject.class));
        // gm.SetObjectCoord(context.deserialize(data,int[][]));
        return cs;
    }
}
