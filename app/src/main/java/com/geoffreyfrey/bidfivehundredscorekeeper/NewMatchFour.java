package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.geoffreyfrey.SQLite.DatabaseDAO;
import com.geoffreyfrey.SQLite.DatabaseExplorer;
import com.geoffreyfrey.SQLite.Game;
import com.geoffreyfrey.SQLite.GameDAO;
import com.geoffreyfrey.SQLite.Hand;
import com.geoffreyfrey.SQLite.HandDAO;
import com.geoffreyfrey.SQLite.Match;
import com.geoffreyfrey.SQLite.MatchDAO;
import com.geoffreyfrey.SQLite.Player;
import com.geoffreyfrey.SQLite.PlayerDAO;
import com.geoffreyfrey.SQLite.SQLiteDatabaseHandler;
import com.geoffreyfrey.SQLite.Team;
import com.geoffreyfrey.SQLite.TeamDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewMatchFour extends AppCompatActivity {
    AutoCompleteTextView playerOneNameInput;
    AutoCompleteTextView playerTwoNameInput;
    AutoCompleteTextView playerThreeNameInput;
    AutoCompleteTextView playerFourNameInput;

    //Setting up the Database Helper
//    SQLiteDatabaseHandler dbHandler = new SQLiteDatabaseHandler(this);
//    PlayerDAO playerDAO = new PlayerDAO(this);
    ArrayList<String> nameArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creating the layout and action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match_four);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbarText.setText("NEW MATCH");
        setSupportActionBar(myToolbar);

        //Loading previously input player names
        PlayerDAO playerDAO = new PlayerDAO(this);
        nameArrayList = playerDAO.dbPlayerNamesToArrayList();

        //Setting up the autoCompletes
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nameArrayList);

        playerOneNameInput = (AutoCompleteTextView) findViewById(R.id.player_one_name);
        playerOneNameInput.setAdapter(adapter);
        playerOneNameInput.setThreshold(1);

        playerTwoNameInput = (AutoCompleteTextView) findViewById(R.id.player_two_name);
        playerTwoNameInput.setAdapter(adapter);
        playerTwoNameInput.setThreshold(1);

        playerThreeNameInput = (AutoCompleteTextView) findViewById(R.id.player_three_name);
        playerThreeNameInput.setAdapter(adapter);
        playerThreeNameInput.setThreshold(1);

        playerFourNameInput = (AutoCompleteTextView) findViewById(R.id.player_four_name);
        playerFourNameInput.setAdapter(adapter);
        playerFourNameInput.setThreshold(1);


    }

    //calls menu inflater for the toolbar when the toolbar is created.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Actions for when things in the toolbar are clicked.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;

            case R.id.manage_players:
                Intent intent = new Intent(this, PlayerEditor.class);
                startActivity(intent);
                return true;

            case R.id.manage_teams:
                Intent intentTeam = new Intent(this, TeamEditor.class);
                startActivity(intentTeam);
                return true;

            case R.id.view_database:
                Intent intent1 = new Intent(this, DatabaseExplorer.class);
                startActivity(intent1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public void startMatch(View view) {
        //Creating the new Match
        DatabaseDAO dbDAO = new DatabaseDAO(this);
        MatchDAO matchDAO = new MatchDAO(this);
        Match match = new Match();

        //Assigning inputs
        String[] currentPlayerNames = {playerOneNameInput.getText().toString(), playerTwoNameInput.getText().toString(),
                playerThreeNameInput.getText().toString(), playerFourNameInput.getText().toString()};

        //Setting up HashSet to check for name repeats
        Set<String> uniqueUsers = new HashSet<String>();

        //Making sure all names are filled in and unique.
        for (int i = 0; i < currentPlayerNames.length; i++) {
            if (currentPlayerNames[i].matches("")) {
                Toast.makeText(this, getString(R.string.missing_player_name), Toast.LENGTH_LONG).show();
                return;
            } else if (!uniqueUsers.add(currentPlayerNames[i])) {
                Toast.makeText(this, getString(R.string.duplicate_name), Toast.LENGTH_LONG).show();
                return;
            }
        }

        //Saving options for SQL Database
        PlayerDAO playerDAO = new PlayerDAO(this);
        int playerID;

        for (int i=0; i< currentPlayerNames.length; i++) {
            if(playerDAO.checkIfUnique(currentPlayerNames[i])){
                Player newPlayer = new Player(currentPlayerNames[i]);
                playerID = playerDAO.addPlayer(newPlayer);
            } else {
                Player player = dbDAO.getPlayerByName(currentPlayerNames[i]);
                playerID = player.get_id();
                playerDAO.unmarkPlayerAsDeleted(currentPlayerNames[i]);
            }
            switch(i) {
                case 0:
                    match.set_playerOneID(playerID);
                    break;
                case 1:
                    match.set_playerTwoID(playerID);
                    break;
                case 2:
                    match.set_playerThreeID(playerID);
                    break;
                case 3:
                    match.set_playerFourID(playerID);
                    break;
            }
        }

        //Checking for unique teams
        TeamDAO teamDAO = new TeamDAO(this);

        List<Team> listTeam = new ArrayList<Team>();
        Team teamOne = new Team(currentPlayerNames[0], currentPlayerNames[2]);
        Team teamTwo = new Team(currentPlayerNames[1], currentPlayerNames[3]);
        listTeam.add(teamOne);
        listTeam.add(teamTwo);

        for (int i=0; i<listTeam.size(); i++){
            if (teamDAO.checkIfUnique(listTeam.get(i).get_playerOneName(), listTeam.get(i).get_playerTwoName())){
                teamDAO.addTeam(listTeam.get(i));
            }
        }

        int team1ID = teamDAO.getTeamID(teamOne.get_playerOneName(), teamOne.get_playerTwoName());
        int team2ID = teamDAO.getTeamID(teamTwo.get_playerOneName(), teamTwo.get_playerTwoName());

        match.set_teamOneID(team1ID);
        match.set_teamTwoID(team2ID);

        //Adding the new match to the SQLite database
        int matchID = matchDAO.addMatch(match);

        //Making a new game and starting the first hand.
        Game game = new Game(matchID);
        GameDAO gameDAO = new GameDAO(this);
        int gameID = gameDAO.addGame(game);

        Hand hand = new Hand(gameID);
        HandDAO handDAO = new HandDAO(this);
        int handID = handDAO.addHand(hand);
        Intent intent = new Intent(this, HandActivity.class);
        intent.putExtra("handID", handID);
        startActivity(intent);

//        Intent intent = new Intent(this, MatchActivity.class);
//        intent.putExtra("matchID", matchID);
//        startActivity(intent);

    }

}
