package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.geoffreyfrey.SQLite.Hand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by geoff on 6/29/16.
 */
public class BidderPointsCalculator {
    //Declaring Variables
    String bid;
    Hand hand;
    int biddingTeamID;
    Context ctx;

    //Constructors
    public BidderPointsCalculator(Hand handInput, int biddingTeamIDInput, Context ctxInput){
        this.hand = handInput;
        this.bid = hand.get_bid();
        this.biddingTeamID = biddingTeamIDInput;
        this.ctx = ctxInput;
    }

    //Methods
    public int[] calculateScoreChange(){

        int[] bidValueAndTricksNeeded = new int[2];
        int tricksNeeded;
        int bidValue;
        int[] scoreChange = new int[2];
        int bidderTricksWon;

        if(biddingTeamID == hand.get_teamOneID()){
            bidderTricksWon = hand.get_teamOneTricks();
        } else {
            bidderTricksWon = hand.get_teamTwoTricks();
        }

        Map<String, int[]> pointMap = new HashMap<>();
        pointMap.put(ctx.getString(R.string.six_spades), new int[] {40, 6});
        pointMap.put(ctx.getString(R.string.six_clubs), new int[] {60, 6});
        pointMap.put(ctx.getString(R.string.six_diamonds), new int[] {80, 6});
        pointMap.put(ctx.getString(R.string.six_hearts), new int[] {100, 6});
        pointMap.put(ctx.getString(R.string.six_notrump), new int[] {120, 6});
        pointMap.put(ctx.getString(R.string.seven_spades), new int[] {140, 7});
        pointMap.put(ctx.getString(R.string.seven_clubs), new int[] {160, 7});
        pointMap.put(ctx.getString(R.string.seven_diamonds), new int[] {180, 7});
        pointMap.put(ctx.getString(R.string.seven_hearts), new int[] {200, 7});
        pointMap.put(ctx.getString(R.string.seven_notrump), new int[] {220, 7});
        pointMap.put(ctx.getString(R.string.eight_spades), new int[] {240, 8});
        pointMap.put(ctx.getString(R.string.eight_clubs), new int[] {260, 8});
        pointMap.put(ctx.getString(R.string.eight_diamonds), new int[] {280, 8});
        pointMap.put(ctx.getString(R.string.eight_hearts), new int[] {300, 8});
        pointMap.put(ctx.getString(R.string.eight_notrump), new int[] {320, 8});
        pointMap.put(ctx.getString(R.string.nine_spades), new int[] {340, 9});
        pointMap.put(ctx.getString(R.string.nine_clubs), new int[] {360, 9});
        pointMap.put(ctx.getString(R.string.nine_diamonds), new int[] {380, 9});
        pointMap.put(ctx.getString(R.string.nine_hearts), new int[] {400, 9});
        pointMap.put(ctx.getString(R.string.nine_notrump), new int[] {420, 9});
        pointMap.put(ctx.getString(R.string.ten_spades), new int[] {440, 10});
        pointMap.put(ctx.getString(R.string.ten_clubs), new int[] {460, 10});
        pointMap.put(ctx.getString(R.string.ten_diamonds), new int[] {480, 10});
        pointMap.put(ctx.getString(R.string.ten_hearts), new int[] {500, 10});
        pointMap.put(ctx.getString(R.string.ten_notrump), new int[] {520, 10});
        pointMap.put(ctx.getString(R.string.nullo), new int[] {250, 0});
        pointMap.put(ctx.getString(R.string.open_nullo), new int[] {500, 0});
        pointMap.put(ctx.getString(R.string.double_nullo), new int[] {500, 0});
        pointMap.put("Null", new int[] {0, 0});


        bidValueAndTricksNeeded = pointMap.get(bid);
        bidValue = bidValueAndTricksNeeded[0];
        tricksNeeded = bidValueAndTricksNeeded[1];

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String nonBiddingPointsPref = prefs.getString("nonbidding_points","Always");



        if ((hand.get_teamOneTricks() == 0) && (hand.get_teamTwoTricks() == 0)) {
            scoreChange[0] = 0;
            scoreChange[1] = 0;
        } else if (tricksNeeded == 0){
            if (bidderTricksWon == 0){
                scoreChange[0] = bidValue;
                scoreChange[1] = 0;
            } else {
                scoreChange[0] = -bidValue;
                if (nonBiddingPointsPref.equals("Never")){
                    scoreChange[1] = 0;
                } else {
                    scoreChange[1] = 10*bidderTricksWon;
                }

            }
        } else {
            if (bidderTricksWon >= tricksNeeded) {
                scoreChange[0] = bidValue;
                if (nonBiddingPointsPref.equals("Always")){
                    scoreChange[1] = 100-10*bidderTricksWon;
                } else {
                    scoreChange[1] = 0;
                }


            } else {
                scoreChange[0] = -bidValue;
                if (nonBiddingPointsPref.equals("Never")){
                    scoreChange[1] = 0;
                } else {
                    scoreChange[1] = 100-10*bidderTricksWon;
                }
            }
        }

        Boolean isTenTrickBonus = prefs.getBoolean("ten_trick_bonus", false);

        if(bidderTricksWon==10 && isTenTrickBonus){
            if (scoreChange[0]<250){
                scoreChange[0]=250;
            }
        }


        return scoreChange;
    }
}
