package com.geoffreyfrey.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by geoff on 6/1/16.
 */

public class DatabaseDAO {

    protected SQLiteDatabase db;
    private SQLiteDatabaseHandler dbHandler;
    private Context mContext;

    public DatabaseDAO(Context ctx){
        this.mContext = ctx;
        dbHandler = SQLiteDatabaseHandler.getHandler(mContext);
        open();
    }

    public void open() throws SQLException{
        if(dbHandler == null)
            dbHandler = SQLiteDatabaseHandler.getHandler(mContext);
        db = dbHandler.getWritableDatabase();
    }

    public String[] getColumnNames(String tableName){

        if (!db.isOpen()){
            this.open();
        }

        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        String[] columnNames = cursor.getColumnNames();

        cursor.close();
        db.close();
        return columnNames;
    }

    public Player getPlayerByID(int playerID){
        Player player = new Player();
        player.set_id(playerID);

        if (!db.isOpen()){
            this.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.PLAYERS_TABLE + " WHERE " + SQLiteDatabaseHandler.PLAYER_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(playerID)});

        if(cursor.moveToFirst()){
            player.set_name(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_NAME)));
            player.set_gamesPlayed(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_GAMES_PLAYED)));
            player.set_gamesWon(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_FOURH_GAMES_WON)));
            player.set_bids(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_BIDS_QUANTITY)));
            player.set_bidsWon(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_BIDS_WON)));
            player.set_netPoints(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_NET_POINTS)));
            player.set_deleted(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_DELETED)));
        }

        cursor.close();
        db.close();
        return player;
    }

    public Player getPlayerByName(String playerName){
        Player player = new Player(playerName);

        if (!db.isOpen()){
            this.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.PLAYERS_TABLE + " WHERE " + SQLiteDatabaseHandler.PLAYER_NAME + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {playerName});

        if(cursor.moveToFirst()){
            player.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_ID)));
            player.set_gamesPlayed(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_GAMES_PLAYED)));
            player.set_gamesWon(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_FOURH_GAMES_WON)));
            player.set_bids(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_BIDS_QUANTITY)));
            player.set_bidsWon(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_BIDS_WON)));
            player.set_netPoints(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_NET_POINTS)));
            player.set_deleted(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_DELETED)));
        }

        cursor.close();
        db.close();
        return player;
    }

    public Team getTeam(int teamID){
        Team team = new Team(teamID);

        if (!db.isOpen()){
            this.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.TEAMS_TABLE + " WHERE " + SQLiteDatabaseHandler.TEAM_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(teamID)});

        if(cursor.moveToFirst()){
            team.set_playerOneName(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_ONE_NAME)));
            team.set_playerTwoName(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.PLAYER_TWO_NAME)));
            team.set_gamesPlayed(Integer.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.GAMES_PLAYED))));
            team.set_gamesWon(Integer.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.VICTORIES))));
        }

        cursor.close();
        db.close();
        return team;
    }

    public Match getMatch(int matchID){
        Match match = new Match(matchID);

        if (!db.isOpen()){
            this.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_MATCHES_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_MATCH_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(matchID)});

        if(cursor.moveToFirst()){
//            match.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID)));
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

        cursor.close();
        db.close();
        return match;
    }

    public Game getGame(int gameID){
        Game game = new Game();
        game.set_id(gameID);

        if (!db.isOpen()){
            this.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_GAMES_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_GAME_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(gameID)});

        if(cursor.moveToFirst()){
//            match.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID)));
            game.set_matchID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID)));
            game.set_teamOneID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_ID)));
            game.set_teamTwoID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_ID)));
            game.set_teamOneScore(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_SCORE)));
            game.set_teamTwoScore(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_SCORE)));
            game.set_winningTeamID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.WINNING_TEAM_ID)));
            try{
                String dateString = cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.GAME_DATE));
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateFormat.parse(dateString);
                game.set_gameDate(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            game.set_lastHandID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.LAST_HAND_ID)));
        }

        cursor.close();
        db.close();
        return game;
    }

    public Hand getHand(int handID){
        Hand hand = new Hand();
        hand.set_id(handID);


        if (!db.isOpen()){
            this.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_HANDS_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_HANDS_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(handID)});

        if(cursor.moveToFirst()){
            hand.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_HANDS_ID)));
            hand.set_gameID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_GAME_ID)));
            hand.set_matchID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID)));
            hand.set_teamOneID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_ID)));
            hand.set_teamTwoID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_ID)));
            hand.set_dealer(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.DEALER_ID)));
            hand.set_bidder(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.BIDDER_ID)));
            hand.set_bid(cursor.getString(cursor.getColumnIndex(SQLiteDatabaseHandler.BID)));
            hand.set_previousHandID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.PREVIOUS_HAND_ID)));
            hand.set_teamOneTricks(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_TRICKS_WON)));
            hand.set_teamTwoTricks(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_TRICKS_WON)));
            hand.set_teamOneScoreChange(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_SCORE_CHANGE)));
            hand.set_teamTwoScoreChange(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_SCORE_CHANGE)));
            hand.set_teamOneEndingScore(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_FINAL_SCORE)));
            hand.set_teamTwoEndingScore(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_FINAL_SCORE)));
            hand.set_handComplete(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.HAND_COMPLETE)));

        }

        cursor.close();
        db.close();
        return hand;
    }

}
