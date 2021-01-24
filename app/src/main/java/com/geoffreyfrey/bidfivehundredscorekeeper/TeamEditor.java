package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geoffreyfrey.SQLite.Match;
import com.geoffreyfrey.SQLite.PlayerDAO;
import com.geoffreyfrey.SQLite.Team;
import com.geoffreyfrey.SQLite.TeamDAO;

import java.util.ArrayList;


/**
 * Created by geoff on 5/24/16.
 */
public class TeamEditor extends AppCompatActivity{

    //Initializing database handler
    ArrayList<Team> teamArrayList = new ArrayList<>();

    //View related variables
    ListView list;

    //Boolean to see if edits were made
    boolean editedSwitch;

    //Individual Selected team
    Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creating the layout and action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_editor);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_no_options);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text_no_options);
        toolbarText.setText("TEAM MANAGER");
        setSupportActionBar(myToolbar);

        //Resetting the switch indicating if the file has been edited
        editedSwitch = false;

        //Loading previously input player names
        final TeamDAO teamDAO = new TeamDAO(this);
        teamArrayList = teamDAO.dbTeamsToArrayList();

        //Setting up the ListView
        if(teamArrayList!=null){
            list = (ListView) findViewById(R.id.team_editor_list);
            TeamArrayAdapter adapter = new TeamArrayAdapter(this, R.layout.team_editor_listview, teamArrayList);
            list.setEmptyView(findViewById(android.R.id.empty));
            list.setAdapter(adapter);
        }

        //Setting up the onClick Options
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                team = (Team) adapterView.getItemAtPosition(i);

                new AlertDialog.Builder(TeamEditor.this)
                        .setTitle("Reset Team Statistics?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Mark that edits were performed
                                editedSwitch = true;

                                teamDAO.resetTeamStatsNoWrite(team);
                                list.invalidateViews();
                            }
                        }).create().show();

            }
        });
    }

    @Override
    public void onBackPressed(){
        //if the file has been edited, give the user a chance to decide to save or not

        if(editedSwitch){
            //pull up dialog to save or not before exit
            new AlertDialog.Builder(this)
                    .setTitle("Save Changes?")
                    .setMessage("Do you want to save the changes?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogInterface, int i){
                            TeamEditor.super.onBackPressed();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            for(int j = 0;j<teamArrayList.size();j++){
                                TeamDAO teamDAO = new TeamDAO(TeamEditor.this);
                                teamDAO.updateTeam(teamArrayList.get(j));
                            }

                            TeamEditor.super.onBackPressed();
                        }
                    }).create().show();
        } else {
            super.onBackPressed();
        }

    }

}
