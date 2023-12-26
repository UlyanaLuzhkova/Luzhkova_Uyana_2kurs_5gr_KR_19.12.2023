import java.io.*;
import java.util.*;

class Team {
    String name;
    int points;
    int wins;
    int draws;
    int losses;
    int goalsScored;
    int goalsConceded;

    public Team(String name) {
        this.name = name;
        this.points = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
    }

    public void addMatchResult(int goalsScored, int goalsConceded) {
        this.goalsScored += goalsScored;
        this.goalsConceded += goalsConceded;
        if (goalsScored > goalsConceded) {
            this.points += 3;
            this.wins++;
        } else if (goalsScored == goalsConceded) {
            this.points += 1;
            this.draws++;
        } else {
            this.losses++;
        }
    }

    public String toString() {
        return name + " " + points + " " + wins + " " + draws + " " + losses + " " + goalsScored + " " + goalsConceded;
    }
}

class Match {
    Team team1;
    Team team2;
    int goalsTeam1;
    int goalsTeam2;

    public Match(Team team1, Team team2, int goalsTeam1, int goalsTeam2) {
        this.team1 = team1;
        this.team2 = team2;
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
    }
}

class Group {
    String name;
    ArrayList<Team> teams;

    public Group(String name, ArrayList<Team> teams) {
        this.name = name;
        this.teams = teams;
    }

    public void calculateResults(ArrayList<Match> matches) {
        for (Match match : matches) {
            match.team1.addMatchResult(match.goalsTeam1, match.goalsTeam2);
            match.team2.addMatchResult(match.goalsTeam2, match.goalsTeam1);
        }
    }
}

class ClassBD {
    public static void main(String[] args) throws IOException {
        ArrayList<Match> matches = new ArrayList<>();
        BufferedReader gameReader = new BufferedReader(new FileReader("Game.txt"));
        String line;
        while ((line = gameReader.readLine()) != null) {
            String[] parts = line.split(" ");
            String team1Name = parts[0];
            String[] goals = parts[1].split(":");
            int goals1 = Integer.parseInt(goals[0]);
            int goals2 = Integer.parseInt(goals[1]);
            String team2Name = parts[2];
            matches.add(new Match(new Team(team1Name), new Team(team2Name), goals1, goals2));
        }
        gameReader.close();

        ArrayList<Group> groups = new ArrayList<>();
        BufferedReader groupsReader = new BufferedReader(new FileReader("Groups.txt"));
        while ((line = groupsReader.readLine()) != null) {
            String[] parts = line.split(" ");
            String groupName = parts[0];
            ArrayList<Team> groupTeams = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                groupTeams.add(new Team(parts[i]));
            }
            Group group = new Group(groupName, groupTeams);
            group.calculateResults(matches);
            groups.add(group);
        }
        groupsReader.close();

        PrintWriter groupsWriter = new PrintWriter("GroupsOut.txt");
        for (Group group : groups) {
            groupsWriter.println("Группа " + group.name + ":");
            for (Team team : group.teams) {
                groupsWriter.println(team.name + " " + team.points + " " + team.wins + " " + team.draws + " " + team.losses + " " + team.goalsScored + " " + team.goalsConceded);
            }
        }
        groupsWriter.close();

        PrintWriter resultsWriter = new PrintWriter("Results.txt");
        Collections.sort(matches, new Comparator<Match>() {
            public int compare(Match m1, Match m2) {
                return m1.team1.points + m1.team1.wins - m1.team1.losses - m1.team1.draws
                        - (m2.team1.points + m2.team1.wins - m2.team1.losses - m2.team1.draws);
            }
        });
        for (Match match : matches) {
            resultsWriter.println(match.team1);
            resultsWriter.println(match.team2);
        }
        resultsWriter.close();
    }
}
