package com.example.appathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appathon.R;

public class ODAcCreation extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odac_creation);

        btn = (Button) findViewById(R.id.create_ac);

        // TODO: create an OD/normal account using api

        // provisionally forward to the next view
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ODAcCreation.this, Home.class);
                ODAcCreation.this.startActivity(intent);

                // TODO: store the preference that account is created.
                ODAcCreation.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO: skip to next view if account is already created

        super.onStart();
    }
}
