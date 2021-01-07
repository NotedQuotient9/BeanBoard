package com.example.beanboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.view.View.GONE;

class routeAdapter extends RecyclerView.Adapter<routeAdapter.RouteViewHolder> {

    private MainActivity main;
    private ArrayList<Route> data;
    private Context context;

    public routeAdapter(ArrayList<Route> routeList, MainActivity main, Context context) {

        this.data = routeList;
        this.main = main;
        this.context = context;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View theView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_row, parent, false);

        return new RouteViewHolder(theView);

    }

    @Override
    public void onBindViewHolder(@NonNull final RouteViewHolder holder, final int position) {
        holder.routeName.setText(data.get(position).getRouteName());
        holder.routeGrade.setText(data.get(position).getGrade());
        holder.routeCreatedBy.setText(data.get(position).getCreatedBy());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.currentRoute = data.get(position);
                Toast.makeText(main, "Problem Selected", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_current);
            }
        });
        holder.routeDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Confirm Delete?");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.deleteRouteFromStorage(position);
                                holder.cv.setVisibility(View.GONE);
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
        holder.editRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter New Grade");

                final Spinner input = new Spinner(context);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.grades, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                input.setAdapter(adapter);

                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.get(position).setGrade(input.getSelectedItem().toString());
                        MainActivity.updateRouteInStorage(data.get(position));
                        holder.routeGrade.setText(input.getSelectedItem().toString());
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

        if (data.get(position).isCompleted()){
            holder.routeComplete.setVisibility(View.VISIBLE);
            holder.routeAttempts.setVisibility(View.VISIBLE);
            holder.routeAttempts.setText(data.get(position).getNumberOfAttempts() + " Attempts");
        }
        if (!data.get(position).isCompleted()){
            holder.routeComplete.setVisibility(GONE);
            holder.routeAttempts.setVisibility(GONE);
        }

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    //view holder class for tour recycler lists,
    //links the elements in layout files to the recycler adapter
    public class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView routeName;
        TextView routeGrade;
        TextView routeCreatedBy;
        CardView cv;
        ImageButton routeDeleteButton;
        TextView routeComplete;
        TextView routeAttempts;
        ImageButton editRouteButton;

        public RouteViewHolder(View itemView) {
            super(itemView);

            routeName = (TextView) itemView.findViewById(R.id.routeName);
            routeGrade = (TextView) itemView.findViewById(R.id.routeGrade);
            routeCreatedBy = (TextView) itemView.findViewById(R.id.routeCreatedBy);
            cv = (CardView) itemView.findViewById(R.id.cv);
            routeDeleteButton = (ImageButton) itemView.findViewById(R.id.deleteRouteButton);
            routeComplete = (TextView) itemView.findViewById(R.id.problemComplete);
            routeAttempts = (TextView) itemView.findViewById(R.id.attemptsNumber);
            editRouteButton = (ImageButton) itemView.findViewById(R.id.editRouteButton);

        }
    }
}
