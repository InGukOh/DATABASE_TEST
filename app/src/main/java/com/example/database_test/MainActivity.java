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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //DB를 생성하고 초기화하는 DB생성자 정의 = DBHelper로 이동됨
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;

    Cursor cursor;

    TextView num , edit_schedule, edit_timer;
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
        num = findViewById(R.id.num);
        edit_schedule = findViewById(R.id.edtNameResult);
        edit_timer = findViewById(R.id.edtNumberResult);
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


        test = 0;

        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

        DBView();

        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);

        test = cursor.getCount();
        timer_Create(test);
        Toast.makeText(getApplicationContext(),"테스트 "+test +" ",Toast.LENGTH_SHORT).show();
        cursor.close();
        sqlDB.close();


        //내 DB클래스 객체생성

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
        //커서를 선언하고 모든 테이블을 조회한 후 커서에 대입한다.
        //이 과정이 조금 이해가 어려운데, 이 커서 객체가 포인터 역할을 한다고 이해하면 된다.
        sqlDB = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);


        //커서가 움직이면서  현재 커서의 열 번호 데이터값을 반환해서 문자열 변수에 계속 누적한다.
        //0은 0번째열(그룹이름) , 1은 1번째열(인원)이 된다.
        while(cursor.moveToNext()){
            strNum += cursor.getString(0)+ "\r\n";
            strSchedule += cursor.getString(1) + "\r\n";
            strTimer += cursor.getString(2) + "\r\n";


        //이름 출력해주기
        num.setText(strNum);
        edit_schedule.setText(strSchedule);
        edit_timer.setText(strTimer);
        }
        cursor.close();
        sqlDB.close();
    }
    public void onClick_Insert(View view) {
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO groupTBL(gName,gNumber) VALUES (" +
                "'" + setSchedule.getText().toString() + "' ,"
                + setTimer.getText().toString() + ");");
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);
        test = cursor.getCount();
        timer_Create(test);
        sqlDB.close();
        Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
        overridePendingTransition(0, 0);//인텐트 효과 없애기
        Intent intent = getIntent(); //인텐트
        startActivity(intent);
    }
    public void onClick_Drop(View view) {
        sqlDB = dbHelper.getWritableDatabase();
        add_Timer_Layout.removeAllViewsInLayout();
        sqlDB.execSQL("Drop table IF EXISTS groupTBL");
        onClick_Create(view);
        sqlDB.close();
    }
    public void onClick_Create(View view) {
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS groupTBL (Num INTEGER PRIMARY KEY AUTOINCREMENT, gName CHAR(20) , gNumber INTEGER);");
        sqlDB.close();
    }

    public void timer_Create(int callCount){
        add_Timer_Layout.removeAllViewsInLayout();
        int start = 0;
        while (start < callCount){
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            EditText et = new EditText(getApplicationContext());
            et.setLayoutParams(p);
            et.setText("editText" + start + "번");
            et.setId(start);
            add_Timer_Layout.addView(et);
            start++;
        }
    }
    public  void DBView(){
       /* DBView.removeAllViewsInLayout();*/
        while(cursor.moveToNext()){
            strNum += cursor.getString(0)+ "\r\n";
            strSchedule += cursor.getString(1) + "\r\n";
            strTimer += cursor.getString(2) + "\r\n";
        }

        //이름 출력해주기
        num.setText(strNum);
        edit_schedule.setText(strSchedule);
        edit_timer.setText(strTimer);
    }
}