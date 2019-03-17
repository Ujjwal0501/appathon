package com.example.appathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.appathon.R;

public class ODAcCreation extends AppCompatActivity {

    Button btn;
    EditText acholder, acphone, acno;
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

        // TODO: create an OD/normal account using api

        // provisionally forward to the next view
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ODAcCreation.this.startActivity(new Intent(ODAcCreation.this, UPICreation.class));

                // store the preference that account is created.
                editor = sharedPref.edit();
                editor.putString(getString(R.string.acholder), acholder.getText().toString());
                editor.putString(getString(R.string.acphone), acphone.getText().toString());
                editor.putString(getString(R.string.acno), "1234567890123456");
                editor.apply();

                ODAcCreation.this.finish();
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
}
