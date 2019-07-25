package com.example.ongraph.socialapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by ongraph on 4/7/17.
 */

public class AppUtils {

    public static void setFragment(int containerViewId, FragmentManager fragmentManager, Fragment fragment, boolean addBackStack) {

        if (!addBackStack) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                fragmentManager.popBackStack();
            }
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        if (addBackStack) {
            fragmentTransaction.addToBackStack("");
        }

        fragmentTransaction.commit();
    }
}
