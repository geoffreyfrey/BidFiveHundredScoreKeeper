package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geoffreyfrey.SQLite.DatabaseExplorer;
import com.geoffreyfrey.SQLite.Match;
import com.geoffreyfrey.SQLite.MatchDAO;
import com.geoffreyfrey.SQLite.PlayerDAO;
import com.geoffreyfrey.SQLite.TeamDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SetPlayerQuantity.PlayerQuantityListener {

    int playerQuantity;
    ArrayList<Match> matchArrayList;
    ListView list;


    //Creates the layout including the toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbarText.setText("500 SCORECARD");
        setSupportActionBar(myToolbar);

        //Loading all of the matches, team IDs, and corresponding player names
        setupListView();

        //Setting up onLongClickListener
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Match match = (Match) adapterView.getItemAtPosition(i);
                final int matchID = match.get_id();

                //pull up dialog to save or not before exit
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Match?")
//                        .setMessage("Do you want to save the changes?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                            jsonHelper.setPlayerNameArray(nameArrayList, PlayerEditor.this);
                                MatchDAO matchDAO = new MatchDAO(MainActivity.this);
                                matchDAO.deleteMatch(matchID);

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
                Match match = (Match) adapterView.getItemAtPosition(i);
                int matchID = match.get_id();

                //Starting the match activity
                Intent intent = new Intent(MainActivity.this, MatchActivity.class);
                intent.putExtra("matchID", matchID);
                startActivity(intent);

            }
        });



    }

    //Set up the list view
    private void setupListView(){
        MatchDAO matchDAO = new MatchDAO(this);

        matchArrayList = matchDAO.dbMatchesToArrayList();

        //Setting up the ListView
        if (matchArrayList != null) {
            list = (ListView) findViewById(R.id.match_list);
            MatchArrayAdapter adapter = new MatchArrayAdapter(this, R.layout.match_table, matchArrayList);
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

    // This method runs when the newMatch button is clicked.
    public void newMatch(View view){
        //Setting up an instance of the SetPlayerQuantity Fragment to use.
        SetPlayerQuantity playerQuantityFragment = new SetPlayerQuantity();
        FragmentManager fm = getSupportFragmentManager();
        playerQuantityFragment.show(fm, "dialog");
    }

    //external method to for the SetPlayerQuantity fragment to pass player quantity.
    public void setPlayerQuantity(int quantity){
        playerQuantity = quantity;

        //Call to start the NewMatch Activity
//        startNewMatch(playerQuantity);
    }

//    public void startNewMatch(int playerQuantity){

    public void startNewMatch(View view){
        playerQuantity = 4;

        switch(playerQuantity){
            case 4:{
                Intent intent = new Intent(this, NewMatchFour.class);
                startActivity(intent);
                break;
            }
            default:break;
        }


    }

    public void deleteMatchHandler(View v){

        list.invalidateViews();
    }

}

