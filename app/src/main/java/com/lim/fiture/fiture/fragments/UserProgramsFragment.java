package com.lim.fiture.fiture.fragments;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.UserProgramsAdapter;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.util.GlobalUser;

import java.util.ArrayList;

public class UserProgramsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UserProgramsAdapter adapter;
    private User mUser;
    private DatabaseReference userProgramsReference;
    private ArrayList<Program> userPrograms = new ArrayList<>();
    private View rootView;

    public static UserProgramsFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable("user",user);
        UserProgramsFragment fragment = new UserProgramsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.activity_user_program, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = (User) getArguments().getSerializable("user");
        findViews();
        getUserPrograms();
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_program);
//
//        getDataFromPreviousActivity();
//        findViews();
//        getUserPrograms();
//    }

//    private void getDataFromPreviousActivity(){
//        mUser = (User) getIntent().getSerializableExtra("user");
//    }

    private void findViews(){
//        ActionBar toolBar = getSupportActionBar();
//        toolBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2980b9")));
//        toolBar.setTitle("My Programs");
//        toolBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getUserPrograms(){
        userPrograms = new ArrayList<>();
        userProgramsReference = FirebaseDatabase.getInstance().getReference().child("UserPrograms").child(mUser.getiD());
        userProgramsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Program program = dataSnapshot.getValue(Program.class);
                DatabaseReference completedProgramsRef = FirebaseDatabase.getInstance().getReference()
                        .child("UserCompletedPrograms")
                        .child(GlobalUser.getmUser().getiD())
                        .child(program.getProgramsId());
                completedProgramsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            userPrograms.add(program);
                            adapter = new UserProgramsAdapter(getActivity(), userPrograms);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
