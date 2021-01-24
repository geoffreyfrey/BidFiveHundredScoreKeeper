package com.geoffreyfrey.SQLite;

import android.os.Parcelable;

/**
 * Created by geoff on 5/31/16.
 */
public class Player {

    private int _id;
    private String _name;
    private int _gamesPlayed;
    private int _gamesWon;
    private int _bids;
    private int _bidsWon;
    private int _netPoints;
    private int _deleted;

    public Player(){
        this(0, "Error", 0, 0, 0, 0, 0, 0);
    }

    public Player(String playerName){
        this(0, playerName, 0, 0, 0, 0, 0, 0);
    }

    public Player(int id, String playerName, int gamesPlayed, int gamesWon, int bids, int bidsWon, int netPoints, int deleted){
        this._id = id;
        this._name = playerName;
        this._gamesPlayed = gamesPlayed;
        this._gamesWon = gamesWon;
        this._bids = bids;
        this._bidsWon = bidsWon;
        this._netPoints = netPoints;
        this._deleted = deleted;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_gamesPlayed() {
        return _gamesPlayed;
    }

    public void set_gamesPlayed(int _gamesPlayed) {
        this._gamesPlayed = _gamesPlayed;
    }

    public int get_gamesWon() {
        return _gamesWon;
    }

    public void set_gamesWon(int _gamesWon) {
        this._gamesWon = _gamesWon;
    }

    public int get_deleted() {
        return _deleted;
    }

    public void set_deleted(int deleted) {
        this._deleted = deleted;
    }

    public int get_bids() {
        return _bids;
    }

    public void set_bids(int _bids) {
        this._bids = _bids;
    }

    public int get_bidsWon() {
        return _bidsWon;
    }

    public void set_bidsWon(int _bidsWon) {
        this._bidsWon = _bidsWon;
    }

    public int get_netPoints() {
        return _netPoints;
    }

    public void set_netPoints(int _netPoints) {
        this._netPoints = _netPoints;
    }
}
