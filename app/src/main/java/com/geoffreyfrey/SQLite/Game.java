package com.geoffreyfrey.SQLite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by geoff on 6/5/16.
 */
public class Game {

    private int _id;
    private int _matchID;
    private int _teamOneID;
    private int _teamTwoID;
    private int _teamOneScore;
    private int _teamTwoScore;
    private int _winningTeamID;
    private Date _gameDate;
    private int _lastHandID;

    public Game(){
        this(0, 0, 0, 0, 0, 0, 0, new Date(), 0);
    }

    public Game(int matchID){
        this(0, matchID, 0, 0, 0, 0, 0, new Date(), 0);
    }

    public Game(int id, int matchID, int teamOneID, int teamTwoID, int teamOneScore, int teamTwoScore, int winningTeamID, Date date, int lastHandID){
        this._id = id;
        this._matchID = matchID;
        this._teamOneID = teamOneID;
        this._teamTwoID = teamTwoID;
        this._teamOneScore = teamOneScore;
        this._teamTwoScore = teamTwoScore;
        this._winningTeamID = winningTeamID;
        this._gameDate = date;
        this._lastHandID = lastHandID;
    }

    //Methods
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public void set_teamOneID(int teamOneID) {
        this._teamOneID = teamOneID;
    }

    public int get_teamTwoID() {
        return _teamTwoID;
    }

    public void set_teamTwoID(int _teamTwoID) {
        this._teamTwoID = _teamTwoID;
    }

    public int get_teamOneScore() {
        return _teamOneScore;
    }

    public void set_teamOneScore(int _teamOneScore) {
        this._teamOneScore = _teamOneScore;
    }

    public int get_teamTwoScore() {
        return _teamTwoScore;
    }

    public void set_teamTwoScore(int _teamTwoScore) {
        this._teamTwoScore = _teamTwoScore;
    }

    public int get_winningTeamID() {
        return _winningTeamID;
    }

    public void set_winningTeamID(int gameComplete) {
        this._winningTeamID = gameComplete;
    }

    public int get_lastHandID() {
        return _lastHandID;
    }

    public void set_lastHandID(int _lastHandID) {
        this._lastHandID = _lastHandID;
    }

    public Date get_gameDate() {
        return _gameDate;
    }

    public void set_gameDate(Date _gameDate) {
        this._gameDate = _gameDate;
    }
}
