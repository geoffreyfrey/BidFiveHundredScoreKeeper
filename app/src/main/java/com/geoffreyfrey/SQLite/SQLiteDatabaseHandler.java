package com.geoffreyfrey.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by geoff on 5/26/16.
 */

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    //Declaring variables
    //Setting up the database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "five_hundred.db";

    //Player Table
    public static final String PLAYERS_TABLE = "players_table";
    public static final String PLAYER_ID = "p_id";
    public static final String PLAYER_NAME = "name";
    public static final String PLAYER_GAMES_PLAYED = "pgames_played";
    public static final String PLAYER_FOURH_GAMES_WON = "games_won";
    public static final String PLAYER_BIDS_QUANTITY = "bids";
    public static final String PLAYER_BIDS_WON = "bids_won";
    public static final String PLAYER_NET_POINTS = "net_points";
    public static final String PLAYER_DELETED = "deleted";

    //Teams Table
    public static final String TEAMS_TABLE = "teams_table";
    public static final String TEAM_ID = "t_id";
    public static final String PLAYER_ONE_NAME = "p1_name";
    public static final String PLAYER_TWO_NAME = "p2_name";
    public static final String GAMES_PLAYED = "tgames_played";
    public static final String VICTORIES = "victories";

    //Four Handed Matches Table
    public static final String FOURH_MATCHES_TABLE = "matches_table";
    public static final String FOURH_MATCH_ID = "m_id";
    public static final String TEAM_ONE_ID = "t1_id";
    public static final String TEAM_TWO_ID = "t2_id";
    public static final String TEAM_ONE_VICTORIES = "t1_wins";
    public static final String TEAM_TWO_VICTORIES = "t2_wins";
    public static final String MATCH_DATE = "m_date";
    public static final String PLAYER_ONE_ID = "p1_id";
    public static final String PLAYER_TWO_ID = "p2_id";
    public static final String PLAYER_THREE_ID = "p3_id";
    public static final String PLAYER_FOUR_ID = "p4_id";
    public static final String LAST_GAME_ID = "lg_id";

    //Four Handed Games Table
    public static final String FOURH_GAMES_TABLE = "games_table";
    public static final String FOURH_GAME_ID = "fg_id";
    //public static final String FOURH_MATCH_ID = "match_id";
    //public static final String TEAM_ONE_ID = "team_one_id";
    //public static final String TEAM_TWO_ID = "team_two_id";
    public static final String TEAM_ONE_SCORE = "t1_score";
    public static final String TEAM_TWO_SCORE = "t2_score";
    public static final String WINNING_TEAM_ID = "win_tid";
    public static final String GAME_DATE = "g_date";
    public static final String LAST_HAND_ID = "lh_id";

    //Four Handed Hands Table
    public static final String FOURH_HANDS_TABLE = "hands_table";
    public static final String FOURH_HANDS_ID = "h_id";
    //public static final String FOURH_GAME_ID = "fourh_game_id";
    //public static final String FOURH_MATCH_ID = "match_id";
    //public static final String TEAM_ONE_ID = "team_one_id";
    //public static final String TEAM_TWO_ID = "team_two_id";
    public static final String DEALER_ID = "d_id";
    public static final String BIDDER_ID = "b_id";
    public static final String BID = "bid";
    public static final String PREVIOUS_HAND_ID = "ph_id";
    public static final String TEAM_ONE_TRICKS_WON = "t1_t";
    public static final String TEAM_TWO_TRICKS_WON = "t2_t";
    public static final String TEAM_ONE_SCORE_CHANGE = "t1_sc";
    public static final String TEAM_TWO_SCORE_CHANGE = "t2_sc";
    public static final String TEAM_ONE_FINAL_SCORE = "t1_fs";
    public static final String TEAM_TWO_FINAL_SCORE = "t2_fs";
    public static final String HAND_COMPLETE = "end";
//    public static final String END_OF_GAME = "end_of_game";

    //Setting the create tables strings
    private static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + PLAYERS_TABLE + " ("
            + PLAYER_ID + " INTEGER PRIMARY KEY, "
            + PLAYER_NAME + " TEXT, "
            + PLAYER_GAMES_PLAYED + " INT, "
            + PLAYER_FOURH_GAMES_WON + " INT, "
            + PLAYER_BIDS_QUANTITY + " INT, "
            + PLAYER_BIDS_WON + " INT, "
            + PLAYER_NET_POINTS + " INT, "
            + PLAYER_DELETED + " INT" + ")";

    private static final String CREATE_TEAMS_TABLE = "CREATE TABLE " + TEAMS_TABLE + " ("
            + TEAM_ID + " INTEGER PRIMARY KEY, "
            + PLAYER_ONE_NAME + " TEXT, "
            + PLAYER_TWO_NAME + " TEXT, "
            + GAMES_PLAYED + " INT, "
            + VICTORIES + " INT"
