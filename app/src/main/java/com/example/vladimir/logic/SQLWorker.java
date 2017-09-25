package com.example.vladimir.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Vladimir on 18.09.2017.
 */

public class SQLWorker {

    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";

    final String TABLE_SETTINGS="game_settings";
    final String TABLE_COORDINATES="coord";
    final String TABLE_CHAMPIONS="champion";

   public SQLWorker(Context context)
    {
        dbHelper=new DBHelper(context);
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

        db.execSQL("delete from "+ TABLE_SETTINGS);
        db.insert(TABLE_SETTINGS, null, cv);

        cv = new ContentValues();
        db.execSQL("delete from "+ TABLE_COORDINATES);
        for(int i=0;i<coord.length;i++)
        {
            cv.put("coord_point_X", coord[i][0]);
            cv.put("coord_point_Y", coord[i][1]);
            db.insert(TABLE_COORDINATES, null, cv);
        }

       // db.insert("coord", null, cv);
        dbHelper.close();
    }

    public void WriteScore(int score)
    {
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("score", score);
        db.execSQL("delete from "+ TABLE_CHAMPIONS);
        db.insert(TABLE_CHAMPIONS, null, cv);
    }
    public String ReadBF()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
        String BF="";
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int BFindex = c.getColumnIndex("battlefield");
            BF =c.getString(BFindex);
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        dbHelper.close();
        return  BF;
    }

    public void ClearSettingsTable()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SETTINGS);
        db.execSQL("delete from "+ TABLE_COORDINATES);
    }

    public int[]ReadFigures(int countf)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
        int[] f=new int[countf];
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int Findex1 = c.getColumnIndex("first_figure");
            int Findex2 = c.getColumnIndex("second_figure");
            int Findex3 = c.getColumnIndex("third_figure");

            f[0] =c.getInt(Findex1);
            f[1] =c.getInt(Findex2);
            f[2] =c.getInt(Findex3);
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        dbHelper.close();
        return  f;
    }

    public int ReadScore()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
        int score=0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int scoreindex = c.getColumnIndex("score");
            score =c.getInt(scoreindex);
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        dbHelper.close();
        return  score;
    }

    public int ReadFigureNum()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
        int figure=0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int figureindex = c.getColumnIndex("current_figure");
            figure =c.getInt(figureindex);
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        dbHelper.close();
        return  figure;
    }

    public int ReadChampion()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_CHAMPIONS, null, null, null, null, null, null);
        int score=0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int scoreindex = c.getColumnIndex("score");
            score =c.getInt(scoreindex);
        } else
            return 0;
        c.close();
        dbHelper.close();
        return  score;
    }

    public int[][] ReadCoord()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_COORDINATES, null, null, null, null, null, null);
        int cnt  = (int)DatabaseUtils.queryNumEntries(db, TABLE_COORDINATES);
        int[][] f=new int[cnt][2];
        if (c.moveToFirst())
        {
            // определяем номера столбцов по имени в выборке
            int Xindex = c.getColumnIndex("coord_point_X");
            int Yindex = c.getColumnIndex("coord_point_Y");

            int i=0;
            do {
                f[i][0]=c.getInt(Xindex);
                f[i][1]=c.getInt(Yindex);
                i++;
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        //db.execSQL("delete from "+ TABLE_COORDINATES);
        dbHelper.close();
        return  f;
    }

    public void WriteGameState(byte[]b)
    {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("byte_data_settings",b);
        db.delete("byte_data",null,null);
        db.insert("byte_data", null, cv);
    }

    public boolean CheckDB()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
        boolean b;
        if (c.moveToFirst())
        b=true;
        else
        b=false;

        c.close();
        db.close();
        return  b;
    }
    public byte[] ReadGameState()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("byte_data", null, null, null, null, null, null);
        byte[] blob = cursor.getBlob(cursor.getColumnIndex("byte_data_settings"));
        return blob;
    }

    public void SetChampion(int game_score)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(TABLE_CHAMPIONS, null, null, null, null, null, null);
        int table_score=0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int scoreindex = c.getColumnIndex("score");
            table_score=c.getInt(scoreindex);
        } else
            Log.d(LOG_TAG, "0 rows");
        ContentValues cv = new ContentValues();

        cv.put("score", Math.max(table_score,game_score));

        db.execSQL("delete from "+ TABLE_CHAMPIONS);
        db.insert(TABLE_CHAMPIONS, null, cv);

        c.close();
        dbHelper.close();

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
            db.execSQL("create table "+TABLE_SETTINGS+" ("
                    + "id integer primary key autoincrement,"
                    + "battlefield text,"
                    + "first_figure integer,"
                    + "second_figure integer,"
                    + "third_figure integer,"
                    + "score integer,"
                    + "current_figure integer"
                    + ");");

            db.execSQL("create table "+TABLE_COORDINATES+" ("
                    + "id integer primary key autoincrement,"
                    + "coord_point_X integer,"
                    + "coord_point_Y integer"
                    + ");");

            db.execSQL("create table "+TABLE_CHAMPIONS+" ("
                    + "id integer primary key autoincrement,"
                    + "score integer"
                    + ");");
        }




        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
