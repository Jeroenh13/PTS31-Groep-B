/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import dontcrash.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kitty
 */
public class DatabaseManager 
{
    private Connection conn;
    
    /**
     * Database manager constructor
     */
    public DatabaseManager() {
    }
    
    /**
     * Opens the connection to the database
     */
    public void openConn()
    {
        /*
        String URL = "jdbc:oracle:thin:@192.168.15.50:1521:fhictora";
        
        String USER = "dbi298273";
        String PASS = "LxbdRdlzEx";
        */
                
        String URL = "jdbc:oracle:thin:@localhost:1521:xe";
        String USER = "PTS3";
        String PASS = "PTS3";
        
        try 
        {
            conn = DriverManager.getConnection(URL, USER, PASS);
        } 
        catch (SQLException ex) 
        {
            System.out.println("Could not make an connection");
            System.out.println(ex.getErrorCode() + " -- " + ex.getMessage());
        }
    }
    
    /**
     * Closes the connection to the database
     */
    public void closeConn()
    {
        try 
        {
            conn.close();
        } 
        catch (SQLException ex) 
        {
            System.out.println("Could not close connection");
            System.out.println(ex.getErrorCode() + " -- " + ex.getMessage());
        }
    }
    
    /**
     * Gets a list of all players in the database.
     * @return List of players
     */
    public List<Player> getPlayers()
    {
        List<Player> ps = new ArrayList<>();
        try 
        {
            String query = "SELECT id, name, email FROM PLAYER";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                ps.add(new Player(id, name, 0, email));
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println("Something went wrong. See next line for more info.");
            System.out.println(ex.getErrorCode() + " -- " + ex.getMessage());
        }
        
        return ps;
    }
    
    /**
     * Checks if password is correct.
     * @param username  of the player
     * @param password that was entered 
     * @return true if password is correct, false if not
     */
    public boolean checkPassword(String username, String password)
    {
        boolean succes;
        String temppassw = null;
        try 
        {
            String query = "SELECT password FROM PLAYER WHERE name=?";
            PreparedStatement stat = conn.prepareStatement(query);
            stat.setString(1, username);
            ResultSet rs = stat.executeQuery();
            while (rs.next())
            {
                temppassw = rs.getString("password");
            }
            succes = password.equals(temppassw);
        } 
        catch (Exception ex) 
        {
            System.out.println("Something went wrong. See next line for more info.");
            System.out.println(ex.getMessage() );
            succes = false;
        }
        return succes;
    }
    
    /**
     * Add a new player to the database.
     * @param name of the new player
     * @param password of the new player
     * @param email of the new player
     * @return true if player is added, false if not
     */
    public boolean addPlayer(String name, String password, String email)
    {
        boolean succes = true;
        try 
        {
            String query = "INSERT INTO PLAYER (name, password, email) VALUES (?,?,?)";
            PreparedStatement stat = conn.prepareStatement(query);
            stat.setString(1, name);
            stat.setString(2, password);
            stat.setString(3, email);
            stat.executeUpdate();
        } 
        catch (Exception ex) 
        {
            System.out.println("Something went wrong. See next line for more info.");
            System.out.println(ex.getMessage());
            succes = false;
        }
        return succes;
    }
    
    /**
     * Test the connection to the database
     * @return string from the database
     */
    public String testCon()
    {
        String naam = null;
        try
        {
            Statement stat = conn.createStatement();
            String query = "SELECT Naam FROM TESTTABLE WHERE TestID = 1";
            ResultSet rs = stat.executeQuery(query);
            while(rs.next())
            {
                naam = rs.getString("Naam");
            }  
        } 
        catch (SQLException ex) 
        {
            System.out.println("Something went wrong. See next line for more info.");
            System.out.println(ex.getErrorCode() + " -- " + ex.getMessage());
        }
        
        return naam;
    }
}
