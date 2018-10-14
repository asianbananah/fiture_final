package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.Program;

import java.util.ArrayList;

/**
 * Created by User on 14/10/2018.
 */

public class UserHistoryProgram extends RecyclerView.Adapter<UserHistoryProgram.ViewHolder> {

    private Context context;
    private ArrayList<Program> programs;

    public UserHistoryProgram(Context context, ArrayList<Program> programs) {
        this.context = context;
        this.programs = programs;
        Log.d("challengeHistory", programs.size() + "");
    }

    @Override
    public UserHistoryProgram.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_history_layout_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserHistoryProgram.ViewHolder holder, int position) {
        holder.completedName.setText(programs.get(position).getProgramsName());
        holder.completedDesc.setText(programs.get(position).getProgramDesc());
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView completedName, completedDesc;
        public ViewHolder(View itemView) {
            super(itemView);

            completedName = itemView.findViewById(R.id.completedName);
            completedDesc = itemView.findViewById(R.id.completedDesc);

        }
    }
}
