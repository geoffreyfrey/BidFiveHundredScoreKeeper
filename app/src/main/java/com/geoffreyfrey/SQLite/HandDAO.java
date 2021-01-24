package com.geoffreyfrey.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by geoff on 6/20/16.
 */
public class HandDAO extends DatabaseDAO {

    Context ctx;

    public HandDAO(Context ctx) {
        super(ctx);
    }

    //Add a new row to the database
    public int addHand(Hand hand){

        //Finding the matchID based on the game ID
        Game game = getGame(hand.get_gameID());
        Team team1 = getTeam(game.get_teamOneID());
        hand.set_matchID(game.get_matchID());
        hand.set_teamOneID(game.get_teamOneID());
        hand.set_teamTwoID(game.get_teamTwoID());
//        Player bidder = getPlayerByName(team1.get_playerOneName());

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.FOURH_GAME_ID, hand.get_gameID());
        values.put(SQLiteDatabaseHandler.FOURH_MATCH_ID, hand.get_matchID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_ID, game.get_teamOneID());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_ID, game.get_teamTwoID());
//        values.put(SQLiteDatabaseHandler.DEALER_ID, hand.get_dealer());
//        values.put(SQLiteDatabaseHandler.BIDDER_ID, hand.get_bidder());
        values.put(SQLiteDatabaseHandler.BID, hand.get_bid());
