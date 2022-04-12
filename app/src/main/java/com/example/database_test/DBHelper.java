package com.example.database_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqlDB;

    public DBHelper(Context context) {
        super(context, "GroupTBL", null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //groupTBL이라는 테이블이름으로 gName, gNumber 필드를 생성해주자
        db.execSQL("CREATE TABLE IF NOT EXISTS groupTBL (Num INTEGER PRIMARY KEY AUTOINCREMENT, gName CHAR(20) , gNumber INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //이곳에선 테이블이 존재하면 없애고 새로 만들어준다.
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(db);
    }
}