package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.DialogInterface;
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

import com.geoffreyfrey.SQLite.Player;
import com.geoffreyfrey.SQLite.PlayerDAO;
import com.geoffreyfrey.SQLite.Team;

import java.util.ArrayList;


/**
 * Created by geoff on 5/24/16.
 */
public class PlayerEditor extends AppCompatActivity{

    //Initializing database handler
    ArrayList<Player> nameArrayList = new ArrayList<>();

    //View related variables
    ListView list;

    //Boolean to see if edits were made
    boolean editedSwitch;

    //String of deleted names
    ArrayList<String> deletedNames = new ArrayList<String>();

    //Selected Player
    Player player;
    PlayerArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creating the layout and action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_editor);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_no_options);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text_no_options);
        toolbarText.setText("PLAYER MANAGER");
        setSupportActionBar(myToolbar);

        //Resetting the switch indicating if the file has been edited
        editedSwitch = false;

        //Loading previously input player names
        final PlayerDAO playerDAO = new PlayerDAO(this);
        nameArrayList = playerDAO.dbPlayersToArrayList();


        //Setting up the ListView
        if(nameArrayList!=null){
            list = (ListView) findViewById(R.id.player_editor_list);
            adapter = new PlayerArrayAdapter(this, R.layout.player_table, nameArrayList);
            list.setEmptyView(findViewById(android.R.id.empty));
            list.setAdapter(adapter);
        }

        //Setting up the onClick Options
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                player = (Player) adapterView.getItemAtPosition(i);

                new AlertDialog.Builder(PlayerEditor.this)
                        .setTitle("Delete Player?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Mark that edits were performed
                                editedSwitch = true;
                                deletedNames.add(player.get_name());
                                adapter.remove(player);
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
                            PlayerEditor.super.onBackPressed();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            jsonHelper.setPlayerNameArray(nameArrayList, PlayerEditor.this);
                            for(int j = 0;j<deletedNames.size();j++){
                                PlayerDAO playerDAO = new PlayerDAO(PlayerEditor.this);
                                playerDAO.markPlayerAsDeleted(deletedNames.get(j));
                            }

                            PlayerEditor.super.onBackPressed();
                        }
                    }).create().show();
        } else {
            super.onBackPressed();
        }

    }

}
