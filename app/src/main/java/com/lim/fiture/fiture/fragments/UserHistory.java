package com.lim.fiture.fiture.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.UserHistoryDailyChallenge;
//import com.lim.fiture.fiture.adapters.UserHistoryProgram;
import com.lim.fiture.fiture.adapters.UserHistoryProgram;
import com.lim.fiture.fiture.adapters.UserProgramsAdapter;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.util.GlobalUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHistory extends Fragment implements TabLayout.OnTabSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TabLayout historyTab;

    private View rootView;


    private ArrayList<Program> userPrograms = new ArrayList<>();
    private ArrayList<DailyChallenge> dailyChallenges = new ArrayList<>();


    private User mUser;
    private UserHistoryDailyChallenge adapter;
    private UserHistoryProgram programAdapter;



    private DatabaseReference userDailyChallengeRef, userProgramsReference;

    public UserHistory() {
        // Required empty public constructor
    }

//    public static UserHistory newInstance(User user) {
//
//        Bundle args = new Bundle();
//        args.putSerializable("user",user);
//        UserHistory fragment = new UserHistory();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_history, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        getUserCompletedDailyChallenge();
    }


    private void findViews() {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        historyTab = rootView.findViewById(R.id.historyTab);
        historyTab.addOnTabSelectedListener(this);
        historyTab.setSelectedTabIndicatorColor(Color.parseColor("#2980b9"));


    }

    private void getUserCompletedDailyChallenge(){
        dailyChallenges = new ArrayList<>();
        userDailyChallengeRef = FirebaseDatabase.getInstance().getReference().child("UserCompletedChallenges").child(GlobalUser.getmUser().getiD());
        userDailyChallengeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DailyChallenge dailyChallenge = dataSnapshot.getValue(DailyChallenge.class);
//                userDailyChallengeRef = FirebaseDatabase.getInstance().getReference()
//                        .child("UserCompletedChallenges")
//                        .child(GlobalUser.getmUser().getiD())
//                        .child(dailyChallenge.getChallengeId());

                dailyChallenges.add(dailyChallenge);
                adapter = new UserHistoryDailyChallenge(getActivity(), dailyChallenges);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserCompletedPrograms(){
        userPrograms = new ArrayList<>();
        userProgramsReference = FirebaseDatabase.getInstance().getReference().child("UserCompletedPrograms").child(GlobalUser.getmUser().getiD());
        userProgramsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Program program = dataSnapshot.getValue(Program.class);

                userPrograms.add(program);
                programAdapter = new UserHistoryProgram(getActivity(), userPrograms);
                recyclerView.setAdapter(programAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                getUserCompletedDailyChallenge();
                break;
            case 1:
                getUserCompletedPrograms();
                break;

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
