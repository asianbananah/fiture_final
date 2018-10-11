package com.lim.fiture.fiture.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.DailyChallengeAdapter;
import com.lim.fiture.fiture.models.DailyChallenge;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminDailyChallengeFragment extends Fragment {

    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DailyChallengeAdapter dailyChallengeAdapter;

    private DatabaseReference challengeReference;
    private ArrayList<DailyChallenge> dailyChallenges = new ArrayList<>();
    private int[] colors = {
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_16,
            R.color.color_17,
            R.color.color_18,
            R.color.color_19,
            R.color.color_20,
            R.color.color_21,
            R.color.color_22,
            R.color.color_23,
            R.color.color_24,
            R.color.color_25,
            R.color.color_26
    };

    public AdminDailyChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin_daily_challenge, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        challengeReference = FirebaseDatabase.getInstance().getReference().child("DailyChallenge");
        findViews();
        getDailyChallenges();
    }

    public void findViews() {
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDailyChallenges();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView = rootView.findViewById(R.id.dailyChallengeList);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getDailyChallenges() {
        dailyChallenges = new ArrayList<>();
        challengeReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DailyChallenge dailyChallenge = dataSnapshot.getValue(DailyChallenge.class);
                dailyChallenges.add(dailyChallenge);
                dailyChallengeAdapter = new DailyChallengeAdapter(getActivity(), dailyChallenges, colors);
                recyclerView.setAdapter(dailyChallengeAdapter);
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


}
