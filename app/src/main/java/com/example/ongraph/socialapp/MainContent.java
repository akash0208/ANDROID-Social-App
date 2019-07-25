package com.example.ongraph.socialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainContent extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    AppUtils.setFragment(R.id.fragmentContainer,getSupportFragmentManager(),new NewsFragment(),false);
                    return true;
                case R.id.navigation_profile:
                    AppUtils.setFragment(R.id.fragmentContainer,getSupportFragmentManager(),new ProfileFragment(),false);
                    return true;
                case R.id.navigation_search:
                    AppUtils.setFragment(R.id.fragmentContainer,getSupportFragmentManager(), new SearchFragment(), false);
                    return true;
                case R.id.navigation_logout:
                    onClickOnDisconnect();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        AppUtils.setFragment(R.id.fragmentContainer,getSupportFragmentManager(),new NewsFragment(),true);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void onClickOnDisconnect() {

        final CharSequence[] items = {"Yes", "No"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainContent.this);
        builder.setTitle("Are you sure?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Yes")) {
                    ParseUser.logOutInBackground(new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Intent intent = new Intent(MainContent.this , LoginActivity.class);
                                startActivity(intent);
                            }

                        }
                    });

                } else if (items[item].equals("No")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }

}