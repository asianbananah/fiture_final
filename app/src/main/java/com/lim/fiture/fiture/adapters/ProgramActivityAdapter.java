package com.lim.fiture.fiture.adapters;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AddProgramDetails;
import com.lim.fiture.fiture.models.Program;

import java.util.ArrayList;

/**
 * Created by User on 26/09/2018.
 */

public class ProgramActivityAdapter extends RecyclerView.Adapter<ProgramActivityAdapter.ViewHolder> {
    private Context context;
    public ArrayList<Program> program;
    private DatabaseReference programReference;
    private DatabaseReference programExerciseReference;


    public ProgramActivityAdapter(Context context, ArrayList<Program> program) {
        this.context = context;
        this.program = program;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(program.get(position).getProgramImages().get(0));
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.programImage);
            }
        });

        holder.programName.setText(program.get(position).getProgramsName());
        holder.programType.setText(program.get(position).getProgramType());
        holder.programWeeks.setText(program.get(position).getProgramWeeks());
        holder.programDifficulty.setText(program.get(position).getProgramDifficulty());

        programReference = FirebaseDatabase.getInstance().getReference("Program");
        programExerciseReference = FirebaseDatabase.getInstance().getReference("ProgramExercise");
        holder.deleteBtn.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Delete Program:");
            builder.setMessage("Are you sure you want to delete this program?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                programReference.child(program.get(position).getProgramsId()).removeValue();
                programExerciseReference.child(program.get(position).getProgramsId()).removeValue();
                program.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), program.size());

            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        });



    }

    @Override
    public int getItemCount() {
        return program.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView programName, programType, programWeeks, programDifficulty;
        ImageButton deleteBtn;
        ImageView programImage;


        public ViewHolder(View itemView) {
            super(itemView);
            programName = itemView.findViewById(R.id.programName);
            programType = itemView.findViewById(R.id.programType);
            programWeeks = itemView.findViewById(R.id.programWeeks);
            programDifficulty = itemView.findViewById(R.id.programDifficulty);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            programImage = itemView.findViewById(R.id.programImage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, AddProgramDetails.class)
                            .putExtra("program", program.get(getAdapterPosition()))
                            .putExtra("action", "edit"));

                }
            });
        }
    }

}

