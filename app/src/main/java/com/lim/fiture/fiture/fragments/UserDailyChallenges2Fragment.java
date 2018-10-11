package com.lim.fiture.fiture.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.adapters.UserDailyChallengeAdapter;
import com.lim.fiture.fiture.adapters.UserProgramsAdapter;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.Program;
import com.lim.fiture.fiture.models.User;
import com.lim.fiture.fiture.util.CarouselEffectTransformer;

import java.util.ArrayList;

/**
 * Created by User on 09/10/2018.
 */

public class UserDailyChallenges2Fragment extends Fragment {

    private User mUser;
    private DatabaseReference dailyChallengeReference;
    private ArrayList<DailyChallenge> dailyChallenges = new ArrayList<>();
    private View rootView;

    private ViewPager viewpagerTop, viewPagerBackground;
    public static final int ADAPTER_TYPE_TOP = 1;
    public static final int ADAPTER_TYPE_BOTTOM = 2;
    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_TRANSITION_IMAGE = "image";

//    private int[] listItems = {R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4,
//            R.mipmap.img5, R.mipmap.img6, R.mipmap.img7, R.mipmap.img8, R.mipmap.img9, R.mipmap.img10};

    public static UserDailyChallenges2Fragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putSerializable("user", user);
        UserDailyChallenges2Fragment fragment = new UserDailyChallenges2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_daily_challenges_two, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = (User) getArguments().getSerializable("user");

        init();
        getUserDailyChallenges();
//        setupViewPager();

    }



    private void getUserDailyChallenges() {
        dailyChallengeReference = FirebaseDatabase.getInstance().getReference().child("DailyChallenge");
        dailyChallengeReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DailyChallenge dailyChallenge = dataSnapshot.getValue(DailyChallenge.class);
                if(dailyChallenge.getChallengeLevel().equals(mUser.getFitnessLevel())){
                    dailyChallenges.add(dailyChallenge);
                    setupViewPager();
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

    /**
     * Initialize all required variables
     */
    private void init() {
        viewpagerTop = (ViewPager) rootView.findViewById(R.id.viewpagerTop);
        viewPagerBackground = (ViewPager) rootView.findViewById(R.id.viewPagerbackground);

        viewpagerTop.setClipChildren(false);
        viewpagerTop.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewpagerTop.setOffscreenPageLimit(3);
        viewpagerTop.setPageTransformer(false, new CarouselEffectTransformer(getActivity())); // Set transformer
    }

    /**
     * Setup viewpager and it's events
     */
    private void setupViewPager() {
        // Set Top ViewPager Adapter
        UserDailyChallengeAdapter adapter = new UserDailyChallengeAdapter(getActivity(), dailyChallenges, ADAPTER_TYPE_TOP);
        viewpagerTop.setAdapter(adapter);

        // Set Background ViewPager Adapter
        UserDailyChallengeAdapter adapterBackground = new UserDailyChallengeAdapter(getActivity(), dailyChallenges, ADAPTER_TYPE_BOTTOM);
        viewPagerBackground.setAdapter(adapterBackground);


        viewpagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int index = 0;

            @Override
            public void onPageSelected(int position) {
                index = position;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int width = viewPagerBackground.getWidth();
                viewPagerBackground.scrollTo((int) (width * position + width * positionOffset), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    viewPagerBackground.setCurrentItem(index);
                }

            }
        });
    }

    /**
     * Handle all click event of activity
     */
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.linMain:
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                break;
//                if (view.getTag() != null) {
//                    int poisition = Integer.parseInt(view.getTag().toString());
//                    //Toast.makeText(getApplicationContext(), "Poistion: " + poisition, Toast.LENGTH_LONG).show();
//
//                    Intent intent=new Intent(this,FullScreenActivity.class);
//                    intent.putExtra(EXTRA_IMAGE,listItems[poisition]);
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view.findViewById(R.id.imageCover), EXTRA_TRANSITION_IMAGE);
//                    ActivityCompat.startActivity(this, intent, options.toBundle());
        }
    }
}
