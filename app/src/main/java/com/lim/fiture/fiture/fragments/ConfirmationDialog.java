package com.lim.fiture.fiture.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lim.fiture.fiture.activities.AdminActivity;
import com.lim.fiture.fiture.activities.ProgramActivity;
import com.lim.fiture.fiture.models.Exercise;
import com.lim.fiture.fiture.models.Program;
import com.wdullaer.materialdatetimepicker.date.DateRangeLimiter;

/**
 * Created by User on 19/09/2018.
 */

public class ConfirmationDialog extends DialogFragment {


    private DatabaseReference exerciseReference;



    public ConfirmationDialog() {

    }

    public static ConfirmationDialog newInstance(String title, String message, Exercise exercise, int position) {
        ConfirmationDialog fragment = new ConfirmationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("message", message);
        args.putSerializable("exercise", exercise);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    public static ConfirmationDialog newInstance(String title, String message, Program program, int position) {
        ConfirmationDialog fragment = new ConfirmationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("message", message);
        args.putSerializable("programa", program);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        exerciseReference = FirebaseDatabase.getInstance().getReference("Exercises");



        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        Exercise exercise = (Exercise) getArguments().getSerializable("exercise");

        int position = getArguments().getInt("position");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exerciseReference.child(exercise.getExerciseId()).removeValue();

                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                ((AdminActivity) getActivity()).updateExerciseListAfterDelete(position);


            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        return alertDialogBuilder.create();
    }


}
