package com.lim.fiture.fiture.models;


import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by User on 27/08/2018.
 */

public class Exercise implements Serializable {
    private String exerciseId;
    private String exerciseName;
    private String mainMuscleGroup;
    private String otherMuscleGroup;
    private String type;
    private String equipment;
    private String difficulty;
    private ArrayList<String> steps;
    private ArrayList<String> exerciseImages;

    public Exercise() {
    }

    public Exercise(String exerciseId, String exerciseName, String mainMuscleGroup, String otherMuscleGroup, String type, String equipment, String difficulty) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.mainMuscleGroup = mainMuscleGroup;
        this.otherMuscleGroup = otherMuscleGroup;
        this.type = type;
        this.equipment = equipment;
        this.difficulty = difficulty;
    }

    public Exercise(String exerciseId, String exerciseName, String mainMuscleGroup, String otherMuscleGroup, String type, String equipment, String difficulty, ArrayList<String> steps) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.mainMuscleGroup = mainMuscleGroup;
        this.otherMuscleGroup = otherMuscleGroup;
        this.type = type;
        this.equipment = equipment;
        this.difficulty = difficulty;
        this.steps = steps;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public String getExerciseId(){
        return exerciseId;
    }
    public void setExerciseId(String exerciseId){
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMainMuscleGroup() {
        return mainMuscleGroup;
    }

    public void setMainMuscleGroup(String mainMuscleGroup) {
        this.mainMuscleGroup = mainMuscleGroup;
    }

    public String getOtherMuscleGroup() {
        return otherMuscleGroup;
    }

    public void setOtherMuscleGroup(String otherMuscleGroup) {
        this.otherMuscleGroup = otherMuscleGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

//    @Override
//    public String toString() {
//        return "Exercise{" +
//                "exerciseId='" + exerciseId + '\'' +
//                ", exerciseName='" + exerciseName + '\'' +
//                ", mainMuscleGroup='" + mainMuscleGroup + '\'' +
//                ", otherMuscleGroup='" + otherMuscleGroup + '\'' +
//                ", type='" + type + '\'' +
//                ", equipment='" + equipment + '\'' +
//                ", difficulty='" + difficulty + '\'' +
//                ", steps=" + steps +
//                ", exerciseImages=" + exerciseImages +
//                '}';
//    }
//    @Override
//    public String toString(){
//        return
//                "id: " + this.getExerciseId() + "\n" +
//                        "exerciseName: " + this.getExerciseName() + "\n" +
//                        "mainMuscleGroup: " + this.getMainMuscleGroup() + "\n" +
//                        "otherMuscleGroup: " + this.getOtherMuscleGroup() + "\n" +
//                        "type: " + this.getType() + "\n" +
//                        "equipment: " + this.getEquipment() + "\n" +
//                        "difficulty: " + this.getDifficulty() + "\n" +
//                        "exerciseSteps: " + this.getSteps().toString() + "\n" +
//                        "exerciseImages: " + this.getExerciseImages().toString();
//    }

     public ArrayList<String> getExerciseImages() {
        return exerciseImages;
    }

    public void setExerciseImages(ArrayList<String> exerciseImages) {
        this.exerciseImages = exerciseImages;
    }
}

