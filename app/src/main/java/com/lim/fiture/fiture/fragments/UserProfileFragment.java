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
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private ImageView profileCoverPic;
    private CircleImageView profilePic, beginnerImg, intermediateImg, expertImg, loseImg, maintainImg, gainImg;
    private TextView userNameTxt, emailTxt, genderTxt, birthDateTxt, heightTxt, weightTxt, fitnessLevelTxt, fitnessGoalTxt;

    private View rootView;
    private User mUser;

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
        setProfileData();
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
    }

}
