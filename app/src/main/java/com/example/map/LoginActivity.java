package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText log_or_email, pass;
    Button log, reg;

    static MyOpenHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new MyOpenHelper(this, "MyDB3", null, 1);
        db = dbHelper.getReadableDatabase();

        log_or_email = findViewById(R.id.StartLogOrEmail);
        pass = findViewById(R.id.StartPas);
        log = findViewById(R.id.StartLogBtn);
        reg = findViewById(R.id.StartRegBtn);



        log.setOnClickListener(v->{
            Cursor c = db.rawQuery("SELECT * FROM user WHERE (login = ? OR email = ?) AND pass = ?", new String[]{log_or_email.getText().toString(),log_or_email.getText().toString(), pass.getText().toString()});
            if(c.getCount() > 0 ){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                c.moveToFirst();
                intent.putExtra("login",c.getString(c.getColumnIndex("login")));
                intent.putExtra("email",c.getString(c.getColumnIndex("email")));
                startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this, "Incorrect login or password", Toast.LENGTH_LONG).show();
            }
        });

        reg.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }
}