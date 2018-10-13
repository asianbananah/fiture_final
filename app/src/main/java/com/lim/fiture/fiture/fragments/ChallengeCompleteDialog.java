package com.lim.fiture.fiture.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.ExecuteChallengeActivity;
import com.lim.fiture.fiture.activities.UserMainActivity;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.util.GlobalUser;

public class ChallengeCompleteDialog extends DialogFragment {

    private View rootView;
    private Button closeBtn;
    private DailyChallenge dailyChallenge;
    private TextView challengeDesc;

    public ChallengeCompleteDialog() {
        // Required empty public constructor
    }

    public static ChallengeCompleteDialog newInstance(DailyChallenge dailyChallenge) {

        Bundle args = new Bundle();
        args.putSerializable("dailyChallenge", dailyChallenge);
        ChallengeCompleteDialog fragment = new ChallengeCompleteDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_challenge_complete_dialog, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dailyChallenge = (DailyChallenge) getArguments().getSerializable("dailyChallenge");
        challengeDesc = rootView.findViewById(R.id.challengeDesc);
        String challengeText = challengeDesc.getText().toString().replace("{0}", String.valueOf(dailyChallenge.getChallengePoints()));
        challengeDesc.setText(challengeText);
        closeBtn = rootView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        DatabaseReference completedChallengeRef = FirebaseDatabase.getInstance().getReference().child("UserCompletedChallenges");
        completedChallengeRef.child(GlobalUser.getmUser().getiD()).child(dailyChallenge.getChallengeId()).setValue(dailyChallenge);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = GlobalUser.getmUser();
        user.setPoints(user.getPoints() + dailyChallenge.getChallengePoints());
        userRef.child(GlobalUser.getmUser().getiD()).setValue(user);
    }
}
