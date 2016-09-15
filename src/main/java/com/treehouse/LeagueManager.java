package com.treehouse;

import com.treehouse.soccer.Menu;
import com.treehouse.soccer.model.League;
import com.treehouse.soccer.model.Player;
import com.treehouse.soccer.model.Players;

import java.io.IOException;

public class LeagueManager {

    public static void main(String[] args) throws IOException {
        Player[] players = Players.load();
        System.out.printf("There are currently %d registered players.%n", players.length);
        League mLeague = new League();
        Menu mMenu = new Menu(mLeague);
        mMenu.run();


    }

}
