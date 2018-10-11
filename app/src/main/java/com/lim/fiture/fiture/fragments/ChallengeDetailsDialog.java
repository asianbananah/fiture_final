package com.lim.fiture.fiture.fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.DoProgramActivity;
import com.lim.fiture.fiture.models.DailyChallenge;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeDetailsDialog extends DialogFragment {

    private TextView challengeTitle, challengeDesc;
    private Button closeBtn;
    private DailyChallenge dailyChallenge;

    private View rootView;

    public ChallengeDetailsDialog() {
    }

    public static ChallengeDetailsDialog newInstance(DailyChallenge dailyChallenge) {
        Bundle args = new Bundle();
        args.putSerializable("dailyChallenge",dailyChallenge);
        ChallengeDetailsDialog fragment = new ChallengeDetailsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_challenge_details, container);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dailyChallenge = (DailyChallenge) getArguments().getSerializable("dailyChallenge");
        challengeTitle = rootView.findViewById(R.id.challengeTitle);
        challengeDesc = rootView.findViewById(R.id.challengeDesc);
        closeBtn = rootView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        challengeTitle.setText(dailyChallenge.getChallengeName().toUpperCase());
        challengeDesc.setText(dailyChallenge.getChallengeDesc());
    }
}
