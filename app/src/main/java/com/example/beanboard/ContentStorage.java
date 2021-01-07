package com.example.beanboard;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContentStorage {

    private static FirebaseDatabase mDatabase;

    public static boolean saveRoute(Route route){

        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        //DatabaseReference mRef =  database.getReference().child("Routes").push();
        //mRef.setValue(route);
        DatabaseReference dbRef;
        dbRef = database.getReference("/test/data/message1");
        dbRef.setValue("Hello");

        Log.d("new route", route.routeName);
        return true;
    }

}
