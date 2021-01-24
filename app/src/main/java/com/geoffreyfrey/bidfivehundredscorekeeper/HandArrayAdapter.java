package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geoffreyfrey.SQLite.DatabaseDAO;
import com.geoffreyfrey.SQLite.Game;
import com.geoffreyfrey.SQLite.Hand;
import com.geoffreyfrey.SQLite.Match;
import com.geoffreyfrey.SQLite.Player;
import com.geoffreyfrey.SQLite.Team;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by geoff on 6/3/16.
 */

public class HandArrayAdapter extends ArrayAdapter<Hand> {
    Context ctx;
    int layoutResourceId;
    ArrayList<Hand> list = new ArrayList<>();
    DatabaseDAO dbDAO = new DatabaseDAO(ctx);

    public HandArrayAdapter(Context context, int resource, ArrayList<Hand> hands) {
        super(context, resource, hands);
        this.layoutResourceId = resource;
        this.ctx = context;
        this.list = hands;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Hand hand = list.get(position);
        DatabaseDAO dbDAO = new DatabaseDAO(ctx);

        if (convertView == null){
            convertView = View.inflate(ctx, layoutResourceId, null);
        }

        //Inflating the table layout from xml
        TableLayout table = (TableLayout) convertView;

        //Pulling the dealer and bidder data
        Player dealer = dbDAO.getPlayerByID(hand.get_dealer());
        Player bidder = dbDAO.getPlayerByID(hand.get_bidder());

        //Pulling the teams from the hand data
        Team teamOne = dbDAO.getTeam(hand.get_teamOneID());
        Team teamTwo = dbDAO.getTeam(hand.get_teamTwoID());
        int biddingTeamID;

        //Grabbing the game data
        Game game = dbDAO.getGame(hand.get_gameID());

        //Setting the Bidder,Dealer,and Bid Text
        TextView teamOneBidder = (TextView) table.findViewById(R.id.team_one_bidder);
        TextView tvDealer = (TextView) table.findViewById(R.id.dealer_text);
        TextView teamTwoBidder = (TextView) table.findViewById(R.id.team_two_bidder);
        TextView tvTeamOneBid = (TextView) table.findViewById(R.id.team_one_bid);
        TextView tvTeamTwoBid = (TextView) table.findViewById(R.id.team_two_bid);

        String bidderText = (bidder.get_name() + " bid:");
        String dealerText = ("Dealer: " + dealer.get_name());

        String openNullo = ctx.getResources().getString(R.string.open_nullo);
        String doubleNullo = ctx.getResources().getString(R.string.double_nullo);

        if (teamOne.isPlayerOnTeamByName(bidder.get_name())){
            biddingTeamID = teamOne.get_id();
            teamOneBidder.setText(bidderText);
            teamTwoBidder.setText("");
            tvTeamOneBid.setText(hand.get_bid());
            if (hand.get_bid().contains("\u2666") || hand.get_bid().contains("\u2665")) {
                tvTeamOneBid.setTextColor(Color.parseColor("#F44336"));
            } else if ((hand.get_bid().contains(doubleNullo)) || (hand.get_bid().contains(openNullo))) {
                tvTeamOneBid.setTextSize(16);
            }
            tvTeamTwoBid.setText("");
        } else if (teamTwo.isPlayerOnTeamByName(bidder.get_name())) {
            biddingTeamID = teamTwo.get_id();
            teamOneBidder.setText("");
            teamTwoBidder.setText(bidderText);
            tvTeamOneBid.setText("");
            tvTeamTwoBid.setText(hand.get_bid());
            if (hand.get_bid().contains("\u2666") || hand.get_bid().contains("\u2665")) {
                tvTeamTwoBid.setTextColor(Color.parseColor("#F44336"));
            } else if ((hand.get_bid().contains(doubleNullo)) || (hand.get_bid().contains(openNullo))) {
                tvTeamTwoBid.setTextSize(14);
            }
        } else {
            biddingTeamID = 0;
            teamOneBidder.setText("");
            teamTwoBidder.setText("");
            tvTeamOneBid.setText("");
            tvTeamTwoBid.setText("");
        }
        tvDealer.setText(dealerText);

        //Setting up the tricks won and score changes
        TextView tvTeamOneTricks = (TextView) table.findViewById(R.id.team_one_tricks);
        TextView tvTeamTwoTricks = (TextView) table.findViewById(R.id.team_two_tricks);
        TextView tvTeamOneScoreChange = (TextView) table.findViewById(R.id.team_one_score_change);
        TextView tvTeamTwoScoreChange = (TextView) table.findViewById(R.id.team_two_score_change);

        String teamOneTricksString;
        String teamTwoTricksString;

        if (hand.get_teamOneTricks()==0 && hand.get_teamTwoTricks()==0){
            teamOneTricksString = ("");
            teamTwoTricksString = ("");
        } else {
            teamOneTricksString = ("Won " + hand.get_teamOneTricks() + " tricks");
            teamTwoTricksString = ("Won " + hand.get_teamTwoTricks() + " tricks");
        }

        tvTeamOneTricks.setText(teamOneTricksString);
        tvTeamTwoTricks.setText(teamTwoTricksString);

        if (biddingTeamID==teamOne.get_id()){
            if (hand.get_teamOneScoreChange() >= 0){
                String teamOneScoreChangeString = ("+" + String.valueOf(hand.get_teamOneScoreChange()));
                tvTeamOneScoreChange.setText(teamOneScoreChangeString);
                tvTeamOneScoreChange.setTextColor(Color.parseColor("#4CAF50"));
            } else {
                String teamOneScoreChangeString = (String.valueOf(hand.get_teamOneScoreChange()));
                tvTeamOneScoreChange.setText(teamOneScoreChangeString);
                tvTeamOneScoreChange.setTextColor(Color.parseColor("#F44336"));
            }
            if (hand.get_teamTwoScoreChange() == 0){
                String teamTwoScoreChangeString = (String.valueOf(hand.get_teamTwoScoreChange()));
                tvTeamTwoScoreChange.setText(teamTwoScoreChangeString);
            } else {
                String teamTwoScoreChangeString = ("+" + String.valueOf(hand.get_teamTwoScoreChange()));
                tvTeamTwoScoreChange.setText(teamTwoScoreChangeString);
            }
        } else if (biddingTeamID==teamTwo.get_id()){
            if (hand.get_teamTwoScoreChange() >= 0){
                String teamTwoScoreChangeString = ("+" + String.valueOf(hand.get_teamTwoScoreChange()));
                tvTeamTwoScoreChange.setText(teamTwoScoreChangeString);
                tvTeamTwoScoreChange.setTextColor(Color.parseColor("#4CAF50"));
            } else {
                String teamTwoScoreChangeString = (String.valueOf(hand.get_teamTwoScoreChange()));
                tvTeamTwoScoreChange.setText(teamTwoScoreChangeString);
                tvTeamTwoScoreChange.setTextColor(Color.parseColor("#F44336"));
            }
            if (hand.get_teamOneScoreChange() == 0){
                String teamOneScoreChangeString = (String.valueOf(hand.get_teamOneScoreChange()));
                tvTeamTwoScoreChange.setText(teamOneScoreChangeString);
            } else {
                String teamOneScoreChangeString = ("+" + String.valueOf(hand.get_teamOneScoreChange()));
                tvTeamOneScoreChange.setText(teamOneScoreChangeString);
            }
        } else {
            tvTeamOneScoreChange.setText("");
            tvTeamTwoScoreChange.setText("");
        }



        //Setting team one's score
        TextView teamOneScore = (TextView) table.findViewById(R.id.team_one_score);
        teamOneScore.setText(String.valueOf(hand.get_teamOneEndingScore()));

        //Setting team two's score
        TextView teamTwoScore = (TextView) table.findViewById(R.id.team_two_score);
        teamTwoScore.setText(String.valueOf(hand.get_teamTwoEndingScore()));

        if(hand.get_teamOneEndingScore()==0 && hand.get_teamTwoEndingScore()==0){
            teamOneScore.setText("");
            teamTwoScore.setText("");

            TextView tvCenterSpacer = (TextView) table.findViewById(R.id.center_spacer);
            tvCenterSpacer.setTextSize(16);
            tvCenterSpacer.setTypeface(null, Typeface.BOLD);
            tvCenterSpacer.setText(ctx.getResources().getString(R.string.incomplete_hand_text));
            GameActivity.lastHandComplete = false;
        }

//        if (hand.get_id() != game.get_lastHandID()){
//            teamOneScore.setPaintFlags(teamOneScore.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            teamTwoScore.setPaintFlags(teamTwoScore.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }

//        //Setting the Victory Text if Appropriate
//        TextView teamOneVictory = (TextView) table.findViewById(R.id.team_one_victory);
//        TextView teamTwoVictory = (TextView) table.findViewById(R.id.team_two_victory);
//
//        String victory = "Victory!";
//
//        Log.v("position", String.valueOf(position));
//        Log.v("listSize", String.valueOf(list.size()));
//
        if ((position)==(list.size()-1)){
            if(game.get_winningTeamID()==game.get_teamOneID()){
                teamOneScore.setTextColor(Color.parseColor("#4CAF50"));
                teamTwoScore.setTextColor(Color.parseColor("#F44336"));
            } else if (game.get_winningTeamID()==game.get_teamTwoID()) {
                teamOneScore.setTextColor(Color.parseColor("#F44336"));
                teamTwoScore.setTextColor(Color.parseColor("#4CAF50"));
            }
        }

        return table;
    }
}
