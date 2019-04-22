package com.lim.fiture.fiture.fragments;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.models.WeightHistory;
import com.lim.fiture.fiture.util.GlobalUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private ImageView profileCoverPic;
    private CircleImageView profilePic, beginnerImg, intermediateImg, expertImg, loseImg, maintainImg, gainImg;
    private TextView userNameTxt, emailTxt, genderTxt, birthDateTxt, heightTxt, weightTxt, fitnessLevelTxt, fitnessGoalTxt;
    private LineChart lineChart;

    private View rootView;
    private User mUser;

    private int count = 0;
    private ArrayList<WeightHistory> weightHistories;
    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = (User) getArguments().getSerializable("user");
        Log.d("userCheck22", mUser.getiD());
        findViews();
        getUpdatedUser();
        getWeightHistory();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static UserProfileFragment newInstance(User user) {
        Bundle args = new Bundle();

        UserProfileFragment fragment = new UserProfileFragment();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    private void setProfileData(){
        Glide.with(getActivity()).load(R.drawable.profile_cover).into(profileCoverPic);
        Glide.with(getActivity()).load(mUser.getPhotoUrl()).into(profilePic);
        switch (mUser.getFitnessLevel()){
            case "Beginner":
                beginnerImg.setVisibility(View.VISIBLE);
                break;
            case "Intermediate":
                intermediateImg.setVisibility(View.VISIBLE);
                break;
            case "Expert":
                expertImg.setVisibility(View.VISIBLE);
                break;
        }
        switch (mUser.getFitnessGoal()){
            case "Lose":
                loseImg.setVisibility(View.VISIBLE);
                break;
            case "Maintain":
                maintainImg.setVisibility(View.VISIBLE);
                break;
            case "Gain":
                gainImg.setVisibility(View.VISIBLE);
                break;
        }
        fitnessLevelTxt.setText(mUser.getFitnessLevel());
        fitnessGoalTxt.setText(mUser.getFitnessGoal());
        userNameTxt.setText(mUser.getFirstName() + " " + mUser.getLastName());
        emailTxt.setText(mUser.getEmail());
        genderTxt.setText(mUser.getGender());
        birthDateTxt.setText(mUser.getDateOfBirth());
        heightTxt.setText(mUser.getHeightInCm()+"cm");
        weightTxt.setText(mUser.getWeightInKg()+"kg");

    }

    private void findViews(){
        profileCoverPic = rootView.findViewById(R.id.profileCoverPic);
        profilePic = rootView.findViewById(R.id.profilePic);
        beginnerImg = rootView.findViewById(R.id.beginnerImg);
        intermediateImg = rootView.findViewById(R.id.intermediateImg);
        expertImg = rootView.findViewById(R.id.expertImg);
        loseImg = rootView.findViewById(R.id.loseImg);
        maintainImg = rootView.findViewById(R.id.maintainImg);
        gainImg = rootView.findViewById(R.id.gainImg);
        userNameTxt = rootView.findViewById(R.id.userNameTxt);
        emailTxt = rootView.findViewById(R.id.emailTxt);
        genderTxt = rootView.findViewById(R.id.genderTxt);
        birthDateTxt = rootView.findViewById(R.id.birthDateTxt);
        heightTxt = rootView.findViewById(R.id.heightTxt);
        weightTxt = rootView.findViewById(R.id.weightTxt);
        fitnessLevelTxt = rootView.findViewById(R.id.fitnessLevelTxt);
        fitnessGoalTxt = rootView.findViewById(R.id.fitnessGoalTxt);
        lineChart = rootView.findViewById(R.id.chart);

    }

    private void getUpdatedUser(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(GlobalUser.getmUser().getiD());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                setProfileData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getWeightHistory(){
        weightHistories = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserWeightHistory").child(GlobalUser.getmUser().getiD());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    WeightHistory weightHistory = snapshot.getValue(WeightHistory.class);
                    weightHistories.add(weightHistory);
                    if (weightHistories.size() >= dataSnapshot.getChildrenCount()){
                        Log.d("weightHistoryy", "last iteration");
                        List<Entry> entries = new ArrayList<Entry>();
                        count = 0;
                        for(WeightHistory weightHistory1 : weightHistories){
                            count++;
                            Entry entry = new Entry(count, weightHistory1.getWeightInKg());
                            entries.add(entry);
                        }
                        LineDataSet dataSet = new LineDataSet(entries, "Weight History"); // add entries to dataset
                        dataSet.setColor(Color.parseColor("#2980b9"));
                        dataSet.setValueTextColor(Color.parseColor("#2980b9"));
                        LineData lineData = new LineData(dataSet);
                        lineChart.setData(lineData);
                        lineChart.invalidate(); // refresh

                        IAxisValueFormatter formatter = new IAxisValueFormatter() {

                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
//                                Log.d("moymoygwaps", "dateSize: " + weightHistories.size());
                                String formattedDate = DateFormat.getInstance().format(weightHistories.get(0).getDate());
                                return formattedDate;
//                                return "";
                            }

                        };

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                        xAxis.setValueFormatter(formatter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
