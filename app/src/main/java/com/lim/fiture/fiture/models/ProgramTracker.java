package com.lim.fiture.fiture.models;

import java.io.Serializable;

/**
 * Created by User on 15/10/2018.
 */

public class ProgramTracker implements Serializable {
    private String programExerciseId;
    private int programExerciseWeek;
    private boolean programExerciseFinished;

    public ProgramTracker() {
    }

    public ProgramTracker(String programExerciseId, int programExerciseWeek, boolean programExerciseFinished) {
        this.programExerciseId = programExerciseId;
        this.programExerciseWeek = programExerciseWeek;
        this.programExerciseFinished = programExerciseFinished;
    }

    public String getProgramExerciseId() {
        return programExerciseId;
    }

    public void setProgramExerciseId(String programExerciseId) {
        this.programExerciseId = programExerciseId;
    }

    public int getProgramExerciseWeek() {
        return programExerciseWeek;
    }

    public void setProgramExerciseWeek(int programExerciseWeek) {
        this.programExerciseWeek = programExerciseWeek;
    }

    public boolean isProgramExerciseFinished() {
        return programExerciseFinished;
    }

    public void setProgramExerciseFinished(boolean programExerciseFinished) {
        this.programExerciseFinished = programExerciseFinished;
    }
}
