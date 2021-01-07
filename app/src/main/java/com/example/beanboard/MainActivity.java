package com.example.beanboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beanboard.CreateFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Route currentRoute;
    public static ArrayList<Route> loadedRoutes;
    private static Context context;
    private static View view;

    static private float x1,x2;
    static final int MIN_DISTANCE = 150;

    //    private FirebaseAuth mAuth;
//    private FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //loadedRoutes = loadRoutes();
        if (loadedStorageRoute() != null){
            loadedRoutes = loadedStorageRoute();
            Log.d("routes found", loadedRoutes.toString());
        }



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_create, R.id.navigation_current, R.id.navigation_select)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        context = this;

    }

    public ArrayList<Route> loadedStorageRoute(){

        ArrayList<Route> routes = new ArrayList<>();
        File directory = getFilesDir();
        File file = new File(directory, "route");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            routes = (ArrayList<Route>) ois.readObject();
            ois.close();
            return routes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void selectHold(View view) {

        int holdId = Integer.parseInt(view.getTag().toString());
        boolean checked = CreateFragment.checkHold(holdId);
        if (!checked) {
            view.setBackground(view.getResources().getDrawable(R.drawable.background));
            CreateFragment.addHold(holdId);
        } else {
            view.setBackground(null);
            CreateFragment.removeHold(holdId);
        }


    }


    public void clearSelection(View view) {
        LinearLayout holdGrids = findViewById(R.id.holdGrids);
        final int childCount = holdGrids.getChildCount();
        CreateFragment.getSelectedHolds().clear();
        for (int i = 0; i < childCount; i++) {

            View v = holdGrids.getChildAt(i);
            for(int j=0; j < ((ViewGroup)v).getChildCount(); j++) {
                View nextChild = ((ViewGroup)v).getChildAt(j);
                if (nextChild instanceof ImageView) {
                    nextChild.setBackground(null);
                }
            }
        }
    }

    public void saveRoute(View view) {
        View parentView = view.getRootView();
        TextView nameView = parentView.findViewById(R.id.enterName);
        TextView creatorView = parentView.findViewById(R.id.enterCreator);
        Spinner gradeView = parentView.findViewById(R.id.enterGrade);
        ArrayList<Integer> holds = new ArrayList<>();
        holds = CreateFragment.getSelectedHolds();

        Route newRoute = new Route(holds, creatorView.getText().toString(), nameView.getText().toString(), gradeView.getSelectedItem().toString());
        Log.d("route info", newRoute.routeName + newRoute.grade + newRoute.createdBy + newRoute.getHolds().toString());
        //uploadRoute(newRoute);
        saveRouteIntoStorage(newRoute);
        loadedStorageRoute();
        Toast.makeText(this, "New Route Selected", Toast.LENGTH_SHORT).show();
        currentRoute = newRoute;
        CreateFragment.changePageBack();

        NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment);
        navController.navigate(R.id.navigation_current);

        View currentFocus = this.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public void saveRouteIntoStorage(Route route){

        if (loadedStorageRoute() == null){
            loadedRoutes = new ArrayList<>();
            loadedRoutes.add(route);
        } else {
            loadedRoutes.add(route);
        }

        File directory = getFilesDir(); //or getExternalFilesDir(null); for external storage
        File file = new File(directory, "route");

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(loadedRoutes);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void updateStorage() {
        File directory = context.getFilesDir(); //or getExternalFilesDir(null); for external storage
        File file = new File(directory, "route");

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(loadedRoutes);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRouteFromStorage(int routePosition){
        loadedRoutes.remove(routePosition);
        updateStorage();
        Toast.makeText(context, "Route Deleted", Toast.LENGTH_SHORT).show();

    }

    public static void updateRouteInStorage(Route route){
        loadedRoutes.remove(route);
//        route.setCompleted(true);
        loadedRoutes.add(route);
        updateStorage();
        Toast.makeText(context, "Route Updated", Toast.LENGTH_SHORT).show();
    }




}

//to be included in on create
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null) {
//            mAuth.signInAnonymously()
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                FirebaseUser user = mAuth.getCurrentUser();
//                            }
//                        }
//                    });
//        }
//
//
//        mDatabase =  FirebaseDatabase.getInstance();

//    public ArrayList<Route> loadRoutes(){ ;
//        final ArrayList<Route> routeList = new ArrayList<>();
//        DatabaseReference mRef =  mDatabase.getReference().child("routes");
//
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                routeList.clear();
//                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
//                    Route route = postSnapshot.getValue(Route.class);
//                    routeList.add(route);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });
//
//
//        return routeList;
//    }

//    public void uploadRoute(Route route){
//        DatabaseReference mRef =  mDatabase.getReference().child("routes").push();
//        mRef.setValue(route);
//    }