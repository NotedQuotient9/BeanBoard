package com.example.beanboard;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {


    ArrayList<Integer> holds;

    boolean completed;
    int numberOfAttempts;
    String createdBy;
    String routeName;
    String grade;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Route(ArrayList<Integer> holds, String createdBy, String routeName, String grade) {
        this.holds = holds;
        this.completed = false;
        this.createdBy = createdBy;
        this.numberOfAttempts = 0;
        this.routeName = routeName;
        this.grade = grade;
    }

    public Route(){

    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public ArrayList<Integer> getHolds() {
        return holds;
    }

    public void setHolds(ArrayList<Integer> holds) {
        this.holds = holds;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(int numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
