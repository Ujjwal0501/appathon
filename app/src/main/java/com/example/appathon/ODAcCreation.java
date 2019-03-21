package com.example.appathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.appathon.R;

public class ODAcCreation extends AppCompatActivity {

    String desc = "";

    Button btn;
    EditText acholder, acphone, acno, acaddr;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odac_creation);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.acfile) , Context.MODE_PRIVATE);

        btn = (Button) findViewById(R.id.create_ac);
        acholder = (EditText) findViewById(R.id.ac_holder);
        acphone = (EditText) findViewById(R.id.ac_phone);
        acaddr = (EditText) findViewById(R.id.ac_addr);

        // TODO: create an OD/normal account using api

        // provisionally forward to the next view
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardFrom(ODAcCreation.this, view);
                desc = "";
                int flag = 1;
                flag = formfill();
                if (flag == 1) flag = formValidate();

                if (flag == 1 && ((CheckBox) findViewById(R.id.tc)).isChecked()) {

                    // store the preference that account is created.
                    editor = sharedPref.edit();
                    editor.putString(getString(R.string.acholder), acholder.getText().toString());
                    editor.putString(getString(R.string.acphone), acphone.getText().toString());
                    editor.putString(getString(R.string.acno), "1234567890123456");
                    editor.apply();

                    ODAcCreation.this.startActivity(new Intent(ODAcCreation.this, UPICreation.class));

                                    ODAcCreation.this.finish();
                } else {
                    if (flag == 1)
                        Snackbar.make(view, "Please agree to the Terms and Conditions.", Snackbar.LENGTH_LONG).show();
                    else if (flag == 2)
                        Snackbar.make(view, "Invalid Phone Number", Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(view, ""+desc, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        // skip to next view if account is already created
        if (sharedPref.contains(getString(R.string.acno))) {
            ODAcCreation.this.startActivity(new Intent(ODAcCreation.this, UPICreation.class));
            ODAcCreation.this.finish();
        }

        super.onStart();
    }

    public void acInfo(View view) {
        Snackbar.make(view, ""+getString(R.string.ac_details), Snackbar.LENGTH_LONG).show();
    }

    public void tcInfo(View view) {
        Snackbar.make(view, ""+getString(R.string.acinfo), Snackbar.LENGTH_LONG).show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public int formfill() {
        if (acholder.getText().toString().trim().length() == 0 || acphone.getText().toString().trim().length() == 0 || acaddr.getText().toString().trim().length() == 0) {
            desc = "Complete the details first";
            return 0;
        } else return 1;
    }

    public int formValidate() {
        if (acphone.getText().toString().trim().length() == 10)
            return 1;
        else return 2;
    }
}
