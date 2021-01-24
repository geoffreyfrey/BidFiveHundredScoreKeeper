package com.geoffreyfrey.bidfivehundredscorekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geoffreyfrey.SQLite.DatabaseDAO;
import com.geoffreyfrey.SQLite.Game;
import com.geoffreyfrey.SQLite.GameDAO;
import com.geoffreyfrey.SQLite.Hand;
import com.geoffreyfrey.SQLite.HandDAO;
import com.geoffreyfrey.SQLite.Match;
import com.geoffreyfrey.SQLite.MatchDAO;
import com.geoffreyfrey.SQLite.Player;
import com.geoffreyfrey.SQLite.Team;

/**
 * Created by geoff on 7/11/16.
 */
public class SetDealerOrderActivity extends AppCompatActivity {

    //Declaring the views
    TextView tvDealerOne;
    TextView tvDealerTwo;
    TextView tvDealerThree;
    TextView tvDealerFour;
    TextView tvDealerButtonsHeader;

    DatabaseDAO dbDAO;
    Match match;
    Game game;
    GameDAO gameDAO;
    Hand hand;
    Hand originalHand;
    Team teamOne;
    Team teamTwo;
    int dealPosition;
    int playerOneID;
    int playerTwoID;
    int playerThreeID;
    int playerFourID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Creating the layout and action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dealer_order);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_no_options);
        TextView toolbarText = (TextView) findViewById(R.id.toolbar_text_no_options);
        toolbarText.setText("SET DEALER ORDER");
        setSupportActionBar(myToolbar);

        //Pulling match, game, and hand info from the intent
        gameDAO = new GameDAO(this);
        Bundle extras = getIntent().getExtras();
        final int handID = extras.getInt("handID");
        dbDAO = new DatabaseDAO(this);
        hand = dbDAO.getHand(handID);
        originalHand = hand;
        match = dbDAO.getMatch(hand.get_matchID());
        game = dbDAO.getGame(hand.get_gameID());
        teamOne = dbDAO.getTeam(match.get_teamOneID());
        teamTwo = dbDAO.getTeam(match.get_teamTwoID());
        playerOneID = match.get_playerOneID();
        playerTwoID = match.get_playerTwoID();
        playerThreeID = match.get_playerThreeID();
        playerFourID = match.get_playerFourID();

        dealPosition = 0;

        setupPlayers();
        setupDealerTable();
    }

    public void setupPlayers(){
        //Setting up the Buttons
        Button playerOneButton = (Button) findViewById(R.id.player_one_button);
        Button playerTwoButton = (Button) findViewById(R.id.player_two_button);
        Button playerThreeButton = (Button) findViewById(R.id.player_three_button);
        Button playerFourButton = (Button) findViewById(R.id.player_four_button);

        Player playerOne = dbDAO.getPlayerByID(match.get_playerOneID());
        Player playerTwo = dbDAO.getPlayerByID(match.get_playerTwoID());
        Player playerThree = dbDAO.getPlayerByID(match.get_playerThreeID());
        Player playerFour = dbDAO.getPlayerByID(match.get_playerFourID());

        playerOneButton.setText(playerOne.get_name());

        playerTwoButton.setText(playerTwo.get_name());

        playerThreeButton.setText(playerThree.get_name());

        playerFourButton.setText(playerFour.get_name());
    }
    public void setupDealerTable(){
        tvDealerOne = (TextView) findViewById(R.id.dealer_one);
        tvDealerTwo = (TextView) findViewById(R.id.dealer_two);
        tvDealerThree = (TextView) findViewById(R.id.dealer_three);
        tvDealerFour = (TextView) findViewById(R.id.dealer_four);
    }

    public void onDealerClick(View v){

        Button button = (Button) v;
        Button teammateButton;
        int ID = v.getId();
        tvDealerButtonsHeader = (TextView) findViewById(R.id.set_dealer_header);

        button.setEnabled(false);

        if (dealPosition==0){
            Player dealerOne;
            Player dealerThree;
            if (ID==R.id.player_one_button){
                teammateButton = (Button) findViewById(R.id.player_three_button);
                dealerOne = dbDAO.getPlayerByID(playerOneID);
                dealerThree = dbDAO.getPlayerByID(playerThreeID);
            } else if (ID==R.id.player_two_button){
                teammateButton = (Button) findViewById(R.id.player_four_button);
                dealerOne = dbDAO.getPlayerByID(playerTwoID);
                dealerThree = dbDAO.getPlayerByID(playerFourID);
            } else if (ID==R.id.player_three_button){
                teammateButton = (Button) findViewById(R.id.player_one_button);
                dealerOne = dbDAO.getPlayerByID(playerThreeID);
                dealerThree = dbDAO.getPlayerByID(playerOneID);
            } else {
                teammateButton = (Button) findViewById(R.id.player_two_button);
                dealerOne = dbDAO.getPlayerByID(playerFourID);
                dealerThree = dbDAO.getPlayerByID(playerTwoID);
            }

            hand.set_dealer(dealerOne.get_id());
            match.set_playerOneID(dealerOne.get_id());
            match.set_playerThreeID(dealerThree.get_id());
            tvDealerOne.setText(dealerOne.get_name());
            tvDealerThree.setText(dealerThree.get_name());
            teammateButton.setEnabled(false);
            button.setVisibility(View.GONE);
            teammateButton.setVisibility(View.GONE);
            tvDealerButtonsHeader.setText(getResources().getString(R.string.set_second_dealer_header_text));

            dealPosition++;
        } else {
            Player dealerTwo;
            Player dealerFour;
            if (ID==R.id.player_one_button){
                teammateButton = (Button) findViewById(R.id.player_three_button);
                dealerTwo = dbDAO.getPlayerByID(playerOneID);
                dealerFour = dbDAO.getPlayerByID(playerThreeID);
            } else if (ID==R.id.player_two_button){
                teammateButton = (Button) findViewById(R.id.player_four_button);
                dealerTwo = dbDAO.getPlayerByID(playerTwoID);
                dealerFour = dbDAO.getPlayerByID(playerFourID);
            } else if (ID==R.id.player_three_button){
                teammateButton = (Button) findViewById(R.id.player_one_button);
                dealerTwo = dbDAO.getPlayerByID(playerThreeID);
                dealerFour = dbDAO.getPlayerByID(playerOneID);
            } else {
                teammateButton = (Button) findViewById(R.id.player_two_button);
                dealerTwo = dbDAO.getPlayerByID(playerFourID);
                dealerFour = dbDAO.getPlayerByID(playerTwoID);
            }

            match.set_playerTwoID(dealerTwo.get_id());
            match.set_playerFourID(dealerFour.get_id());
            tvDealerTwo.setText(dealerTwo.get_name());
            tvDealerFour.setText(dealerFour.get_name());
            teammateButton.setEnabled(false);
            button.setVisibility(View.GONE);
            teammateButton.setVisibility(View.GONE);
            tvDealerButtonsHeader.setVisibility(View.GONE);
            dealPosition++;
        }

    }

    public void onOkClick(View v){
        if(dealPosition==0){
            Toast.makeText(this, "Dealer not set. Use the back button to cancel.",Toast.LENGTH_SHORT).show();
        } else if (dealPosition==1){
            Toast.makeText(this, "The second dealer needs to be set. Use the back button to cancel.", Toast.LENGTH_SHORT).show();
        } else {
            MatchDAO matchDAO = new MatchDAO(this);
            matchDAO.updateMatch(match);

            HandDAO handDAO = new HandDAO(this);
            handDAO.updateHandDealer(hand);

            Intent intent = new Intent(this, HandActivity.class);
            intent.putExtra("handID", hand.get_id());
            startActivity(intent);
        }
    }
}