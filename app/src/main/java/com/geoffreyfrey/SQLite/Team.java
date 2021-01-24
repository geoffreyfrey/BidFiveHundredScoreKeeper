package com.geoffreyfrey.SQLite;

/**
 * Created by geoff on 6/1/16.
 */
public class Team {

    //Variables
    private int _id;
    private String _playerOneName;
    private String _playerTwoName;
    private int _gamesPlayed;
    private int _gamesWon;

    //Constructors
    public Team(int id){
        this(id, "Error", "Error", 0, 0);
    }

    public Team(String playerOneName, String playerTwoName){
        this(0, playerOneName, playerTwoName, 0, 0);
    }

    public Team(int id, String playerOneName, String playerTwoName, int gamesPlayed, int gamesWon){
        this._id = id;
        this._playerOneName = playerOneName;
        this._playerTwoName = playerTwoName;
        this._gamesPlayed = gamesPlayed;
        this._gamesWon = gamesWon;
    }

    //Team Methods
    public boolean isPlayerOnTeamByName(String playerName){
        boolean isPlayer;

        if ((playerName.equals(_playerOneName)) || (playerName.equals(_playerTwoName))){
            isPlayer = true;
        } else {
            isPlayer = false;
        }
        return isPlayer;
    }


    //Getters and Setters
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_playerOneName() {
        return _playerOneName;
    }

    public void set_playerOneName(String _playerOneName) {
        this._playerOneName = _playerOneName;
    }

    public String get_playerTwoName() {
        return _playerTwoName;
    }

    public void set_playerTwoName(String _playerTwoName) {
        this._playerTwoName = _playerTwoName;
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
}
