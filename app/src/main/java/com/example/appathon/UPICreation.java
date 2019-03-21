package com.example.appathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


public class UPICreation extends AppCompatActivity {

    String desc = "";
    private static final int MPIN_TOTAL_SYMBOLS = 6;

    Button btn;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    EditText upitxt, mpin, cmpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_create);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.upifile) ,Context.MODE_PRIVATE);
        upitxt = (EditText) findViewById(R.id.upi_id);
        mpin = (EditText) findViewById(R.id.mpin);
        cmpin = (EditText) findViewById(R.id.cnf_mpin);
        btn = (Button) findViewById(R.id.upi_create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboardFrom(UPICreation.this, view);
                desc = "";
                int flag = 1;
                flag = formfill();

                if (flag == 1 && mpin.getText().toString().equals(cmpin.getText().toString())) {
                    // TODO: create upi handle
                    Intent intent = new Intent(UPICreation.this, Home.class);
                    UPICreation.this.startActivity(intent);

                    // TODO: store in preference that upi is create
                    editor = sharedPref.edit();
                    editor.putString(getString(R.string.upiid), upitxt.getText().toString() + "@icici");
                    editor.putString(getString(R.string.mpin), mpin.getText().toString());
                    editor.apply();

                    UPICreation.this.finish();
                } else {
                    if (flag == 0)
                        Snackbar.make(view, ""+desc, Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(view, "MPIN do not match.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        mpin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (mpin.getText().toString().length() == 6)
                    cmpin.requestFocus();
                return false;
            }
        });


        cmpin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (cmpin.getText().toString().length() == 6) {
                    cmpin.clearFocus();
                    hideKeyboardFrom(UPICreation.this, view);
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        // TODO: check if upi id is already created
        if (sharedPref.contains(getString(R.string.upiid))) {
            UPICreation.this.startActivity(new Intent(UPICreation.this, Home.class));
            UPICreation.this.finish();
        }

        super.onStart();
    }

    protected void onCardCVCTextChanged(Editable s) {
        if (s.length() > MPIN_TOTAL_SYMBOLS) {
            s.delete(MPIN_TOTAL_SYMBOLS, s.length());
        }
    }

    public void upiInfo(View view) {
        Snackbar.make(view, "These details are for the creation of Your UPI handle.", Snackbar.LENGTH_LONG).show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public int formfill() {
        if (upitxt.getText().toString().trim().length() == 0 || mpin.getText().toString().length() != 6 || cmpin.getText().toString().length() != 6) {
            desc = "Complete the details first";
            return 0;
        } else return 1;
    }
}
