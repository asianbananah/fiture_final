package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.DailyChallenge;
import com.lim.fiture.fiture.models.User;

import java.util.ArrayList;

/**
 * Created by User on 14/10/2018.
 */

public class UserHistoryDailyChallenge extends RecyclerView.Adapter<UserHistoryDailyChallenge.ViewHolder>{

    private Context context;
    private ArrayList<DailyChallenge> dailyChallenges;

    public UserHistoryDailyChallenge(Context context, ArrayList<DailyChallenge> dailyChallenges) {
        this.context = context;
        this.dailyChallenges = dailyChallenges;
    }


    @Override
    public UserHistoryDailyChallenge.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_history_layout_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserHistoryDailyChallenge.ViewHolder holder, int position) {
        holder.completedName.setText(dailyChallenges.get(position).getChallengeName());
        holder.completedDesc.setText(dailyChallenges.get(position).getChallengeDesc());
        String points = holder.pointsTxt.getText().toString().replace("{0}", String.valueOf(dailyChallenges.get(position).getChallengePoints()));
        holder.pointsTxt.setText(points);
    }

    @Override
    public int getItemCount() {
      return dailyChallenges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView completedName, completedDesc, pointsTxt;

        public ViewHolder(View itemView) {
            super(itemView);

            completedName = itemView.findViewById(R.id.completedName);
            completedDesc = itemView.findViewById(R.id.completedDesc);
            pointsTxt = itemView.findViewById(R.id.pointsTxt);

        }
    }
}
