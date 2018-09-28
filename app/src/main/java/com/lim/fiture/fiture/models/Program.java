package com.lim.fiture.fiture.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 26/09/2018.
 */

public class Program implements Serializable {
    private String programsId;
    private String programsName;
    private String programDifficulty;
    private String programDesc;
    private String programType;
    private String programWeeks;
    private ArrayList<ProgramExercise> programExercises;
    private ArrayList<String> programImages;

    public Program() {
    }

    public Program(String programsId, String programsName, String programDifficulty, String programDesc, String programType, String programWeeks, ArrayList<ProgramExercise> programExercises, ArrayList<String> programImages) {
        this.programsId = programsId;
        this.programsName = programsName;
        this.programDifficulty = programDifficulty;
        this.programDesc = programDesc;
        this.programType = programType;
        this.programWeeks = programWeeks;
        this.programExercises = programExercises;
        this.programImages = programImages;
    }

    public String getProgramsId() {
        return programsId;
    }

    public void setProgramsId(String programsId) {
        this.programsId = programsId;
    }

    public String getProgramsName() {
        return programsName;
    }

    public void setProgramsName(String programsName) {
        this.programsName = programsName;
    }

    public String getProgramDifficulty() {
        return programDifficulty;
    }

    public void setProgramDifficulty(String programDifficulty) {
        this.programDifficulty = programDifficulty;
    }

    public String getProgramDesc() {
        return programDesc;
    }

    public void setProgramDesc(String programDesc) {
        this.programDesc = programDesc;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getProgramWeeks() {
        return programWeeks;
    }

    public void setProgramWeeks(String programWeeks) {
        this.programWeeks = programWeeks;
    }

    public ArrayList<ProgramExercise> getProgramExercises() {
        return programExercises;
    }

    public void setProgramExercises(ArrayList<ProgramExercise> programExercises) {
        this.programExercises = programExercises;
    }

    public ArrayList<String> getProgramImages() {
        return programImages;
    }

    public void setProgramImages(ArrayList<String> programImages) {
        this.programImages = programImages;
    }

    @Override
    public String toString() {
        return "Program{" +
                "programsId='" + programsId + '\'' +
                ", programsName='" + programsName + '\'' +
                ", programDifficulty='" + programDifficulty + '\'' +
                ", programDesc='" + programDesc + '\'' +
                ", programType='" + programType + '\'' +
                ", programWeeks='" + programWeeks + '\'' +
                ", programExercises=" + programExercises.toString() +
                ", programImages=" + programImages.toString() +
                '}';
    }
}