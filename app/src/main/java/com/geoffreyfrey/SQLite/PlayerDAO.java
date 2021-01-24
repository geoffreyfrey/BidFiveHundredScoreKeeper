package com.geoffreyfrey.SQLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

/**
 * Created by geoff on 6/3/16.
 */
public class PlayerDAO extends DatabaseDAO {

    private static final String WHERE_ID_EQUALS = SQLiteDatabaseHandler.PLAYER_ID + " =?";

    public PlayerDAO(Context ctx){
        super(ctx);
    }

    //Add a new row to the database
    public int addPlayer(Player player){
        int playerID;

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.PLAYER_NAME, player.get_name());
        values.put(SQLiteDatabaseHandler.PLAYER_GAMES_PLAYED, player.get_gamesPlayed());
        values.put(SQLiteDatabaseHandler.PLAYER_FOURH_GAMES_WON, player.get_gamesWon());
        values.put(SQLiteDatabaseHandler.PLAYER_BIDS_QUANTITY, player.get_bids());
        values.put(SQLiteDatabaseHandler.PLAYER_BIDS_WON, player.get_bidsWon());
        values.put(SQLiteDatabaseHandler.PLAYER_NET_POINTS, player.get_netPoints());
        values.put(SQLiteDatabaseHandler.PLAYER_DELETED, player.get_deleted());
        playerID = (int) db.insert(SQLiteDatabaseHandler.PLAYERS_TABLE, null, values);
        db.close();

        return playerID;
    }

    //Delete a player from the database
    public void markPlayerAsDeleted(String playerName){

        Player player = getPlayerByName(playerName);
        player.set_gamesPlayed(0);
        player.set_gamesWon(0);
        player.set_bids(0);
        player.set_bidsWon(0);
        player.set_netPoints(0);
        player.set_deleted(1);
        updatePlayer(player);

    }

    //Delete a player from the database
    public void unmarkPlayerAsDeleted(String playerName){

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        String query = "UPDATE " + SQLiteDatabaseHandler.PLAYERS_TABLE
                + " SET " + SQLiteDatabaseHandler.PLAYER_DELETED
                + " = 0 WHERE " + SQLiteDatabaseHandler.PLAYER_NAME
                + " = ?";

        db.execSQL(query, new String[] {playerName});

        db.close();

    }
    //Print out database players as ArrayList
    public ArrayList<String> dbPlayerNamesToArrayList(){

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        //Setting up variables
        ArrayList<String> arrayList= new ArrayList<String>();
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.PLAYERS_TABLE + " WHERE " + SQLiteDatabaseHandler.PLAYER_DELETED + " = 0";

        //Cursor to point to location in your results
        Cursor c = db.rawQuery(query, null);

        //Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(SQLiteDatabaseHandler.PLAYER_NAME))!= null){
                arrayList.add(c.getString(c.getColumnIndex(SQLiteDatabaseHandler.PLAYER_NAME)));
            }
            c.moveToNext();
        }

        db.close();
        return arrayList;
    }

    //Print out database players as ArrayList
    public ArrayList<Player> dbPlayersToArrayList(){

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        //Setting up variables
        ArrayList<Player> arrayList= new ArrayList<>();
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.PLAYERS_TABLE + " WHERE 1";

        //Cursor to point to location in your results
        Cursor cursor = db.rawQuery(query, null);

        //Move to the first row in your results
        cursor.moveToFirst();
        int i = 0;

        while(!cursor.isAfterLast()){
            Player player = new Player();

            if(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_ID))!= 0){
                player.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_ID)));
                player.set_name(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_NAME)));
                player.set_gamesPlayed(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_GAMES_PLAYED)));
                player.set_gamesWon(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_FOURH_GAMES_WON)));
                player.set_bids(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_BIDS_QUANTITY)));
                player.set_bidsWon(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_BIDS_WON)));
                player.set_netPoints(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_NET_POINTS)));
                player.set_deleted(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_DELETED)));
            }
            if (player.get_deleted()!=1){
                arrayList.add(i, player);
                i++;
            }
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return arrayList;
    }

    //Sees if value exists
    public boolean checkIfUnique(String fieldValue){
        boolean b;

        if (!db.isOpen()){
            super.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.PLAYERS_TABLE + " WHERE " + SQLiteDatabaseHandler.PLAYER_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] {fieldValue});

        int c = cursor.getCount();

        if(cursor.getCount()<=0){
            b = true;
        } else {
            b = false;
        }

        cursor.close();
        db.close();
        return b;

    }

    public void updatePlayer(Player player){
        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.PLAYER_NAME, player.get_name());
        values.put(SQLiteDatabaseHandler.PLAYER_GAMES_PLAYED, player.get_gamesPlayed());
        values.put(SQLiteDatabaseHandler.PLAYER_FOURH_GAMES_WON, player.get_gamesWon());
        values.put(SQLiteDatabaseHandler.PLAYER_BIDS_QUANTITY, player.get_bids());
        values.put(SQLiteDatabaseHandler.PLAYER_BIDS_WON, player.get_bidsWon());
        values.put(SQLiteDatabaseHandler.PLAYER_NET_POINTS, player.get_netPoints());
        values.put(SQLiteDatabaseHandler.PLAYER_DELETED, player.get_deleted());
        db.update(SQLiteDatabaseHandler.PLAYERS_TABLE, values, SQLiteDatabaseHandler.PLAYER_ID + "=" + player.get_id(), null);
        db.close();
    }

}
