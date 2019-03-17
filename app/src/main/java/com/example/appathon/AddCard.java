package com.example.appathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCard extends AppCompatActivity {

    EditText ccno, ccexp, cccvv;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        // Get the Intent that started this activity and extract the string
        final Intent intent = getIntent();
        String message = intent.getStringExtra("account");

        ccno = (EditText) findViewById(R.id.cc_no);
        ccexp = (EditText) findViewById(R.id.cc_exp);
        cccvv = (EditText) findViewById(R.id.cc_cvv);
        btn = (Button) findViewById(R.id.cc_sbmt);

        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

        // TODO: verify the credit card details for validity

        // for the time being, just forward to the next view
        // provisionally add a specific credit card number to accept
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ccno.getText().toString().equals("1234567890123456") && ccexp.getText().toString().equals("01/21") && cccvv.getText().toString().equals("123")) {
                    Intent intent1 = new Intent(AddCard.this, ODAcCreation.class);
                    AddCard.this.startActivity(intent1);

                    // TODO: store the preference that cc has been added
                    AddCard.this.finish();
                } else {
                    Toast.makeText(AddCard.this, "Invalid"+ccexp.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO: check if user has already entered the credit card details
        super.onStart();
    }
}
