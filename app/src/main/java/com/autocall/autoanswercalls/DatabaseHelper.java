package com.autocall.autoanswercalls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "autucall.db";
    public static final String TABLE_NAME = "autucall_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "STARTTIME";
    public static final String COL_3 = "ENDTIME";
    public static final String COL_4 = "AUDIO";
    public static final String COL_5 = "DESCRIPTION";
    public static final String COL_6 = "STARTDATE";
    public static final String COL_7 = "ENDDATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,STARTTIME TEXT,ENDTIME TEXT,AUDIO TEXT,DESCRIPTION TEXT,STARTDATE TEXT,ENDDATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String starttime,String endtime,String audio,String description, String startdate, String enddate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,starttime);
        contentValues.put(COL_3,endtime);
        contentValues.put(COL_4,audio);
        contentValues.put(COL_5,description);
        contentValues.put(COL_6,startdate);
        contentValues.put(COL_7,enddate);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id,String starttime,String endtime,String audio,String description, String startdate, String enddate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,starttime);
        contentValues.put(COL_3,endtime);
        contentValues.put(COL_4,audio);
        contentValues.put(COL_5,description);
        contentValues.put(COL_6,startdate);
        contentValues.put(COL_7,enddate);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
