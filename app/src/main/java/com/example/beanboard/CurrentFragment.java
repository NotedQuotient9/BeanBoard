package com.example.beanboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.view.View.GONE;


public class CurrentFragment extends Fragment {

    private static View view;


    public CurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current, container, false);

        if (MainActivity.currentRoute != null){
            showCurrentRoute(MainActivity.currentRoute);
        }

        return view;
    }

    public static void showCurrentRoute(final Route route) {
        view.findViewById(R.id.noProblemText).setVisibility(GONE);
        view.findViewById(R.id.completeCurrentRoute).setVisibility(GONE);
        view.findViewById(R.id.currentRouteInfo).setVisibility(View.VISIBLE);

        TextView currentName = view.findViewById(R.id.currentRouteName);
        currentName.setText(route.getRouteName());

        TextView currentCreator = view.findViewById(R.id.currentRouteCreator);
        currentCreator.setText("By " +route.getCreatedBy());

        TextView currentGrade = view.findViewById(R.id.currentRouteGrade);
        currentGrade.setText(route.getGrade());

        final Button infoButton = view.findViewById(R.id.showRouteInfo);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.infoBox).getVisibility() == GONE){
                    view.findViewById(R.id.infoBox).setVisibility(View.VISIBLE);
                    infoButton.setText("Hide Info");
                } else {
                    view.findViewById(R.id.infoBox).setVisibility(GONE);
                    infoButton.setText("Show Info");
                }

            }
        });

        if (!route.isCompleted()){
            view.findViewById(R.id.completeCurrentRoute).setVisibility(View.VISIBLE);
            view.findViewById(R.id.completeCurrentRoute).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Number of Attempts");

// Set up the input
                    final EditText input = new EditText(view.getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                    builder.setView(input);


// Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            route.setCompleted(true);
                            route.setNumberOfAttempts(Integer.parseInt(input.getText().toString()));
                            view.findViewById(R.id.completeCurrentRoute).setVisibility(GONE);
                            MainActivity.updateRouteInStorage(route);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                }
            });
        }

        View grid = view.findViewById(R.id.holdGrids);
        //MainActivity.clearSelection(view);

        for (int i = 0; i < route.getHolds().size(); i++) {
//            Log.d("holdId", route.getHolds().get(i).toString());
            View parentView = view.getRootView();
            View childView = parentView.findViewById(R.id.holdGrids);
            Log.d("view is: ", childView.toString());
//            Log.d("parent view is", parentView.toString());
            //MainActivity.selectHoldFromTag(i);
            ImageView iv = childView.findViewWithTag(route.getHolds().get(i).toString());
            Log.d("image view found is:", iv.toString());
            iv.setBackground(view.getResources().getDrawable(R.drawable.background));
            //view.findViewWithTag(r.getHolds().get(i)).setBackground(view.getResources().getDrawable(R.drawable.background));
        }





    }
}
