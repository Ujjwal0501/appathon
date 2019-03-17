package com.example.appathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class UPICreation extends AppCompatActivity {

    Button btn;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    EditText upitxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_create);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.upifile) ,Context.MODE_PRIVATE);
        upitxt = (EditText) findViewById(R.id.upi_id);
        btn = (Button) findViewById(R.id.upi_create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: create upi handle
                Intent intent = new Intent(UPICreation.this, Home.class);
                UPICreation.this.startActivity(intent);

                // TODO: store in preference that upi is create
                editor = sharedPref.edit();
                editor.putString(getString(R.string.upiid), upitxt.getText().toString()+"@icici");
                editor.apply();

                UPICreation.this.finish();
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
}
