package com.geoffreyfrey.SQLite;

import android.content.Context;

/**
 * Created by geoff on 6/20/16.
 */
public class Hand {

    //Declaring Variables
    private int _id;
    private int _gameID;
    private int _matchID;
    private int _teamOneID;
    private int _teamTwoID;
    private int _dealer;
    private int _bidder;
    private String _bid;
    private int _previousHandID;
    private int _teamOneTricks;
    private int _teamTwoTricks;
    private int _teamOneScoreChange;
    private int _teamTwoScoreChange;
    private int _teamOneEndingScore;
    private int _teamTwoEndingScore;
    private int _handComplete;

    //Constructors
    public Hand(){
        this(0, 0, 0, 0, 0, 0, 0, "Null", 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public Hand(int gameID){
        this(0, gameID, 0, 0, 0, 0, 0, "Null", 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public Hand(int id, int gameID, int matchID, int teamOneID, int teamTwoID, int dealer, int bidder, String bid, int previousHandID, int teamOneTricks, int teamTwoTricks, int teamOneScoreChange, int teamTwoScoreChange, int teamOneEndingScore, int teamTwoEndingScore, int handComplete){
        this._id = id;
        this._gameID = gameID;
        this._matchID = matchID;
        this._teamOneID = teamOneID;
        this._teamTwoID = teamTwoID;
        this._dealer = dealer;
        this._bidder = bidder;
        this._bid = bid;
        this._previousHandID = previousHandID;
        this._teamOneTricks = teamOneTricks;
        this._teamTwoTricks = teamTwoTricks;
        this._teamOneScoreChange = teamOneScoreChange;
        this._teamTwoScoreChange = teamTwoScoreChange;
        this._teamOneEndingScore = teamOneEndingScore;
        this._teamTwoEndingScore = teamTwoEndingScore;
        this._handComplete = handComplete;
    }

    //Methods
    public int getBiddingTeamID(DatabaseDAO dbDAO){
        int biddingTeamID;

        Team teamOne = dbDAO.getTeam(_teamOneID);
        Team teamTwo = dbDAO.getTeam(_teamTwoID);
        Player bidder = dbDAO.getPlayerByID(_bidder);

        if ((bidder.get_name().equals(teamOne.get_playerOneName())) || (bidder.get_name().equals(teamOne.get_playerTwoName()))) {
            biddingTeamID = teamOne.get_id();
        } else if ((bidder.get_name().equals(teamTwo.get_playerOneName())) || (bidder.get_name().equals(teamTwo.get_playerTwoName()))) {
            biddingTeamID = teamTwo.get_id();
        } else {
            biddingTeamID = 0;
        }

        return biddingTeamID;
    }


    //Getter and Setter Methods
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_gameID() {
        return _gameID;
    }

    public void set_gameID(int _gameID) {
        this._gameID = _gameID;
    }

    public int get_matchID() {
        return _matchID;
    }

    public void set_matchID(int _matchID) {
        this._matchID = _matchID;
    }

    public int get_teamOneID() {
        return _teamOneID;
    }

    public void set_teamOneID(int _teamOneID) {
        this._teamOneID = _teamOneID;
    }

    public int get_teamTwoID() {
        return _teamTwoID;
    }

    public void set_teamTwoID(int _teamTwoID) {
        this._teamTwoID = _teamTwoID;
    }

    public int get_dealer() {
        return _dealer;
    }

    public void set_dealer(int _dealer) {
        this._dealer = _dealer;
    }

    public int get_bidder() {
        return _bidder;
    }

    public void set_bidder(int _bidder) {
        this._bidder = _bidder;
    }

    public String get_bid() {
        return _bid;
    }

    public void set_bid(String _bid) {
        this._bid = _bid;
    }

    public int get_previousHandID() {
        return _previousHandID;
    }

    public void set_previousHandID(int _previousHandID) {
        this._previousHandID = _previousHandID;
    }

    public int get_teamOneTricks() {
        return _teamOneTricks;
    }

    public void set_teamOneTricks(int _teamOneTricks) {
        this._teamOneTricks = _teamOneTricks;
    }

    public int get_teamTwoTricks() {
        return _teamTwoTricks;
    }

    public void set_teamTwoTricks(int _teamTwoTricks) {
        this._teamTwoTricks = _teamTwoTricks;
    }

    public int get_teamOneScoreChange() {
        return _teamOneScoreChange;
    }

    public void set_teamOneScoreChange(int _teamOneScoreChange) {
        this._teamOneScoreChange = _teamOneScoreChange;
    }

    public int get_teamTwoScoreChange() {
        return _teamTwoScoreChange;
    }

    public void set_teamTwoScoreChange(int _teamTwoScoreChange) {
        this._teamTwoScoreChange = _teamTwoScoreChange;
    }

    public int get_teamOneEndingScore() {
        return _teamOneEndingScore;
    }

    public void set_teamOneEndingScore(int _teamOneEndingScore) {
        this._teamOneEndingScore = _teamOneEndingScore;
    }

    public int get_teamTwoEndingScore() {
        return _teamTwoEndingScore;
    }

    public void set_teamTwoEndingScore(int _teamTwoEndingScore) {
        this._teamTwoEndingScore = _teamTwoEndingScore;
    }

    public int get_handComplete() {
        return _handComplete;
    }

    public void set_handComplete(int _handComplete) {
        this._handComplete = _handComplete;
    }
}
