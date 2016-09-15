package com.treehouse.soccer.model;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by molayodecker on 31/08/2016.
 */
public class League {
    private List<Team> addNewTeam;
    private List<Player> addNewPlayer;
    private BlockingQueue<Team> team;
    private Player[] play;
    private final int MAX_NUM = 11;

    public League() {
        addNewTeam = new ArrayList<>();
        addNewPlayer = new ArrayList<>();
        team = new ArrayBlockingQueue<Team>(MAX_NUM);
    }

    public void addTeam(Team team) {
        addNewTeam.add(team);
    }

    public List<Team> getTeamName() {
        return addNewTeam;
    }

    public int getNumberOfTeams() {
        return addNewTeam.size();
    }

    public Player[] loadPlayers() {
        return Players.load();
    }


    public Map<String, List<Player>> byPlayers() {
        Map<String, List<Player>> byPlayer = new HashMap<>();
        for (Player player : loadPlayers()) {
            List<Player> teamPlayers = byPlayer.get(player.getFirstName() + " " + player.getLastName());
            String IsTrue = (player.isPreviousExperience() == true) ? " Experienced " : " InExperienced ";
            if (teamPlayers == null) {
                teamPlayers = new ArrayList<>();
                byPlayer.put("FullName: " + player.getLastName() + " " + player.getFirstName() + " | Height : " + player.getHeightInInches() + " | Level of Experience? " + IsTrue, teamPlayers);
            }
            teamPlayers.add(player);
        }
        return byPlayer;
    }

    public Map<String, List<Team>> byTeams() {
        Map<String, List<Team>> byTeam = new HashMap<>();
        for (Team team : getTeamName()) {
            List<Team> teamLeague = byTeam.get(team.getTeamName());
            if (teamLeague == null) {
                teamLeague = new ArrayList<>();
                byTeam.put(team.getTeamName(), teamLeague);
            }
            teamLeague.add(team);
        }
        return byTeam;
    }

    public Set<String> getTeams() {
        return byTeams().keySet();
    }

    public Set<String> getPlayers() {
        return byPlayers().keySet();
    }

    public List<Player> getPlayersByTeam(String playerName) {
        return byPlayers().get(playerName);
    }
}
