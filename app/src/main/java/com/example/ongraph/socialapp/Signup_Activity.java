package com.example.ongraph.socialapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Signup_Activity extends AppCompatActivity {

    Button signup;
    EditText fullname, phone, email, password, confirm, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);
        signup();
    }

    public void signup() {

        fullname = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.signup_password);
        confirm = (EditText) findViewById(R.id.confirm);

        signup = (Button) findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser user = new ParseUser();

                if (username.getText().toString().equals(user.getUsername()))
                {
                    Toast.makeText(Signup_Activity.this, "Username Already Exist!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    user.setUsername(username.getText().toString());
                    if (password.getText().toString().equals(confirm.getText().toString()))
                        user.setPassword(password.getText().toString());
                    else
                        Toast.makeText(Signup_Activity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    user.setEmail(email.getText().toString());
                    user.put("phone", phone.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                alertDisplayer("Register successful", "enjoy!!!");
                            } else {
                                alertDisplayer("Register Failed", e.getMessage() + " Please Try Again");
                            }
                        }
                    });
                }

            }
        });
    }
    void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_Activity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
