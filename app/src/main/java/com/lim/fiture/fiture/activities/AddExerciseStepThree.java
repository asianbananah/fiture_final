package com.lim.fiture.fiture.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.AddExerciseImageAdapter;
import com.lim.fiture.fiture.models.Exercise;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddExerciseStepThree extends AppCompatActivity {

    private static final String TAG = "AddExerciseStepThree";
    private FloatingActionButton addExerciseBtn;
    private RecyclerView exerciseImagesRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private AddExerciseImageAdapter adapter;
    private Button finishBtn;
    private Exercise exercise;
    private ArrayList<String> exerciseImageURLs = new ArrayList<>();

    //TODO: instance variables for firebase database
    private DatabaseReference exerciseReference;

    //TODO: instance variable for firebase storage
    private StorageReference storageReference;
    private int count = 0;
    private String action = "";

    private ArrayList<Uri> resumedPics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_step_three);

        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");
        Log.d(TAG, "exerciseReference: " + exerciseReference.toString());

        storageReference = FirebaseStorage.getInstance().getReference();
        Log.d(TAG, "storageReference: " + storageReference.toString());

        findViews();
        getDataFromPreviousActivity();
    }

    private void getDataFromPreviousActivity() {

        exercise = (Exercise) getIntent().getSerializableExtra("exercise");
        if (getIntent().getStringExtra("action") != null
                && getIntent().getStringExtra("action").equals("edit")) {
            action = "edit";
            resumeValues();
        }
    }

    private void resumeValues() {
        for (int i = 0 ; i < exercise.getExerciseImages().size(); i++){
            Log.d("debuggg", exercise.getExerciseImages().get(0));
            StorageReference imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(exercise.getExerciseImages().get(0));
            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//                    Glide.with(context).load(uri).into(holder.exerciseImage);
                    resumedPics.add(uri);
                }
            });
        }

        Log.d("debuggg", resumedPics.toString());
        adapter = new AddExerciseImageAdapter(this, resumedPics, true);
        exerciseImagesRecycler.setAdapter(adapter);
    }

    private void findViews() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
        getSupportActionBar().setTitle("Exercise Images");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addExerciseBtn = findViewById(R.id.addImageBtn);
        addExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }


        });
        adapter = new AddExerciseImageAdapter(this);

        exerciseImagesRecycler = findViewById(R.id.exerciseImagesRecycler);
        exerciseImagesRecycler.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        exerciseImagesRecycler.setLayoutManager(layoutManager);
        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(view -> {
            if(action.equals("edit")) {
                exerciseReference.child(exercise.getExerciseId()).setValue(exercise);
                Toast.makeText(this, "Exercise saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                uploadImages(adapter.imageDatas);
            }

        });

        resumedPics = new ArrayList<>();
    }

    public void uploadImages(ArrayList<Uri> imageDatas) {
        //TODO: just checking for nulls to avoid errors
        if (imageDatas != null) {
            for (Uri imageData : imageDatas) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                StorageReference ref = storageReference.child("exerciseImages/" + UUID.randomUUID().toString());
                ref.putFile(imageData)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Log.d(TAG, ref.toString());
                                exerciseImageURLs.add(ref.toString());
                                Toast.makeText(AddExerciseStepThree.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                if (count++ == imageDatas.size() - 1) {
                                    // TODO: This is the last iteration; save the data here
                                    saveExercise();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddExerciseStepThree.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            }
        }
    }

    public void saveExercise() {
        exercise.setExerciseImages(exerciseImageURLs);
        Log.d(TAG, exercise.toString());

        //TODO: first get the id of the pushed exerciseReference from firebase
        String exerciseId = exerciseReference.push().getKey();
        Log.d(TAG, "exerciseId: " + exerciseId);

        //TODO: next, use the exerciseId from above to set the exercise ID of this exercise
        exercise.setExerciseId(exerciseId);

        //TODO: then push
        exerciseReference.child(exerciseId).setValue(exercise);
        Toast.makeText(this, "Exercise saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        adapter.images.add(bitmap);
                        adapter.imageDatas.add(data.getData());
                        adapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
