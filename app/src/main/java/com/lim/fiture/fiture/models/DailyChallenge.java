package com.lim.fiture.fiture.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 08/10/2018.
 */

public class DailyChallenge implements Serializable {
    private String challengeId;
    private String challengeName;
    private String challengeDesc;
    private ArrayList<String> challengeImages;
    private int challengeGoalNum;
    private int challengePoints;
    private String challengeLevel;
    private String challengeFitnessGoal; //lose,gain,maintain
    private String trackVal; // N/A, steps, distance
    private boolean challengeCompleted;

    public DailyChallenge() {
    }

    public DailyChallenge(String challengeId, String challengeName, String challengeDesc, ArrayList<String> challengeImages, int challengeGoalNum, int challengePoints, String challengeLevel, String challengeFitnessGoal, boolean usePedometer, String trackVal, boolean challengeCompleted) {
        this.challengeId = challengeId;
        this.challengeName = challengeName;
        this.challengeDesc = challengeDesc;
        this.challengeImages = challengeImages;
        this.challengeGoalNum = challengeGoalNum;
        this.challengePoints = challengePoints;
        this.challengeLevel = challengeLevel;
        this.challengeFitnessGoal = challengeFitnessGoal;
        this.trackVal = trackVal;
        this.challengeCompleted = challengeCompleted;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getChallengeDesc() {
        return challengeDesc;
    }

    public void setChallengeDesc(String challengeDesc) {
        this.challengeDesc = challengeDesc;
    }

    public ArrayList<String> getChallengeImages() {
        return challengeImages;
    }

    public void setChallengeImages(ArrayList<String> challengeImages) {
        this.challengeImages = challengeImages;
    }

    public int getChallengeGoalNum() {
        return challengeGoalNum;
    }

    public void setChallengeGoalNum(int challengeGoalNum) {
        this.challengeGoalNum = challengeGoalNum;
    }

    public int getChallengePoints() {
        return challengePoints;
    }

    public void setChallengePoints(int challengePoints) {
        this.challengePoints = challengePoints;
    }

    public String getChallengeLevel() {
        return challengeLevel;
    }

    public void setChallengeLevel(String challengeLevel) {
        this.challengeLevel = challengeLevel;
    }

    public String getChallengeFitnessGoal() {
        return challengeFitnessGoal;
    }

    public void setChallengeFitnessGoal(String challengeFitnessGoal) {
        this.challengeFitnessGoal = challengeFitnessGoal;
    }

    public String getTrackVal() {
        return trackVal;
    }

    public void setTrackVal(String trackVal) {
        this.trackVal = trackVal;
    }
}
