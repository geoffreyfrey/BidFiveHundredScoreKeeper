package com.geoffreyfrey.bidfivehundredscorekeeper;

/**
 * Created by geoff on 6/20/16.
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.geoffreyfrey.SQLite.DatabaseDAO;
import com.geoffreyfrey.SQLite.DatabaseExplorer;
import com.geoffreyfrey.SQLite.Game;
import com.geoffreyfrey.SQLite.GameDAO;
import com.geoffreyfrey.SQLite.Hand;
import com.geoffreyfrey.SQLite.HandDAO;
import com.geoffreyfrey.SQLite.Match;
import com.geoffreyfrey.SQLite.Player;
import com.geoffreyfrey.SQLite.Team;

public class HandActivity extends AppCompatActivity {

    //Declaring the views
    SeekBar seekBar;
    TextView teamOneTricks;
    TextView teamTwoTricks;
    TextView teamOneScoreChangeTextView;
    TextView teamTwoScoreChangeTextView;
    TextView teamOnePointsTitle;
    TextView teamTwoPointsTitle;

    DatabaseDAO dbDAO;
    Match match;
    Game game;
    GameDAO gameDAO;
    HandDAO handDAO;
    Hand hand;
    Hand prevHand;
    Hand originalHand;
    Player dealer;
    Team teamOne;
    Team teamTwo;

    int[] scoreChanges = new int[2];
    int biddingTeamID;
    String bidderName;
    int bidderID;
    String bid;
    boolean hasPrevHand;
    int teamOneScoreChange;
    int teamTwoScoreChange;


    //Creates the layout including the toolbar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbarText.setText("HAND INPUTS");
        setSupportActionBar(myToolbar);

        //Pulling match, game, and hand info from the intent
        gameDAO = new GameDAO(this);
        Bundle extras = getIntent().getExtras();
        final int handID = extras.getInt("handID");
        dbDAO = new DatabaseDAO(this);
        handDAO = new HandDAO(this);
        hand = dbDAO.getHand(handID);
        originalHand = handDAO.copyHand(hand);
        match = dbDAO.getMatch(hand.get_matchID());
        game = dbDAO.getGame(hand.get_gameID());
        teamOne = dbDAO.getTeam(match.get_teamOneID());
        teamTwo = dbDAO.getTeam(match.get_teamTwoID());

        //Getting previous hand if there is one.
        if ((game.get_lastHandID() != 0) && (game.get_lastHandID() != hand.get_id())){
            prevHand = dbDAO.getHand(game.get_lastHandID());
            hand.set_previousHandID(prevHand.get_id());
            hasPrevHand = true;

        } else if (game.get_lastHandID() != 0){
            if (hand.get_previousHandID()==0) {
                hasPrevHand = false;

            } else {
                prevHand = dbDAO.getHand(hand.get_previousHandID());
                hasPrevHand = true;
            }

        } else {
            hasPrevHand = false;
        }


        //Calling function to determine the dealer.
        setupDealer();

        //Calling function to setup the player names.
        setupPlayers();

        //If there is a current bid, this checks the correct bid box.
        setupBid();

        //Calling function to setup the point keeping section
        setupPointKeeping();

//        //Setting up the Up Arrow Navigation
//        myToolbar.setNavigationIcon(R.drawable.up_arrow);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent upIntent = NavUtils.getParentActivityIntent(HandActivity.this);
//                int gameID = hand.get_gameID();
//                upIntent.putExtra("gameID", gameID);
//                NavUtils.navigateUpTo(HandActivity.this, upIntent);
//            }
//        });

        //Setting up the SeekBar
        setupSeekBar();

        //hiding the six bids if user preference doesn't allow them
        setupSixBidRow();

        updatePoints();
    }

    @Override
    public void onBackPressed(){
        //Make sure that the dealer is set:
        handDAO.updateHandDealer(hand);

        Intent intent = new Intent(HandActivity.this, GameActivity.class);
        intent.putExtra("gameID", hand.get_gameID());
        startActivity(intent);

//        //if the file has been edited, give the user a chance to decide to save or not
//
//        if((hand.get_bidder()==0) && hand.get_bid().equals("Null")) {
//            //if there is neither a bid nor a bidder input, it deletes automatically.
//            handDAO = new HandDAO(HandActivity.this);
//            handDAO.deleteHand(hand.get_id());
//            Intent intent = new Intent(HandActivity.this, GameActivity.class);
//            intent.putExtra("gameID", hand.get_gameID());
//            startActivity(intent);
//
//        } else {
//            //If the original was a completed hand, we bring up the options to save the changes or not.
//            new AlertDialog.Builder(this)
//                    .setTitle("Save any changes to hand?")
//                    .setMessage("You can use the OK button at the bottom of the screen to save as well.")
//                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
//                        public void onClick(DialogInterface dialogInterface, int i){
//                            Intent intent = new Intent(HandActivity.this, GameActivity.class);
//                            intent.putExtra("gameID", hand.get_gameID());
//                            startActivity(intent);
//                        }
//                    })
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Button button = (Button) findViewById(R.id.hand_ok_button);
//                            onOkClick(button);
//                            Intent intent = new Intent(HandActivity.this, GameActivity.class);
//                            intent.putExtra("gameID", hand.get_gameID());
//                            startActivity(intent);
//                        }
//                    }).create().show();
//        }
    }

    //Method to set up the dealer information
    public void setupDealer(){
        //Getting the dealer information
        int dealerID = determineDealerID(match, hand);
        hand.set_dealer(dealerID);
        dealer = dbDAO.getPlayerByID(dealerID);
        String dealerName = dealer.get_name();

        //Finding the text view and setting the text
        TextView dealerTextView = (TextView) findViewById(R.id.dealer);
        dealerTextView.setText("Dealer: " + dealerName);
    }

    //Method to determine the dealer
    public int determineDealerID(Match match, Hand hand){
        int dealerID = hand.get_dealer();
        int prevHandID = hand.get_previousHandID();
        int prevDealerID;
        Game prevGame;

        Hand prevHand = dbDAO.getHand(prevHandID);

        //If dealer ID isn't set yet, it will be 0 and need to be set.
        if (dealerID == 0){

            //If there are no previous hands, player one is the dealer
            if (prevHandID == 0){
                if (match.get_lastGameID()==0){
                    dealerID = match.get_playerOneID();
                } else {
                    prevGame = dbDAO.getGame(match.get_lastGameID());
                    prevDealerID = gameDAO.getFirstDealer(prevGame);
                    dealerID = match.getNextDealer(prevDealerID);
                }

            } else {
                //If there is a previous hand, then the dealer needs to be determine based on the previous hand.
                dealerID = match.getNextDealer(prevHand.get_dealer());
            }
        }
        return dealerID;
    }

    //Method to setup the players information
    public void setupPlayers(){
        //Setting up the Buttons
        ToggleButton playerOneButton = (ToggleButton) findViewById(R.id.player_one_button);
        ToggleButton playerTwoButton = (ToggleButton) findViewById(R.id.player_two_button);
        ToggleButton playerThreeButton = (ToggleButton) findViewById(R.id.player_three_button);
        ToggleButton playerFourButton = (ToggleButton) findViewById(R.id.player_four_button);
//
//        Player playerOne = dbDAO.getPlayerByID(match.get_playerOneID());
//        Player playerTwo = dbDAO.getPlayerByID(match.get_playerTwoID());
//        Player playerThree = dbDAO.getPlayerByID(match.get_playerThreeID());
//        Player playerFour = dbDAO.getPlayerByID(match.get_playerFourID());

        Player playerOne = dbDAO.getPlayerByName(teamOne.get_playerOneName());
        Player playerTwo = dbDAO.getPlayerByName(teamTwo.get_playerOneName());
        Player playerThree = dbDAO.getPlayerByName(teamOne.get_playerTwoName());
        Player playerFour = dbDAO.getPlayerByName(teamTwo.get_playerTwoName());

        playerOneButton.setTextOn(playerOne.get_name());
        playerOneButton.setTextOff(playerOne.get_name());
        playerOneButton.setText(playerOne.get_name());

        playerTwoButton.setTextOn(playerTwo.get_name());
        playerTwoButton.setTextOff(playerTwo.get_name());
        playerTwoButton.setText(playerTwo.get_name());

        playerThreeButton.setTextOn(playerThree.get_name());
        playerThreeButton.setTextOff(playerThree.get_name());
        playerThreeButton.setText(playerThree.get_name());

        playerFourButton.setTextOn(playerFour.get_name());
        playerFourButton.setTextOff(playerFour.get_name());
        playerFourButton.setText(playerFour.get_name());

        if(hand.get_bidder()!=0){
            if (hand.get_bidder()==playerOne.get_id()){
                playerOneButton.setChecked(true);
            } else if(hand.get_bidder()==playerTwo.get_id()){
                playerTwoButton.setChecked(true);
            } else if(hand.get_bidder()==playerThree.get_id()){
                playerThreeButton.setChecked(true);
            } else if(hand.get_bidder()==playerFour.get_id()){
                playerFourButton.setChecked(true);
            }
        }
    }

    public void setupBid(){
        //Putting in the initial scores
        TextView tvTeamOneInitialScore = (TextView) findViewById(R.id.team_one_initial_score);
        TextView tvTeamTwoInitialScore = (TextView) findViewById(R.id.team_two_initial_score);

        int teamOneScore;
        int teamTwoScore;

        if (hasPrevHand){
            teamOneScore = prevHand.get_teamOneEndingScore();
            teamTwoScore = prevHand.get_teamTwoEndingScore();
        } else {
            teamOneScore = 0;
            teamTwoScore = 0;
        }
        tvTeamOneInitialScore.setText(String.valueOf(teamOneScore));
        tvTeamTwoInitialScore.setText(String.valueOf(teamTwoScore));


        //Now on to the actual Bid Section
        if(!hand.get_bid().equals("Null")){
            biddingTeamID = hand.getBiddingTeamID(dbDAO);
            TableLayout table = (TableLayout) findViewById(R.id.bid_table);

            for(int i=0; i< table.getChildCount(); i++) {

                if(table.getChildAt(i) instanceof TableRow){
                    TableRow tableRow = (TableRow) table.getChildAt(i);

                    for(int j=0; j<tableRow.getChildCount(); j++) {
                        ToggleButton tb = (ToggleButton) tableRow.getChildAt(j);
                        if(tb.getTextOff().toString().equals(hand.get_bid())){
                            tb.setChecked(true);
                        }
                    }
                }
            }
        }
    }

    //Method to setup the point keeping section
    public void setupPointKeeping(){
        TextView teamOneNameView = (TextView) findViewById(R.id.team_one_names);
        TextView teamTwoNameView = (TextView) findViewById(R.id.team_two_names);

//        Team teamOne = dbDAO.getTeam(match.get_teamOneID());
//        Team teamTwo = dbDAO.getTeam(match.get_teamTwoID());

        String teamOneNames = (teamOne.get_playerOneName() + " & " + teamOne.get_playerTwoName());
        String teamTwoNames = (teamTwo.get_playerOneName() + " & " + teamTwo.get_playerTwoName());

        teamOneNameView.setText(teamOneNames);
        teamTwoNameView.setText(teamTwoNames);
    }

    //Method to setup the Seekbar
    public void setupSeekBar(){
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        teamOneTricks = (TextView) findViewById(R.id.team_one_tricks);
        teamTwoTricks = (TextView) findViewById(R.id.team_two_tricks);
        teamOneScoreChangeTextView = (TextView) findViewById(R.id.team_one_score_change);
        teamTwoScoreChangeTextView = (TextView) findViewById(R.id.team_two_score_change);

        if((hand.get_teamOneTricks()!=0) || (hand.get_teamTwoTricks()!=0)){
            seekBar.setProgress(hand.get_teamOneTricks());
        } else {
//            hand.set_teamOneTricks(seekBar.getProgress());
//            hand.set_teamTwoTricks(10 - seekBar.getProgress());
        }
        updatePoints();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hand.set_teamOneTricks(progress);
                hand.set_teamTwoTricks(10 - progress);
                updatePoints();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

        });
    }

    //Method called to update points
    public void updatePoints(){

        int bidderTricksWon;
        teamOnePointsTitle = (TextView) findViewById(R.id.team_one_points_title);
        teamTwoPointsTitle = (TextView) findViewById(R.id.team_two_points_title);

        BidderPointsCalculator bidderPointsCalculator = new BidderPointsCalculator(hand, hand.getBiddingTeamID(dbDAO), this);
        scoreChanges = bidderPointsCalculator.calculateScoreChange();

        teamOneTricks.setText(String.valueOf(hand.get_teamOneTricks()));
        teamTwoTricks.setText(String.valueOf(hand.get_teamTwoTricks()));

        if (scoreChanges[0] == 0){
            teamOneScoreChangeTextView.setText("");
            teamTwoScoreChangeTextView.setText("");
            teamOnePointsTitle.setText("");
            teamTwoPointsTitle.setText("");
        } else {
            if (biddingTeamID == hand.get_teamOneID()) {
                teamOneScoreChange = scoreChanges[0];
                teamTwoScoreChange = scoreChanges[1];
                teamOneScoreChangeTextView.setText(String.valueOf(scoreChanges[0]));
                teamTwoScoreChangeTextView.setText(String.valueOf(scoreChanges[1]));
                teamOnePointsTitle.setText(getString(R.string.points_awarded));
                teamTwoPointsTitle.setText(getString(R.string.points_awarded));
            } else if (biddingTeamID == hand.get_teamTwoID()) {
                teamOneScoreChange = scoreChanges[1];
                teamTwoScoreChange = scoreChanges[0];
                teamOneScoreChangeTextView.setText(String.valueOf(scoreChanges[1]));
                teamTwoScoreChangeTextView.setText(String.valueOf(scoreChanges[0]));
                teamOnePointsTitle.setText(getString(R.string.points_awarded));
                teamTwoPointsTitle.setText(getString(R.string.points_awarded));
            } else {
                teamOneScoreChangeTextView.setText("");
                teamTwoScoreChangeTextView.setText("");
                teamOnePointsTitle.setText("");
                teamTwoPointsTitle.setText("");
            }
        }
    }

    //calls menu inflater for the toolbar when the toolbar is created.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hand_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Refreshes when back is hit
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    //Actions for when things in the toolbar are clicked.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                return true;

            case R.id.manage_players:
                Intent intent = new Intent(this, PlayerEditor.class);
                startActivity(intent);
                return true;

            case R.id.manage_teams:
                Intent intentTeam = new Intent(this, TeamEditor.class);
                startActivity(intentTeam);
                return true;

            case R.id.view_database:
                Intent intent1 = new Intent(this, DatabaseExplorer.class);
                startActivity(intent1);
                return true;

            case R.id.change_dealer:
                Intent intent2 = new Intent(this, SetDealerOrderActivity.class);
                int handID = hand.get_id();
                intent2.putExtra("handID", handID);
                startActivity(intent2);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void setupSixBidRow(){

        //Getting the user setting
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Boolean sixBidPreference = preferences.getBoolean("allow_six_trick_bids", false);

        //Removing the table row with the six trick bids in them.
        if(!sixBidPreference){
            TableLayout table = (TableLayout) findViewById(R.id.bid_table);
            TableRow row = (TableRow) findViewById(R.id.six_bid_row);
            table.removeView(row);
        }
    }

    public void onBidClick(View v){

        ToggleButton toggleButton = (ToggleButton) v;

        if (toggleButton.isChecked()){
            TableLayout table = (TableLayout) findViewById(R.id.bid_table);

            for(int i=0; i< table.getChildCount(); i++) {

                if(table.getChildAt(i) instanceof TableRow){
                    TableRow tableRow = (TableRow) table.getChildAt(i);

                    for(int j=0; j<tableRow.getChildCount(); j++) {
                        ToggleButton tb = (ToggleButton) tableRow.getChildAt(j);
                        tb.setChecked(false);
                    }
                }
            }
            toggleButton.setChecked(true);
            bid = toggleButton.getTextOff().toString();
        } else {
            toggleButton.setChecked(false);
            bid = "Null";
        }

        hand.set_bid(bid);

        updatePoints();

    }

    public void onBidderClick(View v){

        ToggleButton toggleButton = (ToggleButton) v;

        if (toggleButton.isChecked()){
            TableLayout table = (TableLayout) findViewById(R.id.bidder_table);

            for(int i=0; i< table.getChildCount(); i++) {

                if(table.getChildAt(i) instanceof TableRow){
                    TableRow tableRow = (TableRow) table.getChildAt(i);

                    for(int j=0; j<tableRow.getChildCount(); j++) {
                        ToggleButton tb = (ToggleButton) tableRow.getChildAt(j);
                        tb.setChecked(false);
                    }
                }
            }
            toggleButton.setChecked(true);

            bidderName = toggleButton.getText().toString();
            Player bidder = dbDAO.getPlayerByName(bidderName);
            bidderID = bidder.get_id();
            hand.set_bidder(bidderID);
            biddingTeamID = hand.getBiddingTeamID(dbDAO);

        } else {
            toggleButton.setChecked(false);
            hand.set_bidder(0);
            biddingTeamID = 0;
        }

        updatePoints();

    }

    public void onOkClick(View v){
        //Method to save the hand and go back to the game activity
        handDAO = new HandDAO(this);

        //If the user is editing a hand that completed a game, we need to remove the game complete stats before any other changes.
        if (game.get_winningTeamID()!=0){
            gameDAO.completeChange(game, -1);
        }

        //Undoing the previous player stats if
        if (originalHand.get_handComplete()==1){
            handDAO.updateBidderStats(originalHand,-1);
        }

        //Making sure all of the needed information has been checked
        if(hand.get_bidder()==0){
            Toast.makeText(this, "No Bidder was selected! Select a bidder or hit back to cancel hand.", Toast.LENGTH_SHORT).show();
        } else if (hand.get_bid().equals("Null")) {
            Toast.makeText(this, "No bid was selected! Select bid or hit back to cancel hand.", Toast.LENGTH_SHORT).show();
        } else if (hand.get_teamOneTricks()==0 && hand.get_teamTwoTricks()==0){
            Toast.makeText(this, "The tricks won by each team were not selected. Use the slider to distribute the tricks or hit back to cancel hand.", Toast.LENGTH_SHORT).show();
        } else {

            //Updating the hand information
            hand.set_handComplete(1);

            if (hasPrevHand){
                hand.set_teamOneEndingScore(prevHand.get_teamOneEndingScore() + teamOneScoreChange);
                hand.set_teamTwoEndingScore(prevHand.get_teamTwoEndingScore() + teamTwoScoreChange);
                handDAO.updateHand(prevHand);
            } else {
                hand.set_teamOneEndingScore(teamOneScoreChange);
                hand.set_teamTwoEndingScore(teamTwoScoreChange);
            }

            hand.set_teamOneScoreChange(teamOneScoreChange);
            hand.set_teamTwoScoreChange(teamTwoScoreChange);

            handDAO.updateBidderStats(hand, 1);
            handDAO.updateHand(hand);

            //Updating the game information
            gameDAO = new GameDAO(this);

            game.set_lastHandID(hand.get_id());
            game.set_teamOneScore(hand.get_teamOneEndingScore());
            game.set_teamTwoScore(hand.get_teamTwoEndingScore());

            //Checking to see if the edited hand was part of a finished game
            if ((hand.get_teamOneEndingScore()>=500) || hand.get_teamTwoEndingScore() >= 500){
                if (hand.get_teamOneEndingScore()>hand.get_teamTwoEndingScore()){
                    game.set_winningTeamID(hand.get_teamOneID());
                } else if (hand.get_teamTwoEndingScore()>hand.get_teamOneEndingScore()){
                    game.set_winningTeamID(hand.get_teamTwoID());
                } else {
                    game.set_winningTeamID(biddingTeamID);
                }

                gameDAO.completeChange(game, 1);

            } else if ((hand.get_teamOneEndingScore()<=-500) && (hand.get_teamTwoEndingScore() >= 0)) {
                game.set_winningTeamID(hand.get_teamTwoID());
                gameDAO.completeChange(game, 1);
            } else if (((hand.get_teamTwoEndingScore() <= -500) && (hand.get_teamOneEndingScore() >=0))){
                game.set_winningTeamID(hand.get_teamOneID());
                gameDAO.completeChange(game, 1);
            } else {
                game.set_winningTeamID(0);
            }

            gameDAO.updateGame(game);

            int gameID = hand.get_gameID();
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("gameID", gameID);
            startActivity(intent);
        }
    }

}


