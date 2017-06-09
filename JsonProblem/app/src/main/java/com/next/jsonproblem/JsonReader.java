package com.next.jsonproblem;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by next on 23/3/17.
 */
public class JsonReader
{

    public static String jsonreader(Context context)
    {
        String json = null;
        InputStream jsondata = null;
        try
        {
            jsondata = context.getAssets().open("student.json");

            int size = jsondata.available();
            byte[] buffer = new byte[size];
            jsondata.read(buffer);
            jsondata.close();
            json = new String(buffer);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return json;
    }

}
