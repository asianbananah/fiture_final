package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.Exercise;

import java.util.ArrayList;

/**
 * Created by User on 03/10/2018.
 */

public class ProgramExercisesAdapter extends RecyclerView.Adapter<ProgramExercisesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Exercise> exercises;

    public ProgramExercisesAdapter(Context context, ArrayList<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_exercise_layout_item_two, parent, false);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.exerciseName.setText(exercises.get(position).getExerciseName());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView exerciseImage;
        TextView exerciseName;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            exerciseName = itemView.findViewById(R.id.exerciseName);
        }
    }
}
