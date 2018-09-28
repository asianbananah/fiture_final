package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.Exercise;

import java.util.ArrayList;

public class AdminActivityAdapter extends RecyclerView.Adapter <AdminActivityAdapter.ViewHolder>{

    Context context;
    public ArrayList <Exercise> exercises;


    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_adapter,parent,false);
        AdminActivityAdapter.ViewHolder viewHolder = new AdminActivityAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    holder.exerciseName.setText(exercises.get(position).getExerciseName());
    holder.mainMuscleGroup.setText(exercises.get(position).getMainMuscleGroup());
    holder.otherMuscleGroup.setText(exercises.get(position).getOtherMuscleGroup());
    holder.type.setText(exercises.get(position).getType());
    holder.equipment.setText(exercises.get(position).getEquipment());
    holder.difficulty.setText(exercises.get(position).getDifficulty());
        holder.deleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercises.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), exercises.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText exerciseName, mainMuscleGroup, otherMuscleGroup, type, equipment, difficulty;
        private ImageButton deleteExercise;
        public ViewHolder(View itemView) {
            super(itemView);

            exerciseName = itemView.findViewById(R.id.exerciseName);
            mainMuscleGroup = itemView.findViewById(R.id.mainMuscleGroup);
            otherMuscleGroup = itemView.findViewById(R.id.otherMuscleGroup);
            type = itemView.findViewById(R.id.type);
            equipment = itemView.findViewById(R.id.equipment);
            difficulty = itemView.findViewById(R.id.difficulty);
            deleteExercise = itemView.findViewById(R.id.deleteBtn);

        }
    }
}
