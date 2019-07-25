package com.example.ongraph.socialapp;

import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        forget();
    }

    public void forget()
    {
        final TextInputLayout input = (TextInputLayout) findViewById(R.id.textInputLayout);
        EditText email = (EditText) findViewById(R.id.forget_email);
        final String forget_email = email.getText().toString();
        Button forget_button = (Button) findViewById(R.id.submit_button);

        forget_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.requestPasswordResetInBackground(forget_email,
                        new RequestPasswordResetCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // An email was successfully sent with reset instructions.
                                } else {
                                    // Something went wrong. Look at the ParseException to see what's up.
                                }
                            }
                        });
            }
        });
    }
}

