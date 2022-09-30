package com.cenmetrix.wclaimer.global;

import android.app.Activity;
import android.content.Intent;

import com.cenmetrix.wclaimer.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GlobalMethods {

    public static FirebaseAuth auth;
    public static FirebaseUser currentUser;

    public static void FirebaseAuthRequest(Activity activity) {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(activity, Login.class);
            activity.startActivity(intent);
            activity.finish();
            return;
        }
    }
}
