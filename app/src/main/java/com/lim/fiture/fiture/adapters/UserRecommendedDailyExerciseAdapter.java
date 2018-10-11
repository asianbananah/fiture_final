package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.lim.fiture.fiture.activities.DailyChallengesDetailsActivity;
import com.lim.fiture.fiture.models.DailyChallenge;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by User on 09/10/2018.
 */

public class UserRecommendedDailyExerciseAdapter extends RecyclerView.Adapter<UserRecommendedDailyExerciseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DailyChallenge> dailyChallenges;

    public UserRecommendedDailyExerciseAdapter(Context context, ArrayList<DailyChallenge> dailyChallenges) {
        this.context = context;
        this.dailyChallenges = dailyChallenges;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_daily_challenges_layout_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(dailyChallenges.get(position).getChallengeImages().get(0));
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.dailyChallengeImage);
            }
        });

        holder.challengeName.setText(dailyChallenges.get(position).getChallengeName());
        holder.challengeDuration.setText(dailyChallenges.get(position).getChallengeDuration());


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dailyChallengeImage;
        TextView challengeName, challengeDuration;

        public ViewHolder(View itemView) {
            super(itemView);
            dailyChallengeImage = itemView.findViewById(R.id.dailyChallengeImage);
            challengeName = itemView.findViewById(R.id.challengeName);
            challengeDuration = itemView.findViewById(R.id.durationTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, DailyChallengesDetailsActivity.class).putExtra("dailyChallenges", dailyChallenges.get(getAdapterPosition())));
                }
            });
        }
    }
}

