package com.lim.fiture.fiture.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;

import java.util.ArrayList;

/**
 * Created by User on 26/09/2018.
 */

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder>{
    private Context context;
    public ArrayList<Program> program;

    public ProgramAdapter(Context context, ArrayList<Program> program){
        this.context = context;
        this.program = program;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
