package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.activities.AdminActivity;
import com.lim.fiture.fiture.fragments.AdminExerciseFragment;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.ExerciseFilter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 19/09/2018.
 */

public class AdminExerciseFiltersAdapter extends RecyclerView.Adapter <AdminExerciseFiltersAdapter.ViewHolder> {

    private ArrayList<ExerciseFilter> filters;
    private Context context;
    private AdminExerciseFragment fragment;

    public AdminExerciseFiltersAdapter(ArrayList<ExerciseFilter> filters, Context context, AdminExerciseFragment fragment) {
        this.filters = filters;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_filter_layout_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(filters.get(position).getResource()).into(holder.filterImage);
        holder.filterName.setText(filters.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView filterImage;
        TextView filterName;

        public ViewHolder(View itemView) {
            super(itemView);
            filterImage = itemView.findViewById(R.id.filterImage);
            filterName = itemView.findViewById(R.id.filterName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(filters.get(getAdapterPosition()).getName().equalsIgnoreCase("all")){
                        fragment.loadExercises();
                       Toast.makeText(context, filters.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    }else if (filters.get(getAdapterPosition()).getName().equalsIgnoreCase("lose")){
                        fragment.filterExercises("Lose");
                       Toast.makeText(context, filters.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    }else if (filters.get(getAdapterPosition()).getName().equalsIgnoreCase("maintain")){
                        fragment.filterExercises("Maintain");
                        Toast.makeText(context, filters.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    }else if (filters.get(getAdapterPosition()).getName().equalsIgnoreCase("gain")){
                        fragment.filterExercises("Gain");
                        Toast.makeText(context, filters.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
