package com.lim.fiture.fiture.models;

/**
 * Created by User on 26/09/2018.
 */

public class ProgramExercise {
    private String programId;
    private String exerciseId;
    private String exerciseName;
    private int sets;
    private int reps;
    private int rest;
    private String day; //ex. Thursday
    private int week; // ex. Week 1/ Week 2

    public ProgramExercise() {
    }

    public ProgramExercise(String programId, String exerciseId, String exerciseName, int sets, int reps, int rest, String day, int week) {
        this.programId = programId;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.sets = sets;
        this.reps = reps;
        this.rest = rest;
        this.day = day;
        this.week = week;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
