package com.geoffreyfrey.bidfivehundredscorekeeper;

/**
 * Created by geoff on 6/20/16.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.geoffreyfrey.SQLite.Team;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    int gameID;
    ArrayList<Hand> handArrayList;
    ListView list;
    DatabaseDAO dbDAO;
    Game game;
    public static boolean lastHandComplete;

    //Creates the layout including the toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbarText.setText("HANDS");
        setSupportActionBar(myToolbar);

        //Pulling matchID and gameID from the intent
        Bundle extras = getIntent().getExtras();
        gameID = extras.getInt("gameID");
        dbDAO = new DatabaseDAO(this);
        game = dbDAO.getGame(gameID);

//        myToolbar.setNavigationIcon(R.drawable.up_arrow);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent upIntent = NavUtils.getParentActivityIntent(GameActivity.this);
//                int matchID = game.get_matchID();
//                upIntent.putExtra("matchID", matchID);
//                NavUtils.navigateUpTo(GameActivity.this, upIntent);
//            }
//        });

        //Setting up the Header text
        setupGameHeader(game);

        //Setting up Victory text if the game is over
        if (game.get_winningTeamID() != 0){
            setupVictoryText();
        }

        //Setting up the ListView
        setupListView();

        //Setting up onLongClickListener
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                int lastPosition= list.getCount() -1;

                if(i==lastPosition && i!=0){
                    Hand hand = (Hand) adapterView.getItemAtPosition(i);
                    final int handID = hand.get_id();

                    //pull up dialog to save or not before exit
                    new AlertDialog.Builder(GameActivity.this)
                            .setTitle("Delete Hand?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialogInterface, int i){

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    HandDAO handDAO = new HandDAO(GameActivity.this);
                                    handDAO.deleteHand(handID);
                                    setupListView();
                                }
                            }).create().show();
                }
            return true;
            }
        });

        //Setting up the onClick Options
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int lastPosition= list.getCount() -1;

                if(i==lastPosition){
                    Hand hand = (Hand) adapterView.getItemAtPosition(i);
                    final int handID = hand.get_id();

                    //Starting the match activity
                    Intent intent = new Intent(GameActivity.this, HandActivity.class);
                    intent.putExtra("handID", handID);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        //if the file has been edited, give the user a chance to decide to save or not

        Intent intent = new Intent(GameActivity.this, MatchActivity.class);
        int matchID = game.get_matchID();
        intent.putExtra("matchID", matchID);
        startActivity(intent);
    }

    private void setupListView(){
        //Loading existing hands
        HandDAO handDAO = new HandDAO(this);

        handArrayList = handDAO.dbHandsToArrayList(gameID);

        lastHandComplete = true;

        if (handArrayList != null) {
            list = (ListView) findViewById(R.id.hand_list);
            HandArrayAdapter adapter = new HandArrayAdapter(this, R.layout.hand_table, handArrayList);
            list.setEmptyView(findViewById(android.R.id.empty));
            list.setAdapter(adapter);
        }
    };

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

    // This method runs when the newHand button is clicked.
    public void newHand(View view){

        if (lastHandComplete) {
            Bundle extras = getIntent().getExtras();
            final int gameID = extras.getInt("gameID");
            game = dbDAO.getGame(gameID);

            if (game.get_winningTeamID()==0){
                Hand hand = new Hand(game.get_id());
                HandDAO handDAO = new HandDAO(this);
                int handID = handDAO.addHand(hand);
                Intent intent = new Intent(this, HandActivity.class);
                intent.putExtra("handID", handID);
                startActivity(intent);

            } else {
                //pull up dialog to save or not before exit
                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("New Game?")
                        .setMessage("This game is finished. Start a new game?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                newGame();
                            }
                        }).create().show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.complete_initialized_hand_warning), Toast.LENGTH_SHORT).show();
        }



        //Setting up an instance of the SetPlayerQuantity Fragment to use.
//        SetPlayerQuantity playerQuantityFragment = new SetPlayerQuantity();
//        FragmentManager fm = getSupportFragmentManager();
//        playerQuantityFragment.show(fm, "dialog");
    }

    //Method to setup the match header
    public void setupGameHeader(Game game){

        TextView tvTeamOne = (TextView) findViewById(R.id.team_one_id);
        TextView tvTeamTwo = (TextView) findViewById(R.id.team_two_id);

        Team teamOne = dbDAO.getTeam(game.get_teamOneID());
        Team teamTwo = dbDAO.getTeam(game.get_teamTwoID());

        String teamOneString = teamOne.get_playerOneName() + " and " + teamOne.get_playerTwoName();
        String teamTwoString = teamTwo.get_playerOneName() + " and " + teamTwo.get_playerTwoName();

        tvTeamOne.setText(teamOneString);
        tvTeamTwo.setText(teamTwoString);
    }

    public void newGame(){
        Game newGame = new Game(game.get_matchID());
        GameDAO gameDAO = new GameDAO(this);
        int newGameID = gameDAO.addGame(newGame);

        Hand hand = new Hand(newGameID);
        HandDAO handDAO = new HandDAO(this);
        int handID = handDAO.addHand(hand);
        Intent intent = new Intent(this, HandActivity.class);
        intent.putExtra("handID", handID);
        startActivity(intent);
    }

    public void setupVictoryText(){

//        if (game.get_winningTeamID()==game.get_teamOneID()){
//            TextView teamOneWin = (TextView) findViewById(R.id.team_one_victory);
//            teamOneWin.setText("Victory!");
//        } else {
//            TextView teamTwoWin = (TextView) findViewById(R.id.team_two_victory);
//            teamTwoWin.setText("Victory!");
//        }
    }

}


