package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geoffreyfrey.SQLite.DatabaseDAO;
import com.geoffreyfrey.SQLite.Match;
import com.geoffreyfrey.SQLite.Team;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by geoff on 6/3/16.
 */

public class MatchArrayAdapter extends ArrayAdapter<Match> {
    Context ctx;
    int layoutResourceId;
    ArrayList<Match> list = new ArrayList<>();
    DatabaseDAO dbDAO = new DatabaseDAO(ctx);

    public MatchArrayAdapter(Context context, int resource, ArrayList<Match> matches) {
        super(context, resource, matches);
        this.layoutResourceId = resource;
        this.ctx = context;
        this.list = matches;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Match match = list.get(position);

        if (convertView == null){
            convertView = View.inflate(ctx, layoutResourceId, null);
        }

        TableLayout table = (TableLayout) convertView;
        table.setColumnStretchable(0, true);
        table.setColumnStretchable(2, true);
        table.setColumnShrinkable(1, true);
        table.setPadding(4, 4, 4, 4);
        table.removeAllViews();

        //Setting up the player names row
        TableRow playersRow = new TableRow(ctx);
        playersRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        Team team1 = dbDAO.getTeam(match.get_teamOneID());
        Team team2 = dbDAO.getTeam(match.get_teamTwoID());

        //Adding first team names
        TextView tv1 = createTextView(ctx, 1);
        tv1.setGravity(Gravity.RIGHT);
        tv1.setText(team1.get_playerOneName() + " & " + team1.get_playerTwoName());
        playersRow.addView(tv1);

        //Adding the vs.
        TextView tvMidTeam = createMidTextView(ctx);
        tvMidTeam.setText(" vs. ");
        playersRow.addView(tvMidTeam);

        //Adding the second team name
        TextView tv2 = createTextView(ctx, 1);
        tv2.setGravity(Gravity.LEFT);
        tv2.setText(team2.get_playerOneName() + " & " + team2.get_playerTwoName());
        playersRow.addView(tv2);

        table.addView(playersRow);

        //Setting up Victories Row
        TableRow scoreRow = new TableRow(ctx);
        scoreRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        //Team one game wins
        TextView tv3 = createTextView(ctx, 3);
        tv3.setGravity(Gravity.RIGHT);
        tv3.setText(Integer.toString(match.get_teamOneVictories()));
        scoreRow.addView(tv3);

        //adding the to
        TextView tvMidScore = createMidTextView(ctx);
        tvMidScore.setText(" to ");
        scoreRow.addView(tvMidScore);

        //Team two game wins
        TextView tv4 = createTextView(ctx, 1);
        tv4.setGravity(Gravity.LEFT);
        tv4.setText(Integer.toString(match.get_teamTwoVictories()));
        scoreRow.addView(tv4);


        TextView tvMatchDate = new TextView(ctx);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
        params.weight = 2;
//        params.span = 3;
        tvMatchDate.setLayoutParams(params);
        tvMatchDate.setGravity(Gravity.RIGHT);
        tvMatchDate.setPadding(1, 1, 1, 1);
        tvMatchDate.setTextSize(10);

        SimpleDateFormat dateFormatOut = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = dateFormatOut.format(match.get_matchDate());
        tvMatchDate.setText(dateString);
        scoreRow.addView(tvMatchDate);

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
