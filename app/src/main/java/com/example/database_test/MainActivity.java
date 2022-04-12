package com.example.database_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //DB를 생성하고 초기화하는 DB생성자 정의 = DBHelper로 이동됨
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;

    TextView num , edit_schduel, edit_timer;
    EditText setSchedule, setTimer, listSchedule, listTimer;
    Button btnInsert, btnSelect, btnUpdate, btnCreate, listDelThis;

    LinearLayout add_Timer_Layout;

    Button delThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DB 입출력 학습");

        //xml버튼들 객체연결////////////////////////////////////////
        setSchedule = findViewById(R.id.add_schedule);
        setTimer = findViewById(R.id.add_timer);
        num = findViewById(R.id.num);
        edit_schduel = findViewById(R.id.edtNameResult);
        edit_timer = findViewById(R.id.edtNumberResult);
        btnInsert = findViewById(R.id.btnInsert);
        btnSelect = findViewById(R.id.btnSelect);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCreate = findViewById(R.id.btnCreate);

        listSchedule = new EditText(getApplicationContext());
        listTimer = new EditText(getApplicationContext());
        listDelThis = new Button(getApplicationContext());
        /////////////////////////////////////////////////////////

        dbHelper = new DBHelper(this);
        sqlDB = dbHelper.getReadableDatabase();
        Cursor cursor;

        String sql = "CREATE TABLE IF NOT EXISTS groupTBL (Num INTEGER PRIMARY KEY AUTOINCREMENT, gName CHAR(20) , gNumber INTEGER);";
        sqlDB.execSQL(sql);



        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

        //그룹이름과 인원을 나타내 줄 문자열 선언
        String strNum = "타이머 번호" + "\r\n" + "-------" + "\r\n";
        String strNames = "일정" + "\r\n" + "-------" + "\r\n";
        String strNumbers = "시간" + "\r\n" + "-------" + "\r\n";


        //커서가 움직이면서  현재 커서의 열 번호 데이터값을 반환해서 문자열 변수에 계속 누적한다.
        //0은 0번째열(그룹이름) , 1은 1번째열(인원)이 된다.
        while(cursor.moveToNext()){
            strNum += cursor.getString(0)+ "\r\n";
            strNames += cursor.getString(1) + "\r\n";
            strNumbers += cursor.getString(2) + "\r\n";
        }

        //이름 출력해주기
        num.setText(strNum);
        edit_schduel.setText(strNames);
        edit_timer.setText(strNumbers);

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick_Drop(view);
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

        String strNum = "타이머 번호" + "\r\n" + "-------" + "\r\n";
        String strNames = "일정" + "\r\n" + "-------" + "\r\n";
        String strNumbers = "시간" + "\r\n" + "-------" + "\r\n";


        //커서가 움직이면서  현재 커서의 열 번호 데이터값을 반환해서 문자열 변수에 계속 누적한다.
        //0은 0번째열(그룹이름) , 1은 1번째열(인원)이 된다.
        while(cursor.moveToNext()){
            strNum += cursor.getString(0)+ "\r\n";
            strNames += cursor.getString(1) + "\r\n";
            strNumbers += cursor.getString(2) + "\r\n";
        }

        //이름 출력해주기
        num.setText(strNum);
        edit_schduel.setText(strNames);
        edit_timer.setText(strNumbers);

        cursor.close();
        sqlDB.close();
        cursor.close();
        sqlDB.close();
    }
    public void onClick_Insert(View view) {
        //마찬가지로 쓰기용으로 열고, 쿼리문을 입력해준다. 따옴표에 주의하자.
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("INSERT INTO groupTBL(gName,gNumber) VALUES (" +
                "'" + setSchedule.getText().toString() + "' ,"
                + setTimer.getText().toString() + ");");
        sqlDB.close();
        Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
        overridePendingTransition(0, 0);//인텐트 효과 없애기
        Intent intent = getIntent(); //인텐트
        startActivity(intent);
    }
    public void onClick_Drop(View view) {
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("Drop table IF EXISTS groupTBL");
        onClick_Create(view);
        sqlDB.close();
    }
    public void onClick_Create(View view) {
        sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("CREATE TABLE groupTBL (Num INTEGER PRIMARY KEY AUTOINCREMENT, gName CHAR(20) , gNumber INTEGER);");
        sqlDB.close();
    }
}