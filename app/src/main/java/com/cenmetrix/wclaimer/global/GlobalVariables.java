package com.cenmetrix.wclaimer.global;

import com.cenmetrix.wclaimer.models.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class GlobalVariables {
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wclaimer-538ab-default-rtdb.firebaseio.com/");

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build();

    public static UserModel userDetails = null;
}
