package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.geoffreyfrey.SQLite.DatabaseDAO;
import com.geoffreyfrey.SQLite.Player;

import java.util.ArrayList;

/**
 * Created by geoff on 6/3/16.
 */

public class PlayerArrayAdapter extends ArrayAdapter<Player> {
    Context ctx;
    int layoutResourceId;
    ArrayList<Player> list = new ArrayList<>();
    DatabaseDAO dbDAO = new DatabaseDAO(ctx);

    public PlayerArrayAdapter(Context context, int resource, ArrayList<Player> players) {
        super(context, resource, players);
        this.layoutResourceId = resource;
        this.ctx = context;
        this.list = players;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Player player = list.get(position);
        DatabaseDAO dbDAO = new DatabaseDAO(ctx);

        if (convertView == null){
            convertView = View.inflate(ctx, layoutResourceId, null);
        }

        //Inflating the table layout from xml
        TableLayout table = (TableLayout) convertView;

        //Setting the textviews
        TextView tvPlayerName = (TextView) table.findViewById(R.id.player_name);
        TextView tvPlayerBids = (TextView) table.findViewById(R.id.player_bids);
        TextView tvPlayerBidsMade = (TextView) table.findViewById(R.id.player_bids_made);
        TextView tvPlayerBidsPercentage = (TextView) table.findViewById(R.id.player_bids_percentage);
        TextView tvPlayerNetPoints = (TextView) table.findViewById(R.id.player_net_score);
        TextView tvPlayerGamesPlayed = (TextView) table.findViewById(R.id.player_games_played);
        TextView tvPlayerGamesWon = (TextView) table.findViewById(R.id.player_games_won);
        TextView tvPlayerGamesPercentage = (TextView) table.findViewById(R.id.player_games_percentage);

        ImageView ivPlayerBid = (ImageView) table.findViewById(R.id.bid_scull_and_cross);
        ImageView ivPlayerGame = (ImageView) table.findViewById(R.id.game_scull_and_cross);

        //Hiding the scull and crossbones
        ivPlayerBid.setVisibility(View.INVISIBLE);
        ivPlayerGame.setVisibility(View.INVISIBLE);

        //Setting the texts
        tvPlayerName.setText(player.get_name());

        String playerBids = "Bid " + String.valueOf(player.get_bids());
        tvPlayerBids.setText(playerBids);

        String playerBidsMade = "Made " + String.valueOf(player.get_bidsWon());
        tvPlayerBidsMade.setText(playerBidsMade);

        if (player.get_bids()==0){
            tvPlayerBidsPercentage.setText("NA");
            tvPlayerBidsPercentage.setTextColor(Color.BLACK);
        } else {
            double bidPercentage = (((double) player.get_bidsWon())/player.get_bids())*100;
            String bidPercentageString = String.format("%.0f", bidPercentage) + "%";
            tvPlayerBidsPercentage.setText(bidPercentageString);

            if (bidPercentage>75){
                tvPlayerBidsPercentage.setTextColor(Color.parseColor("#388E3C"));
            } else if (bidPercentage<25){
                tvPlayerBidsPercentage.setTextColor(Color.parseColor("#B71C1C"));
            } else {
                tvPlayerBidsPercentage.setTextColor(Color.BLACK);
            }

        }

        //Setting Net Point Color and displaying Skull if necessary
        tvPlayerNetPoints.setText(String.valueOf(player.get_netPoints()));
        if (player.get_netPoints()< 0){
            tvPlayerNetPoints.setTextColor(Color.parseColor("#B71C1C"));

            if (player.get_gamesPlayed()>3){
                ivPlayerBid.setImageResource(R.drawable.skacr);
                ivPlayerBid.setVisibility(View.VISIBLE);
            }
        } else if (player.get_netPoints()> 1000){
            tvPlayerNetPoints.setTextColor(Color.parseColor("#388E3C"));
        } else {
            tvPlayerNetPoints.setTextColor(Color.BLACK);
        }

        //Choosing when to display point star (if > 250 points per game)
        if (player.get_gamesPlayed()>3) {
            double ppg = ((double) player.get_netPoints())/player.get_gamesPlayed();
            if (ppg > 250){
                ivPlayerBid.setImageResource(R.drawable.gold_star);
                ivPlayerBid.setVisibility(View.VISIBLE);
            }
        }



        String playerGamesPlayed = "Played " + String.valueOf(player.get_gamesPlayed());
        tvPlayerGamesPlayed.setText(playerGamesPlayed);

        String playerGamesWon = "Won " + String.valueOf(player.get_gamesWon());
        tvPlayerGamesWon.setText(playerGamesWon);

        double gamePercentage = (((double) player.get_gamesWon())/player.get_gamesPlayed())*100;

        if (player.get_gamesPlayed()==0){
            tvPlayerGamesPercentage.setText("NA");
            tvPlayerGamesPercentage.setTextColor(Color.BLACK);
        } else {
            String gamePercentageString = String.format("%.0f", gamePercentage) + "%";
            tvPlayerGamesPercentage.setText(gamePercentageString);

            if (gamePercentage<=35) {
                tvPlayerGamesPercentage.setTextColor(Color.parseColor("#B71C1C"));

            } else if (gamePercentage>=65){
                tvPlayerGamesPercentage.setTextColor(Color.parseColor("#388E3C"));
            } else {
                tvPlayerGamesPercentage.setTextColor(Color.BLACK);
            }
        }

        //Displaying Graphics if needed
        if (player.get_gamesPlayed()>3) {
            if (gamePercentage<=20) {
                ivPlayerGame.setImageResource(R.drawable.skacr);
                ivPlayerGame.setVisibility(View.VISIBLE);
            } else if (gamePercentage >= 80) {
                ivPlayerGame.setImageResource(R.drawable.gold_star);
                ivPlayerGame.setVisibility(View.VISIBLE);
            }
        }

        return table;
    }
}