//            + VICTORIES + " INT, "
//            + "FOREIGN KEY(" + PLAYER_ONE_NAME + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_NAME + "), "
//            + "FOREIGN KEY(" + PLAYER_TWO_NAME + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_NAME + ")"
            + ")";

    private static final String CREATE_FOURH_MATCHES_TABLE = "CREATE TABLE " + FOURH_MATCHES_TABLE + " ("
            + FOURH_MATCH_ID + " INTEGER PRIMARY KEY, "
            + TEAM_ONE_ID + " INT, "
            + TEAM_TWO_ID + " INT, "
            + TEAM_ONE_VICTORIES + " INT, "
            + TEAM_TWO_VICTORIES + " INT, "
            + MATCH_DATE + " DATE DEFAULT (datetime('now','localtime')), "
            + PLAYER_ONE_ID + " INT, "
            + PLAYER_TWO_ID + " INT, "
            + PLAYER_THREE_ID + " INT, "
            + PLAYER_FOUR_ID + " INT, "
            + LAST_GAME_ID + " INT, "
            + "FOREIGN KEY(" + TEAM_ONE_ID + ") REFERENCES " + TEAMS_TABLE + "(" + TEAM_ID + "), "
            + "FOREIGN KEY(" + TEAM_TWO_ID + ") REFERENCES " + TEAMS_TABLE + "(" + TEAM_ID + "), "
            + "FOREIGN KEY(" + PLAYER_ONE_ID + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_ID + "), "
            + "FOREIGN KEY(" + PLAYER_TWO_ID + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_ID + "), "
            + "FOREIGN KEY(" + PLAYER_THREE_ID + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_ID + "), "
            + "FOREIGN KEY(" + PLAYER_FOUR_ID + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_ID + ")" + ")";


    private static final String CREATE_FOURH_GAMES_TABLE = "CREATE TABLE " + FOURH_GAMES_TABLE + " ("
            + FOURH_GAME_ID + " INTEGER PRIMARY KEY, "
            + FOURH_MATCH_ID + " INT, "
            + TEAM_ONE_ID + " INT, "
            + TEAM_TWO_ID + " INT, "
            + TEAM_ONE_SCORE + " INT, "
            + TEAM_TWO_SCORE + " INT, "
            + WINNING_TEAM_ID + " INT, "
            + GAME_DATE + " DATE DEFAULT (datetime('now','localtime')), "
            + LAST_HAND_ID + " INT, "
            + "FOREIGN KEY(" + FOURH_MATCH_ID + ") REFERENCES " + FOURH_MATCHES_TABLE + "(" + FOURH_MATCH_ID + "), "
            + "FOREIGN KEY(" + TEAM_ONE_ID + ") REFERENCES " + TEAMS_TABLE + "(" + TEAM_ID + "), "
            + "FOREIGN KEY(" + TEAM_TWO_ID + ") REFERENCES " + TEAMS_TABLE + "(" + TEAM_ID + ")" + ")";

    private static final String CREATE_FOURH_HANDS_TABLE = "CREATE TABLE " + FOURH_HANDS_TABLE + " ("
            + FOURH_HANDS_ID + " INTEGER PRIMARY KEY, "
            + FOURH_GAME_ID + " INT, "
            + FOURH_MATCH_ID + " INT, "
            + TEAM_ONE_ID + " INT, "
            + TEAM_TWO_ID + " INT, "
            + DEALER_ID + " INT, "
            + BIDDER_ID + " INT, "
            + BID + " TEXT, "
            + PREVIOUS_HAND_ID + " INT, "
            + TEAM_ONE_TRICKS_WON + " INT, "
            + TEAM_TWO_TRICKS_WON + " INT, "
            + TEAM_ONE_SCORE_CHANGE + " INT, "
            + TEAM_TWO_SCORE_CHANGE + " INT, "
            + TEAM_ONE_FINAL_SCORE + " INT, "
            + TEAM_TWO_FINAL_SCORE + " INT, "
            + HAND_COMPLETE + " INT, "
