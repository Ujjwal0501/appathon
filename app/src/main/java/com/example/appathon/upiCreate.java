package com.example.appathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;


public class upiCreate extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi_create);

        btn = (Button) findViewById(R.id.upi_create);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: create upi handle
                Intent intent = new Intent(upiCreate.this, Home.class);
                upiCreate.this.startActivity(intent);

                // TODO: store in preference that upi is created
                upiCreate.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO: check if upi id is already created

        super.onStart();
    }
}
