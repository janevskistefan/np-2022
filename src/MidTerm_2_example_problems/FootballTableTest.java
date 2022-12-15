package MidTerm_2_example_problems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

class FootballTable {

    Map<String, Team> competition;

    FootballTable() {
        competition = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {

        if (!competition.containsKey(homeTeam))
            competition.put(homeTeam, new Team(homeTeam));
        Team home_team_var = competition.get(homeTeam);

        if (!competition.containsKey(awayTeam))
            competition.put(awayTeam, new Team(awayTeam));
        Team away_team_var = competition.get(awayTeam);

        away_team_var.incrementNumMatches();
        home_team_var.incrementNumMatches();

        if (homeGoals == awayGoals) {
            away_team_var.incrementTies();
            home_team_var.incrementTies();
        } else if (homeGoals > awayGoals) {
            home_team_var.incrementWins();
            away_team_var.incrementLoses();
        } else {
            away_team_var.incrementWins();
            home_team_var.incrementLoses();
        }

        home_team_var.incrementGoalsEarned(homeGoals);
        home_team_var.incrementGoalsTaken(awayGoals);

        away_team_var.incrementGoalsEarned(awayGoals);
        away_team_var.incrementGoalsTaken(homeGoals);
    }

    public void printTable() {
        Collection<Team> values = competition.values();
        AtomicInteger counter = new AtomicInteger(1);
        values.stream()
                .sorted(Comparator
                        .comparingInt(Team::getPoints)
                        .thenComparingInt(Team::getGoalDiff)
                        .reversed()
                        .thenComparing(Team::getName)
                        ).forEach(elem -> System.out.printf("%2d. %s\n", counter.getAndIncrement(), elem));
    }
}

class Team {
    String name;
    int num_matches;
    int wins;
    int ties;

    int loses;

    int goals_earned;
    int goals_taken;


    public Team(String name) {
        this.name = name;
        this.num_matches = 0;
        this.wins = 0;
        this.ties = 0;
        goals_earned = 0;
        goals_taken = 0;
    }

    public void incrementGoalsEarned(int goals_earned) {
        this.goals_earned += goals_earned;
    }

    public void incrementGoalsTaken(int goals_taken) {
        this.goals_taken += goals_taken;
    }

    public int getGoalDiff() {
        return goals_earned-goals_taken;
    }

    public String getName() {
        return name;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLoses() {
        loses++;
    }

    public void incrementTies() {
        ties++;
    }

    public void incrementNumMatches() {
        num_matches++;
    }


    public int getPoints() {
        return this.wins * 3 + this.ties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team otherTeam = (Team) o;
        return this.name.equals(otherTeam.name);
    }

    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, num_matches, wins, ties, loses, this.getPoints());
    }

}
