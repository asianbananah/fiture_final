package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.customtoast.chen.customtoast.CustomToast;
import com.dd.morphingbutton.MorphingButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.ProgramDetailsActivity;
import com.lim.fiture.fiture.fragments.UserDailyChallengesFragment;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.util.CustomToastNew;

import java.util.ArrayList;


/**
 * Created by User on 24/09/2018.
 */

public class UserRecommendedProgramsAdapter extends RecyclerView.Adapter<UserRecommendedProgramsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Program> programs;
    private User mUser;
    private CustomToastNew customToast;

    private DatabaseReference databaseUserProgram;

    public UserRecommendedProgramsAdapter(Context context, ArrayList<Program> programs, User mUser) {
        this.context = context;
        this.programs = programs;
        this.mUser = mUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        customToast = new CustomToastNew(context); //pass context as parameter
        customToast.setTextColor(context.getResources().getColor(R.color.white));
        customToast.setBackground(context.getResources().getColor(R.color.color_16));

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_program_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(programs.get(position).getProgramImages().get(0));
            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(holder.programImage);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.programName.setText(programs.get(position).getProgramsName());
        holder.programType.setText(programs.get(position).getProgramType());
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView programImage;
        TextView programName, programType;
        Button addProgramButton;

        public ViewHolder(View itemView) {
            super(itemView);


            programImage = itemView.findViewById(R.id.programImage);
            programName = itemView.findViewById(R.id.programName);
            programType = itemView.findViewById(R.id.programType);
            addProgramButton = itemView.findViewById(R.id.addProgramButton);

            databaseUserProgram = FirebaseDatabase.getInstance().getReference("UserPrograms");
            addProgramButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseUserProgram.child(mUser.getiD()).child(programs.get(getAdapterPosition()).getProgramsId()).setValue(programs.get(getAdapterPosition()));

                    addProgramButton.setText("PROGRAM ADDED!");

                    addProgramButton.setBackgroundColor(Color.parseColor("#fbc531"));
                    customToast.showSuccessToast("Program Successfully Added");
                    programs.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), programs.size());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ProgramDetailsActivity.class)
                            .putExtra("program", programs.get(getAdapterPosition())));
                }
            });
        }
    }
}
