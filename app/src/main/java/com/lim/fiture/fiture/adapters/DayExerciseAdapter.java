package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.opengl.Visibility;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.ProgramExercise;
import com.lim.fiture.fiture.models.ProgramTracker;
import com.lim.fiture.fiture.util.GlobalUser;

import java.util.ArrayList;

/**
 * Created by User on 05/10/2018.
 */

public class DayExerciseAdapter extends RecyclerView.Adapter<DayExerciseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProgramExercise> programExercises;
//    private ArrayList<Program> programs;
//    ProgramExercise programExercise;
//    Program program;
//    boolean programTfalse = true;

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

        TextView exerciseName, exerciseReps, doneTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseReps = itemView.findViewById(R.id.exerciseReps);
            doneTxt = itemView.findViewById(R.id.doneTxt);

//            Log.d("moymoygwaps", "Programs size: " + programs.size());
//            Log.d("moymoygwaps", "ProgramExercies size: " + programExercises.size());
//
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProgramTracker")
//                    .child(GlobalUser.getmUser().getiD())
//                    .child(programs.get(getAdapterPosition()).getProgramsId())
//                    .child(programExercises.get(getAdapterPosition()).getProgramExerciseId());
//
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d("id", "onDataChange: "+GlobalUser.getmUser().getiD());
//                    Log.d("programsid", "onDataChange: "+programs.get(getAdapterPosition()).getProgramsId());
//                    Log.d("programexeid", "onDataChange: "+programExercises.get(getAdapterPosition()).getProgramExerciseId());
//                    ProgramTracker programTracker1 = dataSnapshot.getValue(ProgramTracker.class);
//                    if (!programTracker1.isProgramExerciseFinished())
//                        programTfalse = true;
//
//                    doneTxt.setText(View.VISIBLE);
//
//
//
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

        }
    }
}
