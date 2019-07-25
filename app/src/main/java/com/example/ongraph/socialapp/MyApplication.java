package com.example.ongraph.socialapp;

import android.app.Application;
import com.parse.Parse;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("zsCKcEJGcEtJruOKcjdra7l1UoyDtH16KWCdrCKE")
                .clientKey("xgGWy06RQlzC4dKLgmHFeQ9WmHpDkt7wkcVGkRUu")
                .server("https://parseapi.back4app.com/").build()
        );
    }
}
