package com.treehouse.soccer;

import com.treehouse.soccer.model.League;
import com.treehouse.soccer.model.Player;
import com.treehouse.soccer.model.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by molayodecker on 30/08/2016.
 */
public class Menu {
    private League league;
    private Team team;
    private Map<Team, Player> players;
    BufferedReader tReader;
    Map<String, String> tMenu;
    private Queue<Player> tPlayers;
    private BlockingQueue<String> blockPlayer;
    private final int MAX_NUM = 1;


    public Menu(League league) {
        //Menu for application
        this.league = league;
        tReader = new BufferedReader(new InputStreamReader(System.in));
        tPlayers = new ArrayDeque<>();
        tMenu = new LinkedHashMap<>();
        tMenu.put("Add ", "Add a new team and coach to the League");
        tMenu.put("Remove", "Remove Player from team");
        tMenu.put("transfer", "Add Player to Team");
        tMenu.put("Team", "Print a List of Teams");
        tMenu.put("Player", "Print a List of Players and select player");
        tMenu.put("Quit", "Give up. Exiting Program.....");
    }

    public String promptForAction() throws IOException {
        System.out.printf("The are ( %d ) Team available in the League and you have selected ( %d ) player(s)  %n%n", league.getNumberOfTeams(), tPlayers.size());
        for (Map.Entry<String, String> options : tMenu.entrySet()) {
            System.out.printf("%s - %s %n%n", options.getKey(), options.getValue());
        }
        System.out.print(" What do you want to do : ");
        String choice = tReader.readLine();
        System.out.printf("%n--------------------------- %n%n");
        return choice.trim().toLowerCase();
    }

    public void run() {
        String choice = "";
        do {
            try {
                choice = promptForAction();
                switch (choice) {
                    //Prompts for Name of Team and Coach , and Stores them!
                    case "add":
                        //Add condition
                        Team team = addNewTeam();
                        league.addTeam(team);
                        System.out.printf("%s Added! %n%n", team);
                        break;
                    case "remove":
                        //Display selected players and removes them from the queue
                        Player mplayers = tPlayers.peek();
                        //System.out.println(mplayers); Just to check if mplayer is working
                        System.out.print("Please select (1) to Remove selected Player " +
                                "Or (2) Remove Player from team: ");
                        String remove = tReader.readLine();
                        int index = Integer.parseInt(remove);
                        if (index == 1) {
                            removePlayer();
                            try {
                                System.out.printf("%s has been removed %n%n", mplayers.getFirstName() + " " + mplayers.getLastName());
                            } catch (NullPointerException npe) {
                                System.out.printf("Nothing to Delete. %n%n");
                            }
                        }
                        break;
                    case "player":
                        //Display the list of player for selection into queue.
                        String player = promptForPlayer();
                        Player getPlayer = promptToAddPlayer(player);
                        tPlayers.add(getPlayer);
                        System.out.printf("You chose %s %n", getPlayer.getFirstName() + " " + getPlayer.getLastName());
                        break;
                    case "transfer":
                        /*
                        Problem area. Need Fixtures
                        After add Teams and selecting a player to add, this code should be able to store players in teams
                        ie: use displayTeam as key and blockPlayers as values
                        Bug: Code is only able to store one value in the HashMap. All efforts to store addition values in the
                        HashMap deletes the right value and replaces it with the new value.
                        Use MultiMap from guava also produced the same function.
                        Multimap<String, BlockingQueue<String>> addBlock = ArrayListMultimap.create();
                        */
                        String displayTeam = promptForTeam();
                        blockPlayer = new ArrayBlockingQueue<String>(MAX_NUM);
                        Player toAddPlayer = tPlayers.poll();
                        blockPlayer.add(toAddPlayer.getLastName()+" "+toAddPlayer.getFirstName());
                        Map<String, BlockingQueue<String>> addBlock = new HashMap<>();
                        addBlock.put(displayTeam, blockPlayer);
                        for (BlockingQueue<String> entry : addBlock.values()) {
                            System.out.printf("%s %n%n", entry.peek());
                        }
                        //Fix later when above code works
                        //letsList();
                        break;
                    case "team":
                        //Displays add teams
                        loadTeams();
                        break;
                    case "quit":
                        //Quit program
                        System.out.println("Goodbye...");
                        break;
                    default:
                        //Default output
                        System.out.printf("Unknown choice: \"%s\" Choice, Please Try again! %n%n", choice);
                        break;
                }
            } catch (IOException ioe) {
                System.out.println("The was a problem with the Input.");
            }
        } while (!choice.equals("quit"));
    }

