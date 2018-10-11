package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.ExecuteChallengeActivity;
import com.lim.fiture.fiture.fragments.UserDailyChallenges2Fragment;
import com.lim.fiture.fiture.fragments.UserDailyChallengesFragment;
import com.lim.fiture.fiture.models.DailyChallenge;

import java.util.ArrayList;

/**
 * Created by User on 10/10/2018.
 */

public class UserDailyChallengeAdapter extends PagerAdapter {

    Context context;
    ArrayList<DailyChallenge> dailyChallenges;
    int adapterType;

    public UserDailyChallengeAdapter(Context context, ArrayList<DailyChallenge> dailyChallenges, int adapterType) {
        this.context = context;
        this.dailyChallenges = dailyChallenges;
        this.adapterType = adapterType;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_daily_challenge_layout_item, null);
        try {

            RelativeLayout linMain = (RelativeLayout) view.findViewById(R.id.linMain);
            ImageView imageCover = (ImageView) view.findViewById(R.id.imageCover);
            TextView challengeTitle = view.findViewById(R.id.challengeTitle);
            linMain.setTag(position);

            switch (adapterType)
            {
                case UserDailyChallenges2Fragment.ADAPTER_TYPE_TOP:
                    linMain.setBackgroundResource(R.drawable.shadow);
                    break;
                case UserDailyChallenges2Fragment.ADAPTER_TYPE_BOTTOM:
                    linMain.setBackgroundResource(0);
                    break;
            }

            StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(dailyChallenges.get(position).getChallengeImages().get(0));
            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(imageCover);
                    Glide.with(context).load(uri).into(glideDrawableImageViewTarget);
                }
            });

            challengeTitle.setText(dailyChallenges.get(position).getChallengeName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ExecuteChallengeActivity.class)
                        .putExtra("dailyChallenge",dailyChallenges.get(position)));
                }
            });
            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return dailyChallenges.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }


}
