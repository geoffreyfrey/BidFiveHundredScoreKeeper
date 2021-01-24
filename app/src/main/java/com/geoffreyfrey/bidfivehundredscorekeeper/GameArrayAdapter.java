package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geoffreyfrey.SQLite.DatabaseDAO;
import com.geoffreyfrey.SQLite.Game;
import com.geoffreyfrey.SQLite.GameDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by geoff on 6/3/16.
 */

public class GameArrayAdapter extends ArrayAdapter<Game> {
    Context ctx;
    int layoutResourceId;
    ArrayList<Game> list = new ArrayList<>();
    DatabaseDAO dbDAO = new DatabaseDAO(ctx);

    public GameArrayAdapter(Context context, int resource, ArrayList<Game> games) {
        super(context, resource, games);
        this.layoutResourceId = resource;
        this.ctx = context;
        this.list = games;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Game game = list.get(position);

        if (convertView == null){
            convertView = View.inflate(ctx, layoutResourceId, null);
        }

        TableLayout table = (TableLayout) convertView;
        table.setColumnShrinkable(3, true);
        table.removeAllViews();

        //Setting up Victories Row
        TableRow gameRow = new TableRow(ctx);
        gameRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        //Team One Win
        TextView tvTeamOneWin = createTextView(ctx, 1);
        tvTeamOneWin.setGravity(Gravity.CENTER);


        //Team One Win
        TextView tvGameInProgress = createTextView(ctx, 1);
        tvGameInProgress.setGravity(Gravity.CENTER);


        //Team Two Win
        TextView tvTeamTwoWin = createTextView(ctx, 1);
        tvTeamTwoWin.setGravity(Gravity.CENTER);

        if (game.get_winningTeamID()==0){
            tvGameInProgress.setText("In Progress");
        } else if (game.get_winningTeamID()==game.get_teamOneID()){
            tvTeamOneWin.setText("Victory!");
            tvTeamOneWin.setTextColor(Color.parseColor("#4CAF50"));
        } else if (game.get_winningTeamID()==game.get_teamTwoID()){
            tvTeamTwoWin.setText("Victory!");
            tvTeamTwoWin.setTextColor(Color.parseColor("#4CAF50"));
        }

        gameRow.addView(tvTeamOneWin);
        gameRow.addView(tvGameInProgress);
        gameRow.addView(tvTeamTwoWin);
        table.addView(gameRow);

        //Setting up score Row
        TableRow scoreRow = new TableRow(ctx);
        scoreRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        //Hands played
        TextView tvHandCounter = new TextView(ctx);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = 2;
//        params.span = 3;
        tvHandCounter.setLayoutParams(params);
        tvHandCounter.setGravity(Gravity.LEFT);
        tvHandCounter.setPadding(4, 4, 4, 4);
        tvHandCounter.setTextSize(10);
        GameDAO gameDAO = new GameDAO(ctx);
        int handsCount = gameDAO.getHandCount(game);
        String handCounterString = (handsCount + " hands played.");
        tvHandCounter.setText(handCounterString);
        scoreRow.addView(tvHandCounter);

        //team one score
        TextView teamOneScore = createTextView(ctx, 1);
        if (game.get_winningTeamID()==game.get_teamOneID()){
            teamOneScore.setTextColor(Color.parseColor("#4CAF50"));
        }
        teamOneScore.setText(Integer.toString(game.get_teamOneScore()));
        teamOneScore.setGravity(Gravity.RIGHT);
        scoreRow.addView(teamOneScore);

        //adding the to
        TextView tvMidScore = createMidTextView(ctx);
        tvMidScore.setText(" : ");
        scoreRow.addView(tvMidScore);

        //team two score
        TextView teamTwoScore = createTextView(ctx, 1);
        if (game.get_winningTeamID()==game.get_teamTwoID()){
            teamTwoScore.setTextColor(Color.parseColor("#4CAF50"));
        }
        teamTwoScore.setText(Integer.toString(game.get_teamTwoScore()));
        teamTwoScore.setGravity(Gravity.LEFT);
        scoreRow.addView(teamTwoScore);

        //Game date
        TextView tvGameDate = new TextView(ctx);
        TableRow.LayoutParams paramsDate = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        paramsDate.weight = 2;
//        params.span = 3;
        tvGameDate.setLayoutParams(paramsDate);
        tvGameDate.setGravity(Gravity.RIGHT);
        tvGameDate.setPadding(4, 4, 4, 4);
        tvGameDate.setTextSize(10);

        SimpleDateFormat dateFormatOut = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = dateFormatOut.format(game.get_gameDate());
        tvGameDate.setText(dateString);
        scoreRow.addView(tvGameDate);

        table.addView(scoreRow);


        return table;
    }

    private TextView createTextView(Context ctx, int weight){
        TextView tv = new TextView(ctx);

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = weight;
        tv.setLayoutParams(params);

        tv.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT < 23) {
            tv.setTextAppearance(ctx, R.style.MatchTableStyle);
        } else {
            tv.setTextAppearance(R.style.MatchTableStyle);
        }
        tv.setPadding(1, 1, 1, 1);

//        //Cell Outlines
//        GradientDrawable gd = new GradientDrawable();
//        gd.setStroke(1, 0xFF000000);
//        if (android.os.Build.VERSION.SDK_INT >= 16) {
//            tv.setBackground(gd);
//        } else {
//            tv.setBackgroundDrawable(gd);
//        }

        return tv;
    }

    private TextView createMidTextView(Context ctx){
        TextView tv = new TextView(ctx);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        params.weight = 0;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT < 23) {
            tv.setTextAppearance(ctx, R.style.MatchTableStyle);
        } else {
            tv.setTextAppearance(R.style.MatchTableStyle);
        }
        tv.setTypeface(null, Typeface.NORMAL);
        tv.setPadding(1, 1, 1, 1);


//        //Cell Outlines
//        GradientDrawable gd = new GradientDrawable();
//        gd.setStroke(1, 0xFF000000);
//        if (android.os.Build.VERSION.SDK_INT >= 16) {
//            tv.setBackground(gd);
//        } else {
//            tv.setBackgroundDrawable(gd);
//        }

        return tv;
    }

}
