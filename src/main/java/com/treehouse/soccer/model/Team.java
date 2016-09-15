package com.treehouse.soccer.model;


/**
 * Created by molayodecker on 30/08/2016.
 */
public class Team {
    private String teamName;
    private String coachName;

    public Team(String teamName, String coachName) {
        this.teamName = teamName;
        this.coachName = coachName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCoachName() {
        return coachName;
    }


    @Override
    public String toString() {
        return String.format("Team %s and Coach %s ", teamName, coachName);
    }
}
