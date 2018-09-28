package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AddExerciseStepTwo;

import java.util.ArrayList;

/**
 * Created by User on 09/09/2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> steps;
    public ArrayList<ViewHolder> viewHolders;

    public StepAdapter(Context context, ArrayList<String> steps){
        this.context = context;
        this.steps = steps;
        viewHolders = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_layout_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.stepTxt.setText(steps.get(position));

       if(position == 0 || position == getItemCount()){
           holder.stepTxt.setContentDescription("add");
           holder.addStep.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   ((AddExerciseStepTwo)context).addStep("");
               }
           });
       }else{
           holder.stepTxt.setContentDescription("delete");
           holder.addStep.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   ((AddExerciseStepTwo)context).removeStep(position);

               }
           });
       }

       if(holder.stepTxt.getContentDescription().equals("delete")){
           holder.addStep.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_remove_white_24dp));
       }else{
           holder.addStep.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_white_24dp));
       }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton addStep;
        EditText stepTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            addStep = itemView.findViewById(R.id.addStep);
            stepTxt = itemView.findViewById(R.id.stepTxt);
            stepTxt.setTextIsSelectable(true);
            stepTxt.setFocusableInTouchMode(true);

            addStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "heyy", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public String getStepTxt(){
            return stepTxt.getText().toString();
        }
    }
}
