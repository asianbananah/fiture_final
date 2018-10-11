package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.ProgramExercise;

import java.util.ArrayList;

/**
 * Created by User on 05/10/2018.
 */

public class DayExerciseAdapter extends RecyclerView.Adapter<DayExerciseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProgramExercise> programExercises;

    public DayExerciseAdapter(Context context, ArrayList<ProgramExercise> programExercises) {
        this.context = context;
        this.programExercises = programExercises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_exercise_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.exerciseName.setText(programExercises.get(position).getExerciseName().toUpperCase());
        holder.exerciseReps.setText(String.valueOf(programExercises.get(position).getReps()));
    }

    @Override
    public int getItemCount() {
        return programExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView exerciseName, exerciseReps;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseReps = itemView.findViewById(R.id.exerciseReps);
        }
    }
}
