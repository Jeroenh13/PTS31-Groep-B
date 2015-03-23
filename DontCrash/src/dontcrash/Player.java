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
public class Player {

    public int playerID;
    public String name;
    public int score;
    public String email;
    public List<Player> friends;

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
}
