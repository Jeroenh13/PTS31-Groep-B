package dontcrash;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Saya
 */
public class Player implements Chatbox{

    public int playerID;
    public String name;
    public int score;
    public String email;
    public List<Player> friends;
    public Character currentCharacter;
    public List<String> messages;

    /**
     * Initializes a new instance of player
     *
     * @param playerID id of the new player
     * @param name of the new player
     * @param score of the new player
     * @param email of the new player
     */
    public Player(int playerID, String name, int score, String email) {
        friends = new ArrayList<Player>();
        messages = new ArrayList<String>();
        this.name = name;
        this.playerID = playerID;
        this.score = score;
        this.email = email;
    }

    /**
     * Tries to add player as friend
     *
     * @param friend to add to friendlist
     * @return false if failure, true if succes
     */
    public boolean addFriend(Player friend) {
        if (friends.contains(friend)) {
            return false;
        }
        friends.add(friend);
        return true;
    }
    
    public void setCharacter(Character character){
        this.currentCharacter = character;
    }

    @Override
    public void Send(String message, Object reciever) {
        //TODO
    }
}
