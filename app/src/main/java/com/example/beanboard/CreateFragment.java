package com.example.beanboard;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.beanboard.R;

import java.util.ArrayList;


///**
// * A simple {@link Fragment} subclass.
// * Use the {@link CreateFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CreateFragment extends Fragment {

    private static View view;

    public static ArrayList<Integer> getSelectedHolds() {
        return selectedHolds;
    }

    public static void setSelectedHolds(ArrayList<Integer> selectedHolds) {
        CreateFragment.selectedHolds = selectedHolds;
    }

    private static ArrayList<Integer> selectedHolds;

    public CreateFragment() {
        // Required empty public constructor
    }

    public static boolean checkHold(int holdId){
        if (!selectedHolds.isEmpty()){
            for (int i = 0; i < selectedHolds.size(); i++) {
                if (selectedHolds.get(i) == holdId) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }

    }

    public static void addHold(int holdId) {
        selectedHolds.add(holdId);
    }

    public static void removeHold(int holdID){
        for (int i = 0; i < selectedHolds.size(); i++) {
            if (selectedHolds.get(i) == holdID) {
                selectedHolds.remove(i);
            }
        }

    }

    public static void changePageBack() {
        view.findViewById(R.id.saveRouteDetails).setVisibility(View.GONE);
        view.findViewById(R.id.chooseHoldsPage).setVisibility(View.VISIBLE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create, container, false);
        selectedHolds = new ArrayList<Integer>();

        Spinner spinner = (Spinner) view.findViewById(R.id.enterGrade);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.grades, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        view.findViewById(R.id.saveRouteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.saveRouteDetails).setVisibility(View.VISIBLE);
                view.findViewById(R.id.chooseHoldsPage).setVisibility(View.GONE);
            }
        });

        return view;
    }


}
