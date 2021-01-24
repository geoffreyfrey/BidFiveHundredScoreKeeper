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
import android.widget.ImageView;
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

public class TeamArrayAdapter extends ArrayAdapter<Team> {
    Context ctx;
    int layoutResourceId;
    ArrayList<Team> list = new ArrayList<>();
    DatabaseDAO dbDAO = new DatabaseDAO(ctx);

    public TeamArrayAdapter(Context context, int resource, ArrayList<Team> teams) {
        super(context, resource, teams);
        this.layoutResourceId = resource;
        this.ctx = context;
        this.list = teams;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Team team = list.get(position);
        DatabaseDAO dbDAO = new DatabaseDAO(ctx);

        if (convertView == null){
            convertView = View.inflate(ctx, layoutResourceId, null);
        }

        //Inflating the table layout from xml
        TableLayout table = (TableLayout) convertView;

        //Setting the Bidder,Dealer,and Bid Text
        TextView tvPlayerOne = (TextView) table.findViewById(R.id.team_player_one_name);
        TextView tvGamesPlayed = (TextView) table.findViewById(R.id.team_games_played);
        TextView tvGamesWon = (TextView) table.findViewById(R.id.team_games_won);
        TextView tvWinPercentage = (TextView) table.findViewById(R.id.team_win_percentage);


        String playerNames = team.get_playerOneName() + " & " + team.get_playerTwoName();

        //Setting the text
        tvPlayerOne.setText(playerNames);
//        tvPlayerTwo.setText(team.get_playerTwoName());
        tvGamesPlayed.setText(String.valueOf(team.get_gamesPlayed()));
        tvGamesWon.setText(String.valueOf(team.get_gamesWon()));

        double percentage = ((double) team.get_gamesWon())/team.get_gamesPlayed()*100;
        String winPercentage = String.format("%.0f", percentage) + "%";
        tvWinPercentage.setText(winPercentage);
        tvWinPercentage.setTextColor(Color.BLACK);
        tvWinPercentage.setTypeface(Typeface.DEFAULT);

        if (percentage < 35) {
            tvWinPercentage.setTextColor(Color.parseColor("#F44336"));
        } else if (percentage > 65){
            tvWinPercentage.setTextColor(Color.parseColor("#4CAF50"));
        }

        ImageView ivRainMan = (ImageView) table.findViewById(R.id.team_rm);
        ImageView ivZachGal = (ImageView) table.findViewById(R.id.team_zg);

        ivRainMan.setVisibility(View.INVISIBLE);
        ivZachGal.setVisibility(View.INVISIBLE);

        if (team.get_gamesPlayed()>3){

            if (percentage <= 20) {
                ivRainMan.setImageResource(R.drawable.skacr);
                ivZachGal.setImageResource(R.drawable.skacr);
                ivRainMan.setVisibility(View.VISIBLE);
                ivZachGal.setVisibility(View.VISIBLE);
            }

            if (percentage >= 80) {
                ivRainMan.setImageResource(R.drawable.rmcards);
                ivZachGal.setImageResource(R.drawable.zgcc2);
                ivRainMan.setVisibility(View.VISIBLE);
                ivZachGal.setVisibility(View.VISIBLE);
            }
        }

        return table;
    }
}
