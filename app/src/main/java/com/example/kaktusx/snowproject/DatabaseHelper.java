package com.example.kaktusx.snowproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DatabaseHelper extends SQLiteOpenHelper{

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Snowman.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS HIGHSCORE(NAME TEXT, SCORE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS HIGHSCORE;");
        onCreate(db);
    }

    public void insertScore(String name, int score){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("SCORE", score);
        this.getWritableDatabase().insert("HIGHSCORE", "", contentValues);
    }

    public void showScore(TextView view){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM HIGHSCORE", null);
        view.setText("");
        while (cursor.moveToNext()){
            view.append(cursor.getString(0) + " " + cursor.getString(1) + "      " + "\n");
        }
    }
}
