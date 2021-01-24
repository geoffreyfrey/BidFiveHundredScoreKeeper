package com.geoffreyfrey.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by geoff on 6/3/16.
 */
public class TeamDAO extends DatabaseDAO{

    Context ctx;

    public TeamDAO(Context ctx){
        super(ctx);
    }

    //Add a new row to the database
    public void addTeam(Team team){

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.PLAYER_ONE_NAME, team.get_playerOneName());
        values.put(SQLiteDatabaseHandler.PLAYER_TWO_NAME, team.get_playerTwoName());
        values.put(SQLiteDatabaseHandler.GAMES_PLAYED, team.get_gamesPlayed());
        values.put(SQLiteDatabaseHandler.VICTORIES, team.get_gamesWon());
        db.insert(SQLiteDatabaseHandler.TEAMS_TABLE, null, values);
        db.close();
    }

    //Sees if value exists
    public boolean checkIfUnique(String playerOneName, String playerTwoName){
        boolean b;

        if (!db.isOpen()){
            super.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.TEAMS_TABLE + " WHERE ("
                + SQLiteDatabaseHandler.PLAYER_ONE_NAME + " = ? "
                + "OR " + SQLiteDatabaseHandler.PLAYER_ONE_NAME + " =? ) "
                + "AND (" + SQLiteDatabaseHandler.PLAYER_TWO_NAME + " =? "
                + "OR " + SQLiteDatabaseHandler.PLAYER_TWO_NAME + " =?)";

        Cursor cursor = db.rawQuery(query, new String[] {playerOneName, playerTwoName, playerOneName, playerTwoName});

        if(cursor.getCount()<=0){
            b = true;
        } else {
            b = false;
        }

        cursor.close();
        db.close();
        return b;

    }

    //returns team id when two names are given
    public int getTeamID(String playerOneName, String playerTwoName){
        int id = 0;

        if (!db.isOpen()){
            super.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.TEAMS_TABLE + " WHERE ("
                + SQLiteDatabaseHandler.PLAYER_ONE_NAME + " = ? "
                + "OR " + SQLiteDatabaseHandler.PLAYER_ONE_NAME + " =? ) "
                + "AND (" + SQLiteDatabaseHandler.PLAYER_TWO_NAME + " =? "
                + "OR " + SQLiteDatabaseHandler.PLAYER_TWO_NAME + " =?)";

        Cursor cursor = db.rawQuery(query, new String[] {playerOneName, playerTwoName, playerOneName, playerTwoName});

        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ID));
        }

        cursor.close();
        db.close();
        return id;
    }

    public String[] getTeamPlayerNames(int teamID){
        String[] playerNames = new String[2];

        if (!db.isOpen()){
            super.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.TEAMS_TABLE + " WHERE " + SQLiteDatabaseHandler.TEAM_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(teamID)});

        if(cursor.moveToFirst()){
            playerNames[0] = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_ONE_NAME));
            playerNames[1] = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_TWO_NAME));
        }

        cursor.close();
        db.close();
        return playerNames;
    }

    //Add a new row to the database
    public void updateTeam(Team team){

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.PLAYER_ONE_NAME, team.get_playerOneName());
        values.put(SQLiteDatabaseHandler.PLAYER_TWO_NAME, team.get_playerTwoName());
        values.put(SQLiteDatabaseHandler.GAMES_PLAYED, team.get_gamesPlayed());
        values.put(SQLiteDatabaseHandler.VICTORIES, team.get_gamesWon());
        db.update(SQLiteDatabaseHandler.TEAMS_TABLE, values, SQLiteDatabaseHandler.TEAM_ID + "=" + team.get_id(), null);
        db.close();
    }

    //Reset Team stats
    public void resetTeamStatsNoWrite(Team team){

        team.set_gamesPlayed(0);
        team.set_gamesWon(0);
    }

    //Print out database players as ArrayList
    public ArrayList<Team> dbTeamsToArrayList(){

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        //Setting up variables
        ArrayList<Team> arrayList= new ArrayList<>();
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.TEAMS_TABLE + " WHERE 1";

        //Cursor to point to location in your results
        Cursor cursor = db.rawQuery(query, null);

        //Move to the first row in your results
        cursor.moveToFirst();
        int i = 0;

        while(!cursor.isAfterLast()){
            Team team = new Team(0);

            if(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ID))!= 0){
                team.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ID)));
                team.set_playerOneName(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_ONE_NAME)));
                team.set_playerTwoName(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_TWO_NAME)));
                team.set_gamesPlayed(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.GAMES_PLAYED)));
                team.set_gamesWon(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.VICTORIES)));
            }
            arrayList.add(i, team);
            i++;
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return arrayList;
    }

}