//            + END_OF_GAME + " INT, "
            + "FOREIGN KEY(" + FOURH_GAME_ID + ") REFERENCES " + FOURH_GAMES_TABLE + "(" + FOURH_GAME_ID + "), "
            + "FOREIGN KEY(" + FOURH_MATCH_ID + ") REFERENCES " + FOURH_MATCHES_TABLE + "(" + FOURH_MATCH_ID + "), "
            + "FOREIGN KEY(" + TEAM_ONE_ID + ") REFERENCES " + TEAMS_TABLE + "(" + TEAM_ID + "), "
            + "FOREIGN KEY(" + TEAM_TWO_ID + ") REFERENCES " + TEAMS_TABLE + "(" + TEAM_ID + "), "
            + "FOREIGN KEY(" + DEALER_ID + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_ID + "), "
            + "FOREIGN KEY(" + BIDDER_ID + ") REFERENCES " + PLAYERS_TABLE + "(" + PLAYER_ID + ")"+ ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PLAYERS_TABLE + ", "
            + TEAMS_TABLE + ", "
            + FOURH_MATCHES_TABLE + ", "
            + FOURH_GAMES_TABLE + ", "
            + FOURH_HANDS_TABLE;

    private static SQLiteDatabaseHandler instance;

    public static synchronized  SQLiteDatabaseHandler getHandler(Context ctx){
        if (instance == null)
            instance = new SQLiteDatabaseHandler(ctx);
        return instance;
    }

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if(!db.isReadOnly()){
            //Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLAYER_TABLE);
        db.execSQL(CREATE_TEAMS_TABLE);
        db.execSQL(CREATE_FOURH_MATCHES_TABLE);
        db.execSQL(CREATE_FOURH_GAMES_TABLE);
        db.execSQL(CREATE_FOURH_HANDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //This will discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Print out database column as a Arraylist
    public ArrayList<String> dbColumnToArrayList(String tableName, String columnName){

        //Setting up variables
        ArrayList<String> arrayList= new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + tableName + " WHERE 1";

        //Cursor to point to location in your results
        Cursor c = db.rawQuery(query, null);

        //Move to the first row in your results
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(columnName))!= null){
                arrayList.add(c.getString(c.getColumnIndex(columnName)));
            }
            c.moveToNext();
        }

        db.close();
        return arrayList;
    }

    public ArrayList<String> getTableNames(){

        String query = "SELECT * FROM sqlite_master WHERE type='table'";

        //Variable to return
        ArrayList<String> list = new ArrayList<String>();

        //Opening Database
        SQLiteDatabase db = getWritableDatabase();

        //Setting Up Cursor
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()){
                list.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return list;
    }

    public ArrayList<String> getColumnNames(String tableName){

        ArrayList<String> list = new ArrayList<String>();

        String query = "SELECT * FROM " + tableName;

        //Opening Database
        SQLiteDatabase db = getReadableDatabase();

        //Setting up Cursor
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getColumnCount();

        if (cursor!=null){
            if(cursor.moveToFirst()){
                for(int i=0;i<count;i++){
                    list.add(cursor.getColumnName(i));
                }
            }
        }

        cursor.close();
        db.close();
        return list;
    }

    //Creates the table for the Database Explorer
    public TableLayout makeTable(Context ctx, String tableName, TableLayout table){

        String query = "SELECT * FROM " + tableName;

        //Opening Database
        SQLiteDatabase db = getReadableDatabase();

        //Setting up Cursor
        Cursor cursor = db.rawQuery(query, null);

        //Cell Outlines
        GradientDrawable gd = new GradientDrawable();
        gd.setStroke(1, 0xFF000000);

        int cCount = cursor.getColumnCount();
        int rCount = cursor.getCount();

        cursor.moveToFirst();

        //First making the column titles
        if(cursor!=null){
            table.removeAllViews();
            TableRow row = new TableRow(ctx);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < cCount; j++) {
                //Setting up text View
                TextView tv = new TextView(ctx);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setPadding(1, 1, 1, 1);
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    tv.setBackground(gd);
                } else {
                    tv.setBackgroundDrawable(gd);
                }
                tv.setTypeface(null, Typeface.BOLD);

                //Inputting values
                tv.setText(cursor.getColumnName(j));
                row.addView(tv);
            }
            table.addView(row);
        }

        //Then making the content
        cursor.moveToFirst();
        if(cursor!=null) {
            for (int i = 0; i < rCount; i++) {
                TableRow row = new TableRow(ctx);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                //Outer Loop
                for (int j = 0; j < cCount; j++) {
                    //Setting up Text View
                    TextView tv = new TextView(ctx);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(1, 1, 1, 1);
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        tv.setBackground(gd);
                    } else {
                        tv.setBackgroundDrawable(gd);
                    }

                    //Inputting values
                    tv.setText(cursor.getString(j));
                    row.addView(tv);

                }
                cursor.moveToNext();
                table.addView(row);
            }
        }

        cursor.close();
        db.close();
        return table;
    }

}
