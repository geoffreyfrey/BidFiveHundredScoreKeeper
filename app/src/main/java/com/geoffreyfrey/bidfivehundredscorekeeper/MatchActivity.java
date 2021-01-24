package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.geoffreyfrey.SQLite.Team;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {

    int playerQuantity;
    ArrayList<Game> gameArrayList;
    ListView list;
    DatabaseDAO dbDAO;
    Match match;

    //Creates the layout including the toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbarText.setText("GAMES");
        setSupportActionBar(myToolbar);
//        myToolbar.setNavigationIcon(R.drawable.up_arrow);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent upIntent = NavUtils.getParentActivityIntent(MatchActivity.this);
//                NavUtils.navigateUpTo(MatchActivity.this, upIntent);
//            }
//        });

        //Pulling matchID from the intent
        Bundle extras = getIntent().getExtras();
        final int matchID = extras.getInt("matchID");
        dbDAO = new DatabaseDAO(this);
        match = dbDAO.getMatch(matchID);

        //Setting up the Header text
        setupMatchHeader(match);

        //Loading existing games
        setupListView();

        //Setting up onLongClickListener
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Game game = (Game) adapterView.getItemAtPosition(i);
                final int gameID = game.get_id();

                //pull up dialog to save or not before exit
                new AlertDialog.Builder(MatchActivity.this)
                        .setTitle("Delete Game?")
//                        .setMessage("Do you want to save the changes?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                            jsonHelper.setPlayerNameArray(nameArrayList, PlayerEditor.this);
                                GameDAO gameDAO = new GameDAO(MatchActivity.this);
                                gameDAO.deleteGame(gameID);

                                setupListView();
                            }
                        }).create().show();

                return true;
            }
        });

        //Setting up the onClick Options
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game game = (Game) adapterView.getItemAtPosition(i);
                int gameID = game.get_id();

                //Starting the match activity
                Intent intent = new Intent(MatchActivity.this, GameActivity.class);
                intent.putExtra("gameID", gameID);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed(){

        //if the file has been edited, give the user a chance to decide to save or not
        Intent upIntent = NavUtils.getParentActivityIntent(MatchActivity.this);
        NavUtils.navigateUpTo(MatchActivity.this, upIntent);
        NavUtils.navigateUpTo(MatchActivity.this, upIntent);
    }


    //
    private void setupListView(){
        GameDAO gameDAO = new GameDAO(this);
        int matchID = match.get_id();

        gameArrayList = gameDAO.dbGamesToArrayList(matchID);

        if (gameArrayList != null) {
            list = (ListView) findViewById(R.id.game_list);
            GameArrayAdapter adapter = new GameArrayAdapter(this, R.layout.match_table, gameArrayList);
            list.setEmptyView(findViewById(android.R.id.empty));
            list.setAdapter(adapter);
        }

    }



    //calls menu inflater for the toolbar when the toolbar is created.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);

    }

    //Refreshes when back is hit
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
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

    // This method runs when the newGame button is clicked.
    public void newGame(View view){
        Game game = new Game(match.get_id());
        GameDAO gameDAO = new GameDAO(this);
        int gameID = gameDAO.addGame(game);

        Hand hand = new Hand(gameID);
        HandDAO handDAO = new HandDAO(this);
        int handID = handDAO.addHand(hand);
        Intent intent = new Intent(this, HandActivity.class);
        intent.putExtra("handID", handID);
        startActivity(intent);
    }

    //Method to setup the match header
    public void setupMatchHeader(Match match){

        TextView tvTeamOne = (TextView) findViewById(R.id.team_one_id);
        TextView tvTeamTwo = (TextView) findViewById(R.id.team_two_id);
        TextView tvTeamOneWins = (TextView) findViewById(R.id.team_one_wins);
        TextView tvTeamTwoWins = (TextView) findViewById(R.id.team_two_wins);

        Team teamOne = dbDAO.getTeam(match.get_teamOneID());
        Team teamTwo = dbDAO.getTeam(match.get_teamTwoID());

        String teamOneString = teamOne.get_playerOneName() + " and " + teamOne.get_playerTwoName();
        String teamTwoString = teamTwo.get_playerOneName() + " and " + teamTwo.get_playerTwoName();

        tvTeamOne.setText(teamOneString);
        tvTeamTwo.setText(teamTwoString);
        tvTeamOneWins.setText(String.valueOf(match.get_teamOneVictories()));
        tvTeamTwoWins.setText(String.valueOf(match.get_teamTwoVictories()));

    }
}

