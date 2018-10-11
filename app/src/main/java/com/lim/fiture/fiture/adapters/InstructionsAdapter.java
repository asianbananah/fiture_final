package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lim.fiture.fiture.R;

import java.util.ArrayList;

/**
 * Created by User on 07/10/2018.
 */

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> instructions;

    public InstructionsAdapter(Context context, ArrayList<String> instructions) {
        this.context = context;
        this.instructions = instructions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.instructionsTxt.setText(instructions.get(position));
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView instructionsTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            instructionsTxt = itemView.findViewById(R.id.instructionsTxt);
        }
    }
}
