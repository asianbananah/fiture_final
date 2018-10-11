package com.lim.fiture.fiture.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.UserMainActivity;
import com.lim.fiture.fiture.adapters.UserRecommendedProgramsAdapter;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//TODO: PROGRAMS
public class UserDailyChallengesFragment extends Fragment {
    private View rootView;
    private Button addProgramButton;
    private RecyclerView recommendedProgramsRecycler, loseRecycler, maintainRecycler, gainRecycler;
    private RecyclerView.LayoutManager recommendedProgramsManager, loseManager, maintainManager, gainManager;
    private UserRecommendedProgramsAdapter loseAdapter, maintainAdapter, gainAdapter, recommendedProgramsAdapter;

    private ArrayList<Program> loseList, maintainList, gainList, recommendedList;


    private DatabaseReference programReference, userProgramsReference;
    private String TAG = "userProgramsFragment";
    private RelativeLayout programRelative;
    private ProgressBar progressBar;
    private User mUser;

    private Map<String,Program> userPrograms = new HashMap<>();
    private boolean userProgramsFound = false;

    public UserDailyChallengesFragment() {
        // Required empty public constructor
    }

    public static UserDailyChallengesFragment newInstance(User user) {
        Bundle args = new Bundle();

        UserDailyChallengesFragment fragment = new UserDailyChallengesFragment();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_daily_challenges, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        programReference = FirebaseDatabase.getInstance().getReference("Program");
        mUser = (User) getArguments().getSerializable("user");
        findViews();
    }

    private void findViews(){
        loseRecycler = rootView.findViewById(R.id.loseRecycler);
        maintainRecycler = rootView.findViewById(R.id.maintainRecycler);
        gainRecycler = rootView.findViewById(R.id.gainRecycler);
        recommendedProgramsRecycler = rootView.findViewById(R.id.recommendedProgramsRecycler);




        loseManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        maintainManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        gainManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recommendedProgramsManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        loseRecycler.setLayoutManager(loseManager);
        maintainRecycler.setLayoutManager(maintainManager);
        gainRecycler.setLayoutManager(gainManager);
        recommendedProgramsRecycler.setLayoutManager(recommendedProgramsManager);

        loseList = new ArrayList<>();
        gainList = new ArrayList<>();
        maintainList = new ArrayList<>();
        recommendedList = new ArrayList<>();

        loadRecommendedProgram( mUser.getFitnessLevel(), mUser.getFitnessGoal(),false);
        loadRecommendedProgram( mUser.getFitnessLevel(), mUser.getFitnessGoal(),true);
        getUserPrograms();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 3s = 3000ms
                if(!userProgramsFound)
                {
                    loadRecommendedProgram( mUser.getFitnessLevel(), mUser.getFitnessGoal(),false);
                    loadRecommendedProgram( mUser.getFitnessLevel(), mUser.getFitnessGoal(),true);
                }
            }
        }, 3000);
    }


    public void getUserPrograms(){
        userProgramsReference = FirebaseDatabase.getInstance().getReference().child("UserPrograms").child(mUser.getiD());
        userProgramsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Program program = snapshot.getValue(Program.class);
                    userPrograms.put(program.getProgramsId(),program);
                    counter++;
                    Log.d("programa", userPrograms.toString());
                    if(counter >= dataSnapshot.getChildrenCount() ){
                        Log.d("programa", "lastIteration");
                        //TODO: this is the last iteration set all recommended and other programs here
                        loadRecommendedProgram( mUser.getFitnessLevel(), mUser.getFitnessGoal(),false);
                        loadRecommendedProgram( mUser.getFitnessLevel(), mUser.getFitnessGoal(),true);
                    }
                }
            userProgramsFound = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadRecommendedProgram(String difficulty, String type, boolean isAllPrograms){
        loseList = new ArrayList<>();
        gainList = new ArrayList<>();
        maintainList = new ArrayList<>();
        recommendedList = new ArrayList<>();
        programReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Program program = dataSnapshot.getValue(Program.class);
                Log.d("Programszzz", program.toString());
                if (userPrograms.containsKey(program.getProgramsId())){
                    Log.d("Program found", program.toString());
                    return;
                }
            //    progressBar.setVisibility(View.GONE);
//                if(programRelative.getVisibility() == View.INVISIBLE)
//                    programRelative.setVisibility(View.VISIBLE);
                if(isAllPrograms){
                    Log.d("Programszzz1223", program.toString());
                    switch (program.getProgramType()){
                        case "Lose":
                            loseList.add(program);
                            loseAdapter = new UserRecommendedProgramsAdapter(getActivity(), loseList, ((UserMainActivity)getActivity()).getCurrentUser());
                            loseRecycler.setAdapter(loseAdapter);
                            break;
                        case "Maintain":
                            maintainList.add(program);
                            maintainAdapter = new UserRecommendedProgramsAdapter(getActivity(), maintainList, ((UserMainActivity)getActivity()).getCurrentUser());
                            maintainRecycler.setAdapter(maintainAdapter);
                            break;
                        case "Gain":
                            gainList.add(program);
                            gainAdapter = new UserRecommendedProgramsAdapter(getActivity(), gainList, ((UserMainActivity)getActivity()).getCurrentUser());
                            gainRecycler.setAdapter(gainAdapter);
                            break;
                    }
                }else{
                    if (program.getProgramDifficulty().equalsIgnoreCase(difficulty)
                            && program.getProgramType().equalsIgnoreCase(type)) {
                        recommendedList.add(program);
                        recommendedProgramsAdapter = new UserRecommendedProgramsAdapter(getActivity(), recommendedList, ((UserMainActivity)getActivity()).getCurrentUser());
                        recommendedProgramsRecycler.setAdapter(recommendedProgramsAdapter);
                    }
                }
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
