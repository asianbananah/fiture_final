package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AddDailyChallengesDetails;
import com.lim.fiture.fiture.models.DailyChallenge;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by User on 08/10/2018.
 */

public class DailyChallengeAdapter extends RecyclerView.Adapter<DailyChallengeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DailyChallenge> dailyChallenges;
    private int[] overlayColors;

    public DailyChallengeAdapter(Context context, ArrayList<DailyChallenge> dailyChallenges, int[] overlayColors) {
        this.context = context;
        this.dailyChallenges = dailyChallenges;
        this.overlayColors = overlayColors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_challenge_layout_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("imagesDaily", "onBindViewHolder: "+dailyChallenges.get(position).getChallengeImages().get(0) );
        StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(dailyChallenges.get(position).getChallengeImages().get(0));
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.dailyChallengeImage);
            }
        });
        holder.dailyChallengeName.setText(dailyChallenges.get(position).getChallengeName());
        Random random = new Random();
        int rand = random.nextInt(overlayColors.length);
        holder.overlayImage.setImageResource(overlayColors[rand]);
    }

    @Override
    public int getItemCount() {
        return dailyChallenges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dailyChallengeImage, overlayImage;
        TextView dailyChallengeName;

        public ViewHolder(View itemView) {
            super(itemView);

            dailyChallengeImage = itemView.findViewById(R.id.dailyChallengeImage);
            overlayImage = itemView.findViewById(R.id.overlayImage);
            dailyChallengeName = itemView.findViewById(R.id.dailyChallengeName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, AddDailyChallengesDetails.class)
                    .putExtra("dailyChallenge", dailyChallenges.get(getAdapterPosition()))
                    .putExtra("action", "edit"));
                }
            });
        }
    }
}
