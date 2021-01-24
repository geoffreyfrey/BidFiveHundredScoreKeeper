package com.geoffreyfrey.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.textservice.SpellCheckerSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by geoff on 6/3/16.
 */
public class MatchDAO extends DatabaseDAO{

    public MatchDAO(Context ctx){
        super(ctx);
    }

    //Add a new row to the database
    public int addMatch(Match match){
        int matchID;

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.TEAM_ONE_ID, match.get_teamOneID());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_ID, match.get_teamTwoID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_VICTORIES, match.get_teamOneVictories());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_VICTORIES, match.get_teamTwoVictories());
        values.put(SQLiteDatabaseHandler.PLAYER_ONE_ID, match.get_playerOneID());
        values.put(SQLiteDatabaseHandler.PLAYER_TWO_ID, match.get_playerTwoID());
        values.put(SQLiteDatabaseHandler.PLAYER_THREE_ID, match.get_playerThreeID());
        values.put(SQLiteDatabaseHandler.PLAYER_FOUR_ID, match.get_playerFourID());
        values.put(SQLiteDatabaseHandler.LAST_GAME_ID, match.get_lastGameID());
        matchID = (int) db.insert(SQLiteDatabaseHandler.FOURH_MATCHES_TABLE, null, values);
        db.close();

        return matchID;
    }

    //Method to delete a match entry
    public int deleteMatch(int matchID){
        //setting up database
        if (!db.isOpen()){
            super.open();
        }
        db.delete(SQLiteDatabaseHandler.FOURH_HANDS_TABLE, SQLiteDatabaseHandler.FOURH_MATCH_ID + "=" + Integer.toString(matchID), null);
        db.delete(SQLiteDatabaseHandler.FOURH_GAMES_TABLE, SQLiteDatabaseHandler.FOURH_MATCH_ID + "=" + Integer.toString(matchID), null);
        return db.delete(SQLiteDatabaseHandler.FOURH_MATCHES_TABLE, SQLiteDatabaseHandler.FOURH_MATCH_ID + "=" + Integer.toString(matchID), null);
    }

    //Print out database players as ArrayList
    public ArrayList<Match> dbMatchesToArrayList(){

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        //Setting up variables
        ArrayList<Match> arrayList= new ArrayList<>();
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_MATCHES_TABLE + " WHERE 1";

        //Cursor to point to location in your results
        Cursor cursor = db.rawQuery(query, null);

        //Move to the first row in your results
        cursor.moveToFirst();
        int i = 0;

        while(!cursor.isAfterLast()){
            Match match = new Match();

            if(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID))!= 0){
                match.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID)));
                match.set_teamOneID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_ID)));
                match.set_teamTwoID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_ID)));
                match.set_teamOneVictories(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_VICTORIES)));
                match.set_teamTwoVictories(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_VICTORIES)));
                try{
                    String dateString = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.MATCH_DATE));
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = dateFormat.parse(dateString);
                    match.set_matchDate(date);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                match.set_playerOneID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_ONE_ID)));
                match.set_playerTwoID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_TWO_ID)));
                match.set_playerThreeID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_THREE_ID)));
                match.set_playerFourID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_FOUR_ID)));
                match.set_lastGameID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.LAST_GAME_ID)));
            }
            arrayList.add(i, match);
            i++;
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return arrayList;
    }

    public int[] getTeamIDs(int matchID){
        int[] teamIDs = new int[2];

        if (!db.isOpen()){
            super.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_MATCHES_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_MATCH_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(matchID)});

        if(cursor.moveToFirst()){
            teamIDs[0] = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_ID));
            teamIDs[1] = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_ID));
        }

        cursor.close();
        db.close();
        return teamIDs;
    }

    public void updateMatch(Match match){

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.TEAM_ONE_ID, match.get_teamOneID());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_ID, match.get_teamTwoID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_VICTORIES, match.get_teamOneVictories());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_VICTORIES, match.get_teamTwoVictories());
        values.put(SQLiteDatabaseHandler.PLAYER_ONE_ID, match.get_playerOneID());
        values.put(SQLiteDatabaseHandler.PLAYER_TWO_ID, match.get_playerTwoID());
        values.put(SQLiteDatabaseHandler.PLAYER_THREE_ID, match.get_playerThreeID());
        values.put(SQLiteDatabaseHandler.PLAYER_FOUR_ID, match.get_playerFourID());
        values.put(SQLiteDatabaseHandler.LAST_GAME_ID, match.get_lastGameID());
        db.update(SQLiteDatabaseHandler.FOURH_MATCHES_TABLE, values, SQLiteDatabaseHandler.FOURH_MATCH_ID + "=" + match.get_id(), null);
        db.close();
    }

}
