package com.example.map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    static MyOpenHelper dbHelper;
    SQLiteDatabase db;
    Button reg;
    EditText log, pass, email;
    TextView logExc, passExc, emailExc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        dbHelper = new MyOpenHelper(this, "MyDB3", null, 1);
        db = dbHelper.getReadableDatabase();

        log = findViewById(R.id.regLogin);
        pass = findViewById(R.id.regPass);
        email = findViewById(R.id.regEmail);
        reg = findViewById(R.id.regBtn);
        logExc = findViewById(R.id.loginExcText);
        emailExc = findViewById(R.id.emailExcText);
        passExc = findViewById(R.id.passExcText);

            reg.setOnClickListener(v -> {
                if(log.getText().length() < 5) { logExc.setAlpha(0); }
                if(pass.getText().length() < 6) { passExc.setAlpha(0); }
                if(isValidEmail(email.getText().toString())) { emailExc.setAlpha(0); }
                if(log.getText().length() >= 5 && isValidEmail(email.getText().toString()) && pass.getText().toString().length() >= 6) {
                Cursor c = db.rawQuery("SELECT * FROM user WHERE email = ? AND login = ?", new String[]{email.getText().toString(), log.getText().toString()});
                if (c.getCount() > 0) {
                    Toast.makeText(getApplicationContext(), "A user with the same email or name already exists", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues newUser = new ContentValues();
                    newUser.put("login", log.getText().toString());
                    newUser.put("pass", pass.getText().toString());
                    newUser.put("email", email.getText().toString());
                    db.insert("user", null, newUser);
                    c.close();
                    finish();
                }
            }else
                if(log.getText().length() < 5) { logExc.setAlpha(1); }
                if(!isValidEmail(email.getText().toString())) { emailExc.setAlpha(1); }
                if(pass.getText().toString().length() < 6) { passExc.setAlpha(1); }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}