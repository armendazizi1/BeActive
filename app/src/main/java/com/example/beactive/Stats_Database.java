package com.example.beactive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Stats_Database extends SQLiteOpenHelper {



    // database name
    public static final String DATABASE_NAME = "stats_data.db";
    // table name
    public static final String TABLE_NAME = "stats_table";

    // columns
    public static final String col1 = "stats";

    public static final String col2 = "day";


    // constructor
    public Stats_Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        // SQLiteDatabase db = this.getWritableDatabase();

    }

    // create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "  + TABLE_NAME + "( stats INTEGER, day INTEGER )  ");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }


    // insert data into table
    public boolean insertData(int day, int stat) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1,stat);
        contentValues.put(col2,day);


        long result = db.insert(TABLE_NAME, null ,contentValues);
        db.close();

        if(result == -1){
            return false;
        }
        else{
            return true;
        }

    }


    // read data from table
    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+ TABLE_NAME, null);
        return res;
    }






    // update data in table

    public boolean updateData(int day, int stats){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1,stats);
        db.update(TABLE_NAME,contentValues, col2+" =?", new String[]{String.valueOf(day)});
        return true;
    }




    // delete data from table
    public Integer deleteData(int day){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME, col2 +" =?",new String[]{String.valueOf(day)});
        return i;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
       db.execSQL("delete from "+ TABLE_NAME);
    }
}