    //Method use to add teams and coach
    public Team addNewTeam() throws IOException {
        System.out.print("Enter the Name of the Team: ");
        String addTeam = tReader.readLine();
        System.out.print("Enter Coach Name: ");
        String coach = tReader.readLine();
        return new Team(addTeam, coach);
    }

    //Helper method to list output
    private int promptForIndex(List<String> options) throws IOException {
        int counter = 1;
        for (String option : options) {
            System.out.printf("%d.) %s %n", counter, option);
            counter++;
        }
        System.out.print(" Please select Player to add to Team: ");
        String OptionAsString = tReader.readLine();
        int choice = Integer.parseInt(OptionAsString);
        return choice - 1;
    }
    /*
    Helper method to list players
    Used this method to display a different output from first helper method
    Fix: Created a DRY Method. Need to harmonize helper method
     */
    private int promptForIndex1(List<String> options) throws IOException {
        int counter = 1;
        for (String option : options) {
            System.out.printf("%d.) %s %n", counter, option);
            counter++;
        }
        System.out.print("Please add Player to add to Team: ");
        System.out.println();
        String OptionAsString = tReader.readLine();
        int choice = Integer.parseInt(OptionAsString);
        return choice - 1;
    }


    private int promptNumberOfTeams(List<String> options) throws IOException {
        int counter = 1;
        for (String option : options) {
            System.out.printf("Available Teams are: %n%n");
            System.out.printf("%d.) %s %n%n", counter, option);
            counter++;
        }
        return counter;
    }

    public String promptForPlayer() throws IOException {
        System.out.printf("Available Players %n%n");
        List<String> players = new ArrayList<>(league.getPlayers());
        int index = promptForIndex(players);
        return players.get(index);
    }

    private Player promptToAddPlayer(String firstName) throws IOException {
        List<Player> player = league.getPlayersByTeam(firstName);
        List<String> result = new ArrayList<>();
        for (Player players : player) {
            result.add(players.getFirstName() + " " + players.getLastName());
        }
        System.out.printf("You selected %s %n", firstName);
        int index = promptForIndex(result);
        return player.get(index);
    }

   //Display the number of teams available
    public String promptForTeam() throws IOException {
        List<String> teams = new ArrayList<>(league.getTeams());
        System.out.println("The available teams are : ");
        try {
            int index = promptForIndex1(teams);
            return teams.get(index);
        } catch (NumberFormatException nfe) {
            System.out.printf("No Team Found.. %n%n");
        }
        return null;
    }

    /*
    This method was created to add players to Team but is not working as expected
     */
    public Map<String, String> addPlayerToTeam(String team, String player) {
        Map<String, String> byLeague = new HashMap<>();
        byLeague.put(team, player);
        for (Map.Entry entry : byLeague.entrySet()) {
            System.out.println("-------------------------------------------");
            System.out.printf("%s has been added to %s ! %n", entry.getValue(), entry.getKey());
            System.out.println("-------------------------------------------");
        }
        return byLeague;
    }

   //Displays the teams in cache in list order
    public String loadTeams() throws IOException {
        List<String> load = new ArrayList<>(league.getTeams());
        try {
            int index = promptNumberOfTeams(load);
            return load.get(index - 2);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.printf("Sorry! ( 0 ) Team(s) available, Please add team(s) from the Main Menu. %n%n");

        }

        return null;
    }

   //Method to remove player from queue
    public Player removePlayer() {
        Player player = null;
        try {
            player = tPlayers.remove();
        } catch (NoSuchElementException nse) {
            System.out.printf("%n Nothing to Delete. ");
        } catch (NullPointerException npe) {
            System.out.printf("Nothing to Delete. ");
        }
        return player;
    }

    /*
     Another method to solve adding players to HashMap
     */
    public Map<String, String> letsList(Map<String, String> str) {
        try {
            for (Map.Entry entry : str.entrySet()) {
                if (entry.getValue() == null && entry.getKey() == null) {
                } else {
                    System.out.printf("%s - %s cdd", entry.getKey(), entry.getValue());
                }
            }
            return str;
        } catch (NullPointerException npe) {
            System.out.printf("No Player Found. %n%n");
        } catch (NumberFormatException nfe) {
            System.out.printf("No Player Found. %n%n");
        }
        return null;
    }

}

