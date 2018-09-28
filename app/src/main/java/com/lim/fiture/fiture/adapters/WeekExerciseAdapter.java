package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.ProgramExercise;

import java.util.ArrayList;

/**
 * Created by User on 26/09/2018.
 */

public class WeekExerciseAdapter extends RecyclerView.Adapter<WeekExerciseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProgramExercise> programExercises;

    public WeekExerciseAdapter() {
    }

    public WeekExerciseAdapter(Context context, ArrayList<ProgramExercise> programExercises) {
        this.context = context;
        this.programExercises = programExercises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_exercise_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.exerciseNameTxt.setText(programExercises.get(position).getExerciseName());
        holder.setsTxt.setText(String.valueOf(programExercises.get(position).getSets()));
        holder.repsTxt.setText(String.valueOf(programExercises.get(position).getReps()));
        holder.restTxt.setText(String.valueOf(programExercises.get(position).getRest()));
    }

    @Override
    public int getItemCount() {
        return programExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton deleteExerciseBtn;
        TextView exerciseNameTxt, setsTxt, repsTxt, restTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseNameTxt = itemView.findViewById(R.id.exerciseNameTxt);
            setsTxt = itemView.findViewById(R.id.setsTxt);
            repsTxt = itemView.findViewById(R.id.repsTxt);
            restTxt = itemView.findViewById(R.id.restTxt);
            deleteExerciseBtn = itemView.findViewById(R.id.deleteExerciseBtn);
            deleteExerciseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "TODO: Implement this soon", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
