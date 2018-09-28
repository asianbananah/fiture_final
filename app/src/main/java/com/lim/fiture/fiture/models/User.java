package com.lim.fiture.fiture.models;

import java.io.Serializable;

/**
 * Created by User on 24/07/2018.
 */

public class User implements Serializable {
    private String iD;
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String gender;
    private float bmi;
    private int weightInKg;
    private int heightInCm;
    private String fitnessLevel;
    private String fitnessGoal;
    private String photoUrl;

    public User() {
    }

    public User(String iD, String firstName, String lastName, String email, String dateOfBirth, String gender, float bmi, int weightInKg, int heightInCm, String fitnessLevel, String fitnessGoal, String photoUrl) {
        this.iD = iD;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bmi = bmi;
        this.weightInKg = weightInKg;
        this.heightInCm = heightInCm;
        this.fitnessLevel = fitnessLevel;
        this.fitnessGoal = fitnessGoal;
        this.photoUrl = photoUrl;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public int getWeightInKg() {
        return weightInKg;
    }

    public void setWeightInKg(int weightInKg) {
        this.weightInKg = weightInKg;
    }

    public int getHeightInCm() {
        return heightInCm;
    }

    public void setHeightInCm(int heightInCm) {
        this.heightInCm = heightInCm;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String toString() {
        return
                "id: " + this.getiD() + "\n" +
                        "firstName: " + this.firstName + "\n" +
                        "lastName: " + this.lastName + "\n" +
                        "email: " + this.getEmail() + "\n" +
                        "dateOfBirth: " + this.getDateOfBirth() + "\n" +
                        "gender: " + this.getGender() + "\n" +
                        "bmi: " + this.getBmi() + "\n" +
                        "weight: " + this.getWeightInKg() + "\n" +
                        "height: " + this.getHeightInCm() + "\n" +
                        "fitnessLevel: " + this.getFitnessLevel() + "\n" +
                        "fitnessGoal: " + this.getFitnessGoal() + "\n" +
                        "photoUrl: " + this.getPhotoUrl() + "\n";

    }
}
