package com.lim.fiture.fiture.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.LeaderboardAdapter;
import com.lim.fiture.fiture.models.User;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardFragment extends Fragment {

    private View rootView;
    private User mUser;

    private TextView userRank, userName, userPoints;
    private CircleImageView userPic;
    private RelativeLayout relContainer;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LeaderboardAdapter leaderboardAdapter;

    private DatabaseReference userReference;

    private ArrayList<User> allUsers = new ArrayList<>();
    private User[] usersArr;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    public static LeaderboardFragment newInstance(User mUser) {

        Bundle args = new Bundle();
        args.putSerializable("user",mUser);
        LeaderboardFragment fragment = new LeaderboardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getAllUsers();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = (User) getArguments().getSerializable("user");
        findViews();
        getAllUsers();
    }

    private void findViews(){
        userRank = rootView.findViewById(R.id.userRank);
        userPic = rootView.findViewById(R.id.userPic);
        userName = rootView.findViewById(R.id.userName);
        userPoints = rootView.findViewById(R.id.userPoints);

        relContainer = rootView.findViewById(R.id.relContainer);
        progressBar = rootView.findViewById(R.id.progressBar);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getAllUsers(){
        allUsers = new ArrayList<>();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("childrenCountz", dataSnapshot.getChildrenCount() +"");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("childrenCountz", "ni sulod");
                    User user = snapshot.getValue(User.class);
                    Log.d("childrenCountz", user.toString());
                    allUsers.add(user);
                    Log.d("childrenCountz", allUsers.toString());

                    if(allUsers.size() == dataSnapshot.getChildrenCount()){
                        //this is the end of the loop, sort the users here and set leaderboard data
                        Log.d("childrenCountz", "last iteration");
                        usersArr = new User[allUsers.size()];
                        sortUsers();
                        setLeaderboardData();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sortUsers(){
        //first assign the arrayList object to an array index
        //ex. allUsers.get(0) assign it to userArr[0]
        //    or allUsers.get(1) assign it to userArr[1]
        for(int i = 0; i < allUsers.size(); i++){
            usersArr[i] = allUsers.get(i);
        }

        //call java sort function, pass in the newly assigned arrays. In this case, "userArr"
        Arrays.sort(usersArr);

        //reset the arrayList
        allUsers = new ArrayList<>();

        //assign the sorted arrays to each arrayList object
        for(User user : usersArr){
            allUsers.add(user);
        }

        Log.d("childrenCountz", allUsers.toString());
    }

    private void setLeaderboardData(){

        //first, set the user's data; rank, name and points (blue placeholder)
        for(int i = 0 ; i < allUsers.size(); i++){
            if (allUsers.get(i).getiD().equals(mUser.getiD())){
                int rank = i+1;
                userRank.setText(String.valueOf(rank));
                userName.setText(allUsers.get(i).getFirstName() + " " + allUsers.get(i).getLastName());
                userPoints.setText(String.valueOf(allUsers.get(i).getPoints()));
                Glide.with(getActivity()).load(allUsers.get(i).getPhotoUrl()).into(userPic);
            }
        }

        leaderboardAdapter = new LeaderboardAdapter(getActivity(), allUsers);
        recyclerView.setAdapter(leaderboardAdapter);

        progressBar.setVisibility(View.GONE);
        relContainer.setVisibility(View.VISIBLE);
    }
}
