package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.ProgramDetailsActivity;
import com.lim.fiture.fiture.models.Program;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

/**
 * Created by User on 04/10/2018.
 */

public class UserProgramsAdapter extends RecyclerView.Adapter<UserProgramsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Program> programs;

    public UserProgramsAdapter(Context context, ArrayList<Program> programs) {
        this.context = context;
        this.programs = programs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_program_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(programs.get(position).getProgramImages().get(0));
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.programImage);
            }
        });

        holder.titleProgramName.setText(programs.get(position).getProgramsName());
        holder.titleProgramDescription.setText(programs.get(position).getProgramDesc());
        holder.titleProgramWeeks.setText(programs.get(position).getProgramWeeks());
        holder.titleProgramType.setText(programs.get(position).getProgramType());
        holder.titleProgramDifficulty.setText(programs.get(position).getProgramDifficulty());
        holder.programName.setText(programs.get(position).getProgramsName());
        holder.programDescription.setText(programs.get(position).getProgramDesc());
        holder.weeksTxt.setText(programs.get(position).getProgramWeeks());
        holder.typeTxt.setText(programs.get(position).getProgramType());
        holder.difficultyTxt.setText(programs.get(position).getProgramDifficulty());
//        holder.viewProgramBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//            }
//        });
//        holder.exercisesTxt.setText(programs.get(position).get);
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleProgramName, titleProgramDescription, titleProgramWeeks, titleProgramType, titleProgramDifficulty, titleNumExercises;
        ImageView programImage;
        TextView programName, programDescription, exercisesTxt, weeksTxt, typeTxt, difficultyTxt, viewProgramBtn;
        FoldingCell foldingCell;
        Button startProgram;

        public ViewHolder(View itemView) {
            super(itemView);

            startProgram = itemView.findViewById(R.id.startProgram);
            titleProgramName = itemView.findViewById(R.id.titleProgramName);
            titleProgramDescription = itemView.findViewById(R.id.titleProgramDescription);
            titleProgramWeeks = itemView.findViewById(R.id.titleProgramWeeks);
            titleProgramType = itemView.findViewById(R.id.titleProgramType);
            titleProgramDifficulty = itemView.findViewById(R.id.titleProgramDifficulty);
            titleNumExercises = itemView.findViewById(R.id.titleNumExercises);
            programImage = itemView.findViewById(R.id.programImage);
            programName = itemView.findViewById(R.id.programName);
            programDescription = itemView.findViewById(R.id.programDescription);
            exercisesTxt = itemView.findViewById(R.id.exercisesTxt);
            weeksTxt = itemView.findViewById(R.id.weeksTxt);
            typeTxt = itemView.findViewById(R.id.typeTxt);
            difficultyTxt = itemView.findViewById(R.id.difficultyTxt);
            viewProgramBtn = itemView.findViewById(R.id.viewProgramBtn);
            viewProgramBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ProgramDetailsActivity.class)
                            .putExtra("action", "myProgram")
                            .putExtra("program", programs.get(getAdapterPosition())));
                    //   startProgram.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Program details", Toast.LENGTH_SHORT).show();


                }


            });

            foldingCell = itemView.findViewById(R.id.folding_cell);
            foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                    foldingCell.toggle(false);
                }
            });

        }
    }
}
