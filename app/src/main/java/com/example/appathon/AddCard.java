package com.example.appathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnTextChanged;

import static java.lang.Integer.parseInt;

public class AddCard extends AppCompatActivity {

    private static final int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';

    private static final int CARD_DATE_TOTAL_SYMBOLS = 5; // size of pattern MM/YY
    private static final int CARD_DATE_TOTAL_DIGITS = 4; // max numbers of digits in pattern: MM + YY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    private static final int CARD_CVC_TOTAL_SYMBOLS = 3;

    String desc = "";
    EditText ccno, ccexp, cccvv, ccholder;
    Button btn;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.ccfile) , Context.MODE_PRIVATE);

        // Get the Intent that started this activity and extract the string
        final Intent intent = getIntent();
        String message = intent.getStringExtra("account");

        ccno = (EditText) findViewById(R.id.cc_no);
        ccexp = (EditText) findViewById(R.id.cc_exp);
        cccvv = (EditText) findViewById(R.id.cc_cvv);
        btn = (Button) findViewById(R.id.cc_sbmt);
        ccholder = (EditText) findViewById(R.id.cc_holder);

        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

        // TODO: verify the credit card details for validity

        // for the time being, just forward to the next view
        // provisionally add a specific credit card number to accept
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardFrom(AddCard.this, view);
                desc = "";
                int flag;
                if (formfill() == 1)
                    flag = validateCard();
                else flag = 0;

                if (flag == 1) {

                    AddCard.this.startActivity(new Intent(AddCard.this, ODAcCreation.class));

                    // store the preference that cc has been added
                    editor = sharedPref.edit();
                    editor.putString(getString(R.string.ccno), ccno.getText().toString());
                    editor.putString(getString(R.string.ccexp), ccexp.getText().toString());
                    editor.putString(getString(R.string.cccvv), cccvv.getText().toString());
                    editor.putString(getString(R.string.ccholder), ccholder.getText().toString());
                    editor.apply();

                    AddCard.this.finish();
                } else {
                    Snackbar.make(view, ""+desc, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ccno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (ccno.getText().toString().length() == 19)
                    ccexp.requestFocus();
                return false;
            }
        });

        ccexp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (ccexp.getText().toString().length() == 5)
                    cccvv.requestFocus();
                return false;
            }
        });

        cccvv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (cccvv.getText().toString().length() == 3)
                    hideKeyboardFrom(AddCard.this, view);
                return false;
            }
        });

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        // check if user has already entered the credit card details
        if (sharedPref.contains(getString(R.string.ccno))) {
            AddCard.this.startActivity(new Intent(AddCard.this, ODAcCreation.class));
            AddCard.this.finish();
        }

        super.onStart();
    }

    @OnTextChanged(value = R.id.cc_no, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardNumberTextChanged(Editable s) {
        if (!isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
            s.replace(0, s.length(), concatString(getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
        }
    }

    @OnTextChanged(value = R.id.cc_exp, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardDateTextChanged(Editable s) {
        if (!isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
            s.replace(0, s.length(), concatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
        }
    }

    @OnTextChanged(value = R.id.cc_cvv, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardCVCTextChanged(Editable s) {
        if (s.length() > CARD_CVC_TOTAL_SYMBOLS) {
            s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length());
        }
    }

    private boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    public static String removeCharAt(String s, int pos) {
        if (s.length() > pos) return s.substring(0, pos) + s.substring(pos + 1);
        else return "";
    }

    public int validateCard() {
        int flag = 1;

        try {
            String[] str = ccexp.getText().toString().split("/");
            String ccn = ccno.getText().toString().replaceAll("[^0-9]", "");
            int m = 0, y = 0;
            if (str[0].length() > 0) m = parseInt(str[0], 10);
            if (str[1].length() > 1) y = parseInt(str[1], 10);

            if (ccn.length() < 16) {
                desc = "Invalid card no";
                flag = 0;
            } else if (cccvv.getText().toString().length() < 3) {
                desc = "Invalid secret";
                flag = 0;
            } else if (y < 19 || m > 12 || (y == 19 && m < 3) ||  m < 1) {
                desc = "Credit card is expired";
                flag = 0;
            }
            return flag;
        } catch (Error e) {
            Log.e("ICICI-TEST", ""+e);
            return 0;
        }
    }

    public int formfill() {
        if (ccholder.getText().toString().length() < 4 || ccno.getText().toString().length() < 19 || ccexp.getText().toString().length() < 5 || cccvv.getText().toString().length() < 3) {
            desc = "Complete the details first";
            return 0;
        } else return 1;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}