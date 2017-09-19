package com.example.vladimir.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Vladimir on 18.09.2017.
 */

public class SQLWorker {

    DBHelper dbHelper;
    SQLWorker(Context context)
    {
        dbHelper=new DBHelper(context);
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context)
        {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // создаем таблицу с полями
            db.execSQL("create table game_settings ("
                    + "id integer primary key autoincrement,"
                    + "battlefield text,"
                    + "first_figure integer"
                    + "second_figure integer"
                    + "third_figure integer"
                    + "score integer"
                    + "current_figure integer"
                    + ");");

            db.execSQL("create table coord ("
                    + "id integer primary key autoincrement,"
                    + "coord_point_X integer"
                    + "coord_point_Y integer"
                    + ");");
        }

        public void WriteGameState(String bf,int[] pic,int score,int currenfigure,int [][] coord)
        {
            // создаем объект для данных
            ContentValues cv = new ContentValues();
            // подключаемся к БД
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            cv.put("battlefield", bf);
            cv.put("first_figure", pic[0]);
            cv.put("second_figure", pic[1]);
            cv.put("third_figure", pic[2]);
            cv.put("score", score);
            cv.put("current_figure", currenfigure);
            db.delete("mytable",null,null);

            db.insert("mytable", null, cv);

            cv = new ContentValues();
            for(int i=0;i<coord.length;i++)
            {
                cv.put("coord_point_X", coord[i][0]);
                cv.put("coord_point_Y", coord[i][1]);
            }
            db.delete("coord",null,null);
            db.insert("coord", null, cv);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
