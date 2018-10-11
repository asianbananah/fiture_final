package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.customtoast.chen.customtoast.CustomToast;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.DayExerciseActivity;
import com.lim.fiture.fiture.activities.StartProgramActivity;
import com.lim.fiture.fiture.models.ProgramExercise;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import java.util.ArrayList;

/**
 * Created by User on 05/10/2018.
 */

public class TestStackAdapter extends StackAdapter<Integer> {

    private CustomToast customToast;


    public TestStackAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof ColorItemLargeHeaderViewHolder) {
            ColorItemLargeHeaderViewHolder h = (ColorItemLargeHeaderViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof ColorItemWithNoHeaderViewHolder) {
            ColorItemWithNoHeaderViewHolder h = (ColorItemWithNoHeaderViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof ColorItemViewHolder) {
            ColorItemViewHolder h = (ColorItemViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case R.layout.list_card_item_larger_header:
                view = getLayoutInflater().inflate(R.layout.list_card_item_larger_header, parent, false);
                return new ColorItemLargeHeaderViewHolder(view);
            case R.layout.list_card_item_with_no_header:
                view = getLayoutInflater().inflate(R.layout.list_card_item_with_no_header, parent, false);
                return new ColorItemWithNoHeaderViewHolder(view);
            default:
                view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
                return new ColorItemViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 6) {//TODO TEST LARGER ITEM
            //TODO: butngi ni nga layout ug button sa iyang ubos nga view shit; and tangtangon ang scrollview
            return R.layout.list_card_item_larger_header;
        } else if (position == 10) {
            return R.layout.list_card_item_with_no_header;
        }else {
            //TODO: butngi ni nga layout ug button sa iyang ubos nga view shit
            return R.layout.list_card_item;
        }

    }


    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle, quoteTxt, text_view;
        Button seeExercisesBtn, seeExercisesBtnLargeHeader;


        public ColorItemViewHolder(View view) {
            super(view);

            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            quoteTxt = view.findViewById(R.id.quoteTxt);

            Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/cookie.ttf");
            quoteTxt.setTypeface(custom_font);

            seeExercisesBtn = view.findViewById(R.id.seeExercisesBtn);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer data, int position) {

            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            switch (position){
                case 0:
                    mTextTitle.setText("Monday");
                    quoteTxt.setText("\"If you can make it that far, what's stopping you from one more mile or one more set of reps?\"");

                    break;
                case 1:
                    mTextTitle.setText("Tuesday");
                    quoteTxt.setText("\"If you can conquer that endless set of burpees, what can't you do?\"");
                    break;
                case 2:
                    mTextTitle.setText("Wednesday");
                    quoteTxt.setText("\"No matter how slow you go, you are still lapping everybody on the couch.\"");
                    break;
                case 3:
                    mTextTitle.setText("Thursday");
                    quoteTxt.setText("\"The difference between the impossible and the possible lies in a person’s determination\"");
                    break;
                case 4:
                    mTextTitle.setText("Friday");
                    quoteTxt.setText("\"The last three or four reps is what makes the muscle grow. This area of pain divides the champion from someone else who is not a champion. That’s what most people lack, having the guts to go on and just say they’ll go through the pain no matter what happens.\"");
                    break;
                case 5:
                    mTextTitle.setText("Saturday");
                    quoteTxt.setText("\"No matter how many mistakes you make or how slow you progress, you are still way ahead of everyone who isn’t trying.\"");
                    break;
                case 6:
                    mTextTitle.setText("Sunday");
                    break;
            }
            seeExercisesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((StartProgramActivity)getContext()).setWeekExercisesData(position);
                    ArrayList<ProgramExercise> programExercises = ((StartProgramActivity)getContext()).getDayOfWeekExercises();
                    if(programExercises.size() > 0) { //TODO: meaning naay sulod
                        getContext().startActivity(new Intent(getContext(), DayExerciseActivity.class).putExtra("programExercises", programExercises));
                    }else{
                        CustomToast customToast = new CustomToast(getContext());
                        customToast.setTextColor(getContext().getResources().getColor(R.color.white));
                        customToast.setBackground(getContext().getResources().getColor(R.color.color_2));

                        customToast.showErrorToast("No exercises for this day!");
                       // Toast.makeText(getContext(), "No exercises for this day :(", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

    static class ColorItemWithNoHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        TextView mTextTitle;



        public ColorItemWithNoHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
        }

        @Override
        public void onItemExpand(boolean b) {
        }


        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(String.valueOf(position));
        }

    }

    static class ColorItemLargeHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle,quoteTxt;
        Button seeExercisesBtn;

        public ColorItemLargeHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
            quoteTxt = view.findViewById(R.id.quoteTxt);

            Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/cookie.ttf");
            quoteTxt.setTypeface(custom_font);
            CustomToast customToast = new CustomToast(getContext());

            quoteTxt.setText("\"Just believe in yourself. Even if you don’t pretend that you do and, and some point, you will.\"");
            seeExercisesBtn = view.findViewById(R.id.seeExercisesBtnLargeHeader);

        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            switch (position) {
                case 0:
                    mTextTitle.setText("Monday");
                    break;
                case 1:
                    mTextTitle.setText("Tuesday");
                    break;
                case 2:
                    mTextTitle.setText("Wednesday");
                    break;
                case 3:
                    mTextTitle.setText("Thursday");
                    break;
                case 4:
                    mTextTitle.setText("Friday");
                    break;
                case 5:
                    mTextTitle.setText("Saturday");
                    break;
                case 6:
                    mTextTitle.setText("Sunday");
                    break;
            }

            seeExercisesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            itemView.findViewById(R.id.quoteTxt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CardStackView)itemView.getParent()).performItemClick(ColorItemLargeHeaderViewHolder.this);
                    ((StartProgramActivity)getContext()).setWeekExercisesData(position);
                    //TODO:
                }
            });
        }

    }

}
