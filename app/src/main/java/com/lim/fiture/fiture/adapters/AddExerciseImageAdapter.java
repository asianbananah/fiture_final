package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AddExerciseStepThree;

import java.util.ArrayList;

/**
 * Created by User on 12/09/2018.
 */

public class AddExerciseImageAdapter extends RecyclerView.Adapter <AddExerciseImageAdapter.ViewHolder> {

    private Context context;
    public ArrayList<Bitmap> images;
    private boolean isEdit = false;
    private ArrayList<Uri> resumedPics;

    //TODO: imageDatas contain the data to be uploaded to firebase; Just take note
    public ArrayList<Uri> imageDatas;

    public AddExerciseImageAdapter(Context context, ArrayList<Uri> resumedPics, boolean isEdit){
        this.context = context;
        this.resumedPics = resumedPics;
        this.isEdit = isEdit;
    }

    public AddExerciseImageAdapter(Context context) {
        this.context = context;
        images = new ArrayList<>();
        imageDatas = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_exercise_image_adapter,parent,false);
        AddExerciseImageAdapter.ViewHolder viewHolder = new AddExerciseImageAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(images != null) {
            holder.exerciseImageView.setImageBitmap(images.get(position));
        } else {
            Glide.with(context).load(resumedPics.get(position)).into(holder.exerciseImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(images != null) {
            Log.d("debuggg", "size1: " + images.size());
            return images.size();
        } else {
            Log.d("debuggg", "size2: " + resumedPics.size());
            return resumedPics.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView exerciseImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            exerciseImageView = itemView.findViewById(R.id.exerciseImageView);
        }
    }
}