//        values.put(SQLiteDatabaseHandler.PREVIOUS_HAND_ID, hand.get_previousHandID());
//        values.put(SQLiteDatabaseHandler.TEAM_ONE_TRICKS_WON, hand.get_teamOneTricks());
//        values.put(SQLiteDatabaseHandler.TEAM_TWO_TRICKS_WON, hand.get_teamTwoTricks());
//        values.put(SQLiteDatabaseHandler.TEAM_ONE_SCORE_CHANGE, hand.get_teamOneScoreChange());
//        values.put(SQLiteDatabaseHandler.TEAM_TWO_SCORE_CHANGE, hand.get_teamTwoScoreChange());
//        values.put(SQLiteDatabaseHandler.TEAM_ONE_FINAL_SCORE, hand.get_teamOneEndingScore());
//        values.put(SQLiteDatabaseHandler.TEAM_TWO_FINAL_SCORE, hand.get_teamTwoEndingScore());
        values.put(SQLiteDatabaseHandler.HAND_COMPLETE, hand.get_handComplete());

        int handID = (int) db.insert(SQLiteDatabaseHandler.FOURH_HANDS_TABLE, null, values);
        db.close();

        return handID;
    }

    //Method to delete a hand entry
    public void deleteHand(int handID){
        //Correcting the game data before removing the hand.
        Hand hand = getHand(handID);
        Game game = getGame(hand.get_gameID());
        GameDAO gameDAO = new GameDAO(ctx);

        if (game.get_winningTeamID()!=0){
            gameDAO.completeChange(game, -1);
        }

        //Correcting Bidder Stats
        if (hand.get_handComplete()==1){
            updateBidderStats(hand, -1);
        }

        if(hand.get_previousHandID()==0){
            game.set_teamOneScore(0);
            game.set_teamTwoScore(0);
            game.set_winningTeamID(0);
            game.set_lastHandID(0);
        } else {
            Hand prevHand = getHand(hand.get_previousHandID());
            game.set_teamOneScore(prevHand.get_teamOneEndingScore());
            game.set_teamTwoScore(prevHand.get_teamTwoEndingScore());
            game.set_winningTeamID(0);
            game.set_lastHandID(prevHand.get_id());
        }

        gameDAO.updateGame(game);

        //setting up database
        if (!db.isOpen()){
            super.open();
        }
        db.delete(SQLiteDatabaseHandler.FOURH_HANDS_TABLE, SQLiteDatabaseHandler.FOURH_HANDS_ID + "=" + Integer.toString(handID), null);
        db.close();
    }

    //Print out database hands as ArrayList
    public ArrayList<Hand> dbHandsToArrayList(int gameID){

        //setting up database
        if (!db.isOpen()){
            super.open();
        }

        //Setting up variables
        ArrayList<Hand> arrayList= new ArrayList<>();
        String query = "SELECT * FROM " + SQLiteDatabaseHandler.FOURH_HANDS_TABLE + " WHERE " + SQLiteDatabaseHandler.FOURH_GAME_ID + " =?";

        //Cursor to point to location in your results
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(gameID)});

        //Move to the first row in your results
        cursor.moveToFirst();
        int i = 0;

        while(!cursor.isAfterLast()){
            Hand hand = new Hand(gameID);

            if(cursor.getInt(cursor.getColumnIndex(SQLiteDatabaseHandler.FOURH_HANDS_ID))!= 0){
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
            arrayList.add(i, hand);
            i++;
            cursor.moveToNext();
        }

        db.close();
        return arrayList;
    }

    //Add a new row to the database
    public void updateHand(Hand hand){

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.FOURH_GAME_ID, hand.get_gameID());
        values.put(SQLiteDatabaseHandler.FOURH_MATCH_ID, hand.get_matchID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_ID, hand.get_teamOneID());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_ID, hand.get_teamTwoID());
        values.put(SQLiteDatabaseHandler.DEALER_ID, hand.get_dealer());
        values.put(SQLiteDatabaseHandler.BIDDER_ID, hand.get_bidder());
        values.put(SQLiteDatabaseHandler.BID, hand.get_bid());
        values.put(SQLiteDatabaseHandler.PREVIOUS_HAND_ID, hand.get_previousHandID());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_TRICKS_WON, hand.get_teamOneTricks());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_TRICKS_WON, hand.get_teamTwoTricks());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_SCORE_CHANGE, hand.get_teamOneScoreChange());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_SCORE_CHANGE, hand.get_teamTwoScoreChange());
        values.put(SQLiteDatabaseHandler.TEAM_ONE_FINAL_SCORE, hand.get_teamOneEndingScore());
        values.put(SQLiteDatabaseHandler.TEAM_TWO_FINAL_SCORE, hand.get_teamTwoEndingScore());
        values.put(SQLiteDatabaseHandler.HAND_COMPLETE, hand.get_handComplete());
        db.update(SQLiteDatabaseHandler.FOURH_HANDS_TABLE, values, SQLiteDatabaseHandler.FOURH_HANDS_ID + "=" + hand.get_id(), null);
        db.close();
    }

    //Update dealer in database
    public void updateHandDealer(Hand hand){

        //setting up database helper
        if (!db.isOpen()){
            super.open();
        }

        //Setting up input values
        ContentValues values = new ContentValues();
        values.put(SQLiteDatabaseHandler.DEALER_ID, hand.get_dealer());
        db.update(SQLiteDatabaseHandler.FOURH_HANDS_TABLE, values, SQLiteDatabaseHandler.FOURH_HANDS_ID + "=" + hand.get_id(), null);
        db.close();
    }

    public void updateBidderStats(Hand hand, int change){
        //This methods updates the bidder stats. change should be set to 1 for new hands and -1 to cancel a hand.

        Player bidder = getPlayerByID(hand.get_bidder());
        Team teamOne = getTeam(hand.get_teamOneID());
        int bidValue;
        PlayerDAO playerDAO = new PlayerDAO(ctx);


        if(teamOne.isPlayerOnTeamByName(bidder.get_name())){
            bidValue = hand.get_teamOneScoreChange();
        } else {
            bidValue = hand.get_teamTwoScoreChange();
        }

        bidder.set_bids(bidder.get_bids() + change);

        if (bidValue>=0){
            bidder.set_bidsWon(bidder.get_bidsWon()+1*change);
        }

        bidder.set_netPoints(bidder.get_netPoints()+change*bidValue);

        playerDAO.updatePlayer(bidder);
    }

    public Hand copyHand(Hand hand){
        Hand copy = new Hand();
        copy.set_id(hand.get_id());
        copy.set_gameID(hand.get_gameID());
        copy.set_matchID(hand.get_matchID());
        copy.set_teamOneID(hand.get_teamOneID());
        copy.set_teamTwoID(hand.get_teamTwoID());
        copy.set_dealer(hand.get_dealer());
        copy.set_bidder(hand.get_bidder());
        copy.set_bid(hand.get_bid());
        copy.set_previousHandID(hand.get_previousHandID());
        copy.set_teamOneTricks(hand.get_teamOneTricks());
        copy.set_teamTwoTricks(hand.get_teamTwoTricks());
        copy.set_teamOneScoreChange(hand.get_teamOneScoreChange());
        copy.set_teamTwoScoreChange(hand.get_teamTwoScoreChange());
        copy.set_teamOneEndingScore(hand.get_teamOneEndingScore());
        copy.set_teamTwoEndingScore(hand.get_teamTwoEndingScore());
        copy.set_handComplete(hand.get_handComplete());

        return copy;
    }

}
