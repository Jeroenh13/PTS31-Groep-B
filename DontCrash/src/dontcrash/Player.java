package dontcrash;

import java.io.Serializable;
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
public class Player implements Serializable{

    public int playerID;
    public String name;
    public int score;
    public String email;
    public transient List<Player> friends;
    public dontcrash.Character character;

    /**
     * Initializes a new instance of player
     *
     * @param playerID id of the new player
     * @param name of the new player
     * @param score of the new player
     * @param email of the new player
     */
    public Player(int playerID, String name, int score, String email) {
        friends = new ArrayList<>();
        this.name = name;
        this.playerID = playerID;
        this.score = score;
        this.email = email;
    }
    
    public dontcrash.Character newCharacter(Player p, int nxt)
    {
        if(p.playerID == this.playerID)
        {
            character = new Character(p,nxt);
        }
        return character;
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
}
