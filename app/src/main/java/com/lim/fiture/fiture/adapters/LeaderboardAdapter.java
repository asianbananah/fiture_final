package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 14/10/2018.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> users;

    public LeaderboardAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_layout_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int rank = position+1;
        holder.userRank.setText(String.valueOf(rank));
        holder.userName.setText(users.get(position).getFirstName() + " " + users.get(position).getLastName());
        holder.userPoints.setText(String.valueOf(users.get(position).getPoints()));
        Glide.with(context).load(users.get(position).getPhotoUrl()).into(holder.userPic);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userRank, userName, userPoints;
        CircleImageView userPic;

        public ViewHolder(View itemView) {
            super(itemView);
            userRank = itemView.findViewById(R.id.userRank);
            userName = itemView.findViewById(R.id.userName);
            userPoints = itemView.findViewById(R.id.userPoints);
            userPic = itemView.findViewById(R.id.userPic);
        }
    }
}
