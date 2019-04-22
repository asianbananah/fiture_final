package com.lim.fiture.fiture.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.models.WeightHistory;
import com.lim.fiture.fiture.util.GlobalUser;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class InputWeightDialog extends DialogFragment {


    public InputWeightDialog() {
        // Required empty public constructor
    }

    private TextView challengeTitle, challengeDesc;
    private EditText weightTxt;
    private Button closeBtn;

    private View rootView;

    public static InputWeightDialog newInstance() {
        Bundle args = new Bundle();
        InputWeightDialog fragment = new InputWeightDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_input_weight_dialog, container);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        dailyChallenge = (DailyChallenge) getArguments().getSerializable("dailyChallenge");
        challengeTitle = rootView.findViewById(R.id.challengeTitle);
        challengeDesc = rootView.findViewById(R.id.challengeDesc);
        weightTxt = rootView.findViewById(R.id.weightTxt);
        closeBtn = rootView.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserWeightHistory");
                WeightHistory weightHistory = new WeightHistory(
                        GlobalUser.getmUser().getiD(),
                        Integer.parseInt(weightTxt.getText().toString()),
                        Calendar.getInstance().getTime()
                );
                String uniqueId = databaseReference.child(GlobalUser.getmUser().getiD()).push().getKey();
                databaseReference.child(GlobalUser.getmUser().getiD()).child(uniqueId).setValue(weightHistory);

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(GlobalUser.getmUser().getiD());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setWeightInKg(Integer.parseInt(weightTxt.getText().toString()));
                        userRef.setValue(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dismiss();
                getActivity().finish();
            }
        });
    }

}
