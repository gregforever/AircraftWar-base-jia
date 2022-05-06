package edu.hitsz.rank;

import java.io.Serializable;

public class Player implements Serializable {

    private static final long serialVersionUID = 2618916752132488921L;
    private String playerName;
    private int playerScore;
    private String playerTime;

    public Player(String playerName,int playerScore,String playerTime){
        this.playerName = playerName;
        this.playerScore = playerScore;
        this.playerTime = playerTime;
    }

    public String getPlayerTime(){
        return playerTime;
    }
    public void setPlayerTime(String playerTime){
        this.playerTime = playerTime;
    }

    public String getPlayerName(){
        return playerName;
    }
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    public int getPlayerScore(){
        return playerScore;
    }
    public void setPlayerScore(int playerScore){
        this.playerScore = playerScore;
    }

}
