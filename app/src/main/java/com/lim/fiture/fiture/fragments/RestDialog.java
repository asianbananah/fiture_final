package com.lim.fiture.fiture.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.DoProgramActivity;

/**
 * Created by User on 07/10/2018.
 */

public class RestDialog extends DialogFragment {

    private int rest;
    private Button closeBtn;
    private CountDownTimer countDownTimer;

    public RestDialog() {
    }

    public static RestDialog newInstance(int rest) {
        Bundle args = new Bundle();
        args.putInt("rest",rest);
        RestDialog fragment = new RestDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rest_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rest = getArguments().getInt("rest");
        closeBtn = view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                exitDialog();
            }
        });

        TextView charTxt = view.findViewById(R.id.charTxt);
        charTxt.setText("That was great work. Take a rest for a" + "\n" +"while.");

        countDownTimer = new CountDownTimer(rest*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                closeBtn.setText("Resuming in " + String.format("%d:%02d", minutes, seconds));
            }

            public void onFinish() {
                exitDialog();
            }

        }.start();
    }

    public void exitDialog(){
        ((DoProgramActivity)getActivity()).updateCurrentSet();
        dismiss();
    }
}
