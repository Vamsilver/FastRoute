package com.example.map.ui.slideshow;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.map.MainActivity;
import com.example.map.MyOpenHelper;
import com.example.map.R;
import com.google.android.material.navigation.NavigationView;

public class SlideshowFragment extends Fragment {

    EditText log, email, oldPas, newPas;
    TextView logExc, emailExc, oldPasExc,
            newPasExc, oldLog, oldEmail;
    Button change;

    static MyOpenHelper dbHelper;
    SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        dbHelper = new MyOpenHelper(getContext(), "MyDB4", null, 1);
        db = dbHelper.getReadableDatabase();

        log = root.findViewById(R.id.EditLog);
        email = root.findViewById(R.id.EditEmail);
        oldPas = root.findViewById(R.id.EditOldPas);
        newPas = root.findViewById(R.id.EditNewPas);
        change = root.findViewById(R.id.changeBtn);
        logExc = root.findViewById(R.id.LoginExc);
        emailExc = root.findViewById(R.id.EmailExc);

        oldEmail = getActivity().findViewById(R.id.HeaderEmail);
        oldLog = getActivity().findViewById(R.id.HeaderLogin);

        oldPasExc = root.findViewById(R.id.OldPasExc);
        newPasExc = root.findViewById(R.id.NewPasExc);

        change.setOnClickListener(v->{
            if(oldPas.getText().length() >= 6 && newPas.getText().length() >= 6) {
                Cursor c = db.rawQuery("SELECT * FROM user WHERE pass = ?", new String[]{oldPas.getText().toString()});
                if(c.getCount() > 0 ){
                    ContentValues newPasC = new ContentValues();
                    newPasC.put("pass", newPas.getText().toString());
                    db.update("user", newPasC, "pass = ?", new String[]{oldPas.getText().toString()});
                }
                newPasExc.setAlpha(0);
                oldPasExc.setAlpha(0);
            }
            else{
                newPasExc.setAlpha(1);
                oldPasExc.setAlpha(1);
            }
            if(isValidEmail(email.getText().toString())) {
                ContentValues newEmailC = new ContentValues();
                newEmailC.put("email", email.getText().toString());
                db.update("user", newEmailC, "email = ?", new String[]{oldEmail.getText().toString()});
                emailExc.setAlpha(0);
            }
            else{ emailExc.setAlpha(1); }
            if(log.getText().length() < 5 ){ logExc.setAlpha(1); }
            else if (log.getText().length() >= 5 || log == null){
                ContentValues newLogC = new ContentValues();
                newLogC.put("login", log.getText().toString());
                db.update("user", newLogC, "login = ?", new String[]{oldLog.getText().toString()});
                logExc.setAlpha(0);
            }
        });

        return root;


    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}