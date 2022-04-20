package com.example.database_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    //DB를 생성하고 초기화하는 DB생성자 정의 = DBHelper로 이동됨
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;

    Cursor cursor;

    TextView DB_DATA;
    EditText setSchedule, setTimer, listSchedule, listTimer;
    Button btnInsert, btnSelect, btnReset, btnCreate, listDelThis;

    int test;

    String strNum = "";
    String strSchedule = "";
    String strTimer = "";

    LinearLayout add_Timer_Layout;
    LinearLayout add_Schedule;
    LinearLayout DBView;

    Button delThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DB 입출력 학습");

        setSchedule = findViewById(R.id.add_schedule);
        setTimer = findViewById(R.id.add_timer);
        btnInsert = findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);
        btnReset = findViewById(R.id.btnReset);
        btnCreate = findViewById(R.id.btnCreate);

        add_Timer_Layout = (LinearLayout) findViewById(R.id.add_Timer_Layout);

        listSchedule = new EditText(getApplicationContext());
        listTimer = new EditText(getApplicationContext());
        listDelThis = new Button(getApplicationContext());
        /////////////////////////////////////////////////////////

        dbHelper = new DBHelper(this);
        sqlDB = dbHelper.getReadableDatabase();

        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

        test = cursor.getCount();
        timer_Create(test);

        Toast.makeText(getApplicationContext(),"테스트 "+test +" ",Toast.LENGTH_SHORT).show();
        cursor.close();
        sqlDB.close();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_Insert(view);
                onClick_Select(view);
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_Select(view);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_Drop(view);
                onClick_Select(view);
            }

        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_Create(view);
            }
        });

    }
    public void onClick_Select(View v) {
        sqlDB = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);
        cursor.close();
        sqlDB.close();
    }
    public void onClick_Insert(View view) {
        add_Timer_Layout.removeAllViewsInLayout();
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO groupTBL(gName,gNumber) VALUES (" +
                "'" + setSchedule.getText().toString() + "' ,"
                + setTimer.getText().toString() + ");");
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);
        test = cursor.getCount();
        timer_Create(test);
        sqlDB.close();
        Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
    }
    public void onClick_Drop(View view) {
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("Drop table IF EXISTS groupTBL");
        onClick_Create(view);
        add_Timer_Layout.removeAllViewsInLayout();
        sqlDB.close();
    }
    public void onClick_Create(View view) {
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS groupTBL (Num INTEGER PRIMARY KEY AUTOINCREMENT, gName CHAR(20) , gNumber INTEGER);");
        sqlDB.close();
    }

    public void timer_Create(int callCount){
        int start = 0;
        sqlDB = dbHelper.getWritableDatabase();

        while (start < callCount){
            LinearLayout p2 = new LinearLayout(this);
            p2.setOrientation(LinearLayout.HORIZONTAL);
            p2.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.HORIZONTAL);

            TextView Num = new TextView(getApplicationContext());
            EditText Schedule = new EditText(getApplicationContext());
            EditText Timer = new EditText(getApplicationContext());

            Num.setLayoutParams(p);
            Schedule.setLayoutParams(p);
            Timer.setLayoutParams(p);
            int starter = 1;
            while(starter <= callCount){
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL where num = "+ starter ,null);
                cursor.moveToFirst();
                String number = cursor.getString(0);
                String SC = cursor.getString(1);
                Num.setId(start);
                Num.setText(number);
                Schedule.setText(SC);
                Timer.setText("editText" + start + "번");
                Schedule.setId(start);
                Timer.setId(start);
                starter++;
            }
            p2.addView(Num);
            p2.addView(Schedule);
            p2.addView(Timer);
            add_Timer_Layout.addView(p2);

            start++;
        }
    }
}