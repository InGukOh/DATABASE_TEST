package com.example.database_test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqlDB;
    DBHelper dbHelper;
    Cursor cursor;

    public DBHelper(Context context) {
        super(context, "GroupTBL", null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        //groupTBL이라는 테이블이름으로 gName, gNumber 필드를 생성해주자
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS groupTBL (Num INTEGER PRIMARY KEY AUTOINCREMENT, gName CHAR(20) , gNumber INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        //이곳에선 테이블이 존재하면 없애고 새로 만들어준다.
        sqlDB.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(sqlDB);
    }

}