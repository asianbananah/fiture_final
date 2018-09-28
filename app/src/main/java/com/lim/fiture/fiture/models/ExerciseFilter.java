package com.lim.fiture.fiture.models;

/**
 * Created by User on 19/09/2018.
 */

public class ExerciseFilter {
    private String name;
    private int resource;

    public ExerciseFilter(String name, int resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
