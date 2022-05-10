package com.example.database_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //DB를 생성하고 초기화하는 DB생성자 정의 = DBHelper로 이동됨
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;

    Cursor cursor;

    TextView DB_DATA;
    EditText setSchedule, setTimer, listSchedule, listTimer;
    Button btnInsert, btnSelect, btnReset, btnCreate, listDelThis;

    int CallCount;

    String strNum = "";
    String strSchedule = "";
    String strTimer = "";

    TableLayout add_Timer_Layout;
    LinearLayout add_Schedule;
    LinearLayout DBView;

    Button delThis;

    ArrayList<Integer> numb= new ArrayList<>();
    ArrayList<String> sch= new ArrayList<>();
    ArrayList<String> tm= new ArrayList<>();

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

        add_Timer_Layout = findViewById(R.id.add_Timer_Layout);

        listSchedule = new EditText(getApplicationContext());
        listTimer = new EditText(getApplicationContext());
        listDelThis = new Button(getApplicationContext());
        /////////////////////////////////////////////////////////

        dbHelper = new DBHelper(this);
        sqlDB = dbHelper.getReadableDatabase();

        cursor = sqlDB.rawQuery("SELECT count(*) FROM groupTBL;", null);
        cursor.moveToFirst();
        CallCount = cursor.getInt(0);
        Toast.makeText(getApplicationContext(),"DB 내용갯수 "+ CallCount +" ",Toast.LENGTH_SHORT).show();

        timer_Create(CallCount);

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

        cursor = sqlDB.rawQuery("SELECT count(*) FROM groupTBL;", null);
        cursor.moveToFirst();
        CallCount = cursor.getInt(0);
        timer_Create(CallCount);

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

    public void timer_Create(int CallCount) {
        sqlDB = dbHelper.getWritableDatabase();

        int nn;
        String ss;
        int tt;

        for(int i =1; i<=CallCount; i++){

            TableRow TimerSet = new TableRow(this);
            TimerSet.setOrientation(TableRow.HORIZONTAL);
            TimerSet.setGravity(Gravity.CENTER);

            TableRow.LayoutParams rowLayout = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.HORIZONTAL);

            EditText Num = new EditText(getApplicationContext());
            EditText Schedule = new EditText(getApplicationContext());
            EditText Timer = new EditText(getApplicationContext());
            Button EditThis = new Button(getApplicationContext());
            Button DelThis = new Button(getApplicationContext());
            EditThis.setText("수정");
            DelThis.setText("삭제");

            Num.setLayoutParams(rowLayout);
            Schedule.setLayoutParams(rowLayout);
            Timer.setLayoutParams(rowLayout);

            Num.setId(i);
            Schedule.setId(i);
            Timer.setId(i);
            EditThis.setId(i);
            DelThis.setId(i);

            EditThis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditThis(view);
                }
            });

            cursor = sqlDB.rawQuery("SELECT * FROM groupTBL WHERE NUM ="+i+";", null);
            cursor.moveToFirst();

            Num.setText(Integer.toString(cursor.getInt(0)));
            Schedule.setText(cursor.getString(1));
            Timer.setText(Integer.toString(cursor.getInt(2)));

            TimerSet.addView(Num);
            TimerSet.addView(Schedule);
            TimerSet.addView(Timer);
            TimerSet.addView(EditThis);
            TimerSet.addView(DelThis);
            add_Timer_Layout.addView(TimerSet);

            Toast.makeText(getApplicationContext(), " "+cursor.getInt(0)+" "+cursor.getString(1)+" "+cursor.getInt(2), Toast.LENGTH_SHORT).show();
        }
    }
    public void EditThis(View view){
        EditText ES = findViewById(view.getId());

        String tes = ES.getText().toString();
        Toast.makeText(getApplicationContext(), " "+tes,Toast.LENGTH_SHORT).show();
    }
}