package com.geoffreyfrey.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by geoff on 6/6/16.
 */
public class GameDAO extends DatabaseDAO{

    Context ctx;

    public GameDAO(Context ctx){
        super(ctx);
    }

    //Add a new row to the database
    public int addGame(Game game){
        Match match = getMatch(game.get_matchID());
        game.set_teamOneID(match.get_teamOneID());
        game.set_teamTwoID(match.get_teamTwoID());

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Finding the previous game
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_GAMES_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_MATCH_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(match.get_id())});

        int prevGameID;

        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToPosition(cursor.getCount() -1);
            prevGameID = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_GAME_ID));
        } else {
            prevGameID = 0;
        }

        match.set_lastGameID(prevGameID);
        MatchDAO matchDAO = new MatchDAO(ctx);
        matchDAO.updateMatch(match);

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.FOURH_MATCH_ID, game.get_matchID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_ID, game.get_teamOneID());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_ID, game.get_teamTwoID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_SCORE, game.get_teamOneScore());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_SCORE, game.get_teamTwoScore());
        values.put(SQLiteDatabaseHandler.WINNING_TEAM_ID, game.get_winningTeamID());
        values.put(SQLiteDatabaseHandler.LAST_HAND_ID, game.get_lastHandID());
        int gameID = (int) db.insert(SQLiteDatabaseHandler.FOURH_GAMES_TABLE, null, values);

        cursor.close();
        db.close();

        return gameID;
    }

    public void updateGame(Game game){

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.FOURH_GAME_ID, game.get_id());
        values.put(SQLiteDatabaseHandler.FOURH_MATCH_ID, game.get_matchID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_ID, game.get_teamOneID());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_ID, game.get_teamTwoID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_SCORE, game.get_teamOneScore());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_SCORE, game.get_teamTwoScore());
        values.put(SQLiteDatabaseHandler.WINNING_TEAM_ID, game.get_winningTeamID());
        values.put(SQLiteDatabaseHandler.GAME_DATE, getDateTime());
        values.put(SQLiteDatabaseHandler.LAST_HAND_ID, game.get_lastHandID());

        db.update(SQLiteDatabaseHandler.FOURH_GAMES_TABLE, values, SQLiteDatabaseHandler.FOURH_GAME_ID + "=" + game.get_id(), null);

        db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    //Method to delete a game entry
    public int deleteGame(int gameID){

        Game game = getGame(gameID);

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        if (game.get_winningTeamID()!=0){
            completeChange(game, -1);
        }

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        db.delete(SQLiteDatabaseHandler.FOURH_HANDS_TABLE, SQLiteDatabaseHandler.FOURH_GAME_ID + "=" + Integer.toString(gameID), null);
        int deleted = db.delete(SQLiteDatabaseHandler.FOURH_GAMES_TABLE, SQLiteDatabaseHandler.FOURH_GAME_ID + "=" + Integer.toString(gameID), null);
        db.close();
        return  deleted;
    }

    //Print out database games as ArrayList
    public ArrayList<Game> dbGamesToArrayList(int matchID){

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        //Setting up variables
        ArrayList<Game> arrayList= new ArrayList<>();
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_GAMES_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_MATCH_ID + " =?";

        //Cursor to point to location in your results
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(matchID)});

        //Move to the first row in your results
        cursor.moveToFirst();
        int i = 0;

        while(!cursor.isAfterLast()){
            Game game = new Game(matchID);

            if(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID))!= 0){
                game.set_id(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_GAME_ID)));
                game.set_matchID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_MATCH_ID)));
                game.set_teamOneID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_ID)));
                game.set_teamTwoID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_ID)));
                game.set_teamOneScore(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_ONE_SCORE)));
                game.set_teamTwoScore(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.TEAM_TWO_SCORE)));
                game.set_winningTeamID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.WINNING_TEAM_ID)));
                game.set_lastHandID(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.LAST_HAND_ID)));
            }
            arrayList.add(i, game);
            i++;
            cursor.moveToNext();
        }

        db.close();
        return arrayList;
    }

    public void completeChange(Game game, int change){

        PlayerDAO playerDAO = new PlayerDAO(ctx);
        TeamDAO teamDAO = new TeamDAO(ctx);

        Match match = getMatch(game.get_matchID());
        Team winningTeam = getTeam(game.get_winningTeamID());
        Team losingTeam;
        if (game.get_teamOneID()==game.get_winningTeamID()){
            losingTeam = getTeam(game.get_teamTwoID());
            match.set_teamOneVictories(match.get_teamOneVictories() + change);
        } else {
            losingTeam = getTeam(game.get_teamOneID());
            match.set_teamTwoVictories(match.get_teamTwoVictories() + change);
        }

        MatchDAO matchDAO = new MatchDAO(ctx);
        matchDAO.updateMatch(match);


        Player playerOne = getPlayerByID(match.get_playerOneID());
        Player playerTwo = getPlayerByID(match.get_playerTwoID());
        Player playerThree = getPlayerByID(match.get_playerThreeID());
        Player playerFour = getPlayerByID(match.get_playerFourID());

        List<Player> playerList = new ArrayList<>();
        playerList.add(playerOne);
        playerList.add(playerTwo);
        playerList.add(playerThree);
        playerList.add(playerFour);

        //Updating the players
        for (Player player : playerList){
            player.set_gamesPlayed(player.get_gamesPlayed() + change);
            if ((winningTeam.get_playerOneName().equals(player.get_name())) || (winningTeam.get_playerTwoName().equals(player.get_name()))) {
                player.set_gamesWon(player.get_gamesWon() + change);
            }
            playerDAO.updatePlayer(player);
        }

        //updating the winning team
        winningTeam.set_gamesPlayed(winningTeam.get_gamesPlayed() + change);
        winningTeam.set_gamesWon(winningTeam.get_gamesWon() + change);
        teamDAO.updateTeam(winningTeam);

        //updating the losing team
        losingTeam.set_gamesPlayed(losingTeam.get_gamesPlayed() + change);
        teamDAO.updateTeam(losingTeam);

    }

    public int getHandCount(Game game){
        int handCount;

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        //Setting up variables
        ArrayList<Game> arrayList= new ArrayList<>();
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_HANDS_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_GAME_ID + " =?";

        //Cursor to point to location in your results
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(game.get_id())});

        handCount = cursor.getCount();

        cursor.close();
        db.close();

        return handCount;
    }

    public int getFirstDealer(Game game) {

        Hand hand;
        int handID = 0;

        if (!db.isOpen()) {
            this.open();
        }

        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_HANDS_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_GAME_ID + " =?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(game.get_id())});

        if (cursor.moveToFirst()) {
            handID = cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_HANDS_ID));
        }

        cursor.close();
        db.close();

        hand = getHand(handID);
        int dealerID = hand.get_dealer();

        return dealerID;
    }

}
