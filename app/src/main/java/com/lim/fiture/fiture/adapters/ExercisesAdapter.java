package com.lim.fiture.fiture.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AddExerciseStepOne;
import com.lim.fiture.fiture.activities.AdminActivity;
import com.lim.fiture.fiture.fragments.AdminExerciseFragment;
import com.lim.fiture.fiture.models.Exercise;

import java.util.ArrayList;

/**
 * Created by User on 16/09/2018.
 */

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {

    private Context context;
    public ArrayList<Exercise> exercises;
    private AdminExerciseFragment adminExerciseFragment;
    private DatabaseReference exerciseReference;

    public ExercisesAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
        this.adminExerciseFragment = adminExerciseFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(exercises.get(position).getExerciseImages().get(0));
            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(holder.exerciseImage);
                }
            });
            holder.exerciseName.setText(exercises.get(position).getExerciseName());
            holder.exerciseMainMuscleGroup.setText(exercises.get(position).getMainMuscleGroup());
            holder.exerciseType.setText(exercises.get(position).getType());
            holder.exerciseDifficulty.setText(exercises.get(position).getDifficulty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView exerciseImage;
        ImageButton deleteBtn;
        TextView exerciseName, exerciseType, exerciseMainMuscleGroup, exerciseDifficulty;


        //remove
        @SuppressLint("ResourceType")
        public ViewHolder(View itemView) {
            super(itemView);

            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            exerciseType = itemView.findViewById(R.id.exerciseType);
            exerciseMainMuscleGroup = itemView.findViewById(R.id.exerciseMainMuscleGroup);
            exerciseDifficulty = itemView.findViewById(R.id.exerciseDifficulty);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, AddExerciseStepOne.class)
                            .putExtra("exercise", exercises.get(getAdapterPosition()))
                            .putExtra("action", "edit"));

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Delete Exercise");
                    builder.setMessage("Are you sure you want to delete this exercise?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                        exerciseReference.child(exercises.get(getAdapterPosition()).getExerciseId()).removeValue();
                        exercises.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), exercises.size());

                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });


        }
    }
}
