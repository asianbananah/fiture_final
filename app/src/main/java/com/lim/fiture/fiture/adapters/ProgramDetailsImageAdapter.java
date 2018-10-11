package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lim.fiture.fiture.models.Exercise;

import java.util.ArrayList;

/**
 * Created by User on 03/10/2018.
 */

public class ProgramDetailsImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> programImages;

    public ProgramDetailsImageAdapter(Context context, ArrayList<String> programImages) {
        this.context = context;
        this.programImages = programImages;
    }

    @Override
    public int getCount() {
        return programImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ImageView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(programImages.get(position));
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(mImageView);
            }
        });
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);
    }
}
