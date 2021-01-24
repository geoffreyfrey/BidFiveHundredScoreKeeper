package com.geoffreyfrey.SQLite;

import java.util.Date;

/**
 * Created by geoff on 6/2/16.
 */
public class Match {
    private int _id;
    private int _teamOneID;
    private int _teamTwoID;
    private int _teamOneVictories;
    private int _teamTwoVictories;
    private Date _matchDate;
    private int _playerOneID;
    private int _playerTwoID;
    private int _playerThreeID;
    private int _playerFourID;
    private int _lastGameID;


    //Constructors
    public Match(){
        this(0, 0, 0, 0, 0, new Date(), 0, 0, 0, 0, 0);
    }

    public Match(int matchID){
        this(matchID, 0, 0, 0, 0, new Date(), 0, 0, 0, 0, 0);
    }

    public Match(int teamOneID, int teamTwoID){
        this(0, teamOneID, teamTwoID, 0, 0, new Date(), 0, 0, 0, 0, 0);
    }

    public Match(int id, int teamOneID, int teamTwoID, int teamOneVictories, int teamTwoVictories, Date date, int playerOneID, int playerTwoID, int playerThreeID, int playerFourID, int lastGameID){
        this._id = id;
        this._teamOneID = teamOneID;
        this._teamTwoID = teamTwoID;
        this._teamOneVictories = teamOneVictories;
        this._teamTwoVictories = teamTwoVictories;
        this._matchDate = date;
        this._playerOneID = playerOneID;
        this._playerTwoID = playerTwoID;
        this._playerThreeID = playerThreeID;
        this._playerFourID = playerFourID;
        this._lastGameID = lastGameID;
    }

    //Match Functions
    public int getNextDealer(int currentDealerID){
        int nextDealerID;

        if (currentDealerID == _playerOneID){
            nextDealerID = _playerTwoID;
        } else if (currentDealerID == _playerTwoID){
            nextDealerID = _playerThreeID;
        } else if (currentDealerID == _playerThreeID){
            nextDealerID = _playerFourID;
        } else {
            nextDealerID = _playerOneID;
        }
        return nextDealerID;
    }

    //Getters and Setters
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public int get_teamOneVictories() {
        return _teamOneVictories;
    }

    public void set_teamOneVictories(int teamOneVictories) {
        this._teamOneVictories = teamOneVictories;
    }

    public int get_teamTwoVictories() {
        return _teamTwoVictories;
    }

    public void set_teamTwoVictories(int teamTwoVictories) {
        this._teamTwoVictories = teamTwoVictories;
    }

    public Date get_matchDate() {
        return _matchDate;
    }

    public void set_matchDate(Date date) {
        this._matchDate = date;
    }

    public int get_playerOneID() {
        return _playerOneID;
    }

    public void set_playerOneID(int _playerOneID) {
        this._playerOneID = _playerOneID;
    }

    public int get_playerTwoID() {
        return _playerTwoID;
    }

    public void set_playerTwoID(int _playerTwoID) {
        this._playerTwoID = _playerTwoID;
    }

    public int get_playerThreeID() {
        return _playerThreeID;
    }

    public void set_playerThreeID(int _playerThreeID) {
        this._playerThreeID = _playerThreeID;
    }

    public int get_playerFourID() {
        return _playerFourID;
    }

    public void set_playerFourID(int _playerFourID) {
        this._playerFourID = _playerFourID;
    }

    public int get_lastGameID() {
        return _lastGameID;
    }

    public void set_lastGameID(int _lastGameID) {
        this._lastGameID = _lastGameID;
    }
}
