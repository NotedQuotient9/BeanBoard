package com.example.beanboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SelectFragment extends Fragment {

    private MainActivity main;
    private View view;
    private ArrayList<Route> routeList;

    public SelectFragment() {
        // Required empty public constructor
    }

    void initializeData(){
        routeList = new ArrayList<>();

        ArrayList<Integer> holds = new ArrayList<>();
        holds.add(1);
        holds.add(6);
        holds.add(3);

        Route r = new Route(holds, "testUser", "test route 1", "5+");

        holds.clear();

        holds.add(2);
        holds.add(8);
        holds.add(1);
        holds.add(4);

        Route r2 = new Route(holds, "testUser", "test route 2", "6a");

        routeList.add(r);
        routeList.add(r2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select, container, false);
        //initializeData();

        routeList = new ArrayList<>();

        ArrayList<Integer> holds = new ArrayList<>();


        holds.add(2);
        holds.add(8);
        holds.add(1);
        holds.add(4);

        Route r = new Route(holds, "testUser", "test route 1", "5+");

        ArrayList<Integer> holds2 = new ArrayList<>();

        holds2.add(1);
        holds2.add(6);
        holds2.add(3);

        Route r2 = new Route(holds2, "testUser", "test route 2", "6a");

        routeList.add(r2);
        routeList.add(r);

        main = ((MainActivity) getActivity());

        RecyclerView rv = view.findViewById(R.id.rv);


        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter rvAdapter = new routeAdapter(MainActivity.loadedRoutes, main, view.getContext());
//        RecyclerView.Adapter rvAdapter = new routeAdapter(routeList, main);
        rv.setAdapter(rvAdapter);

        Log.d("routes in the list", routeList.toString());
        return view;
    }
}
