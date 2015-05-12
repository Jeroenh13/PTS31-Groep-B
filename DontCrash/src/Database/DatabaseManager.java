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
    public Connection conn;
    
    public DatabaseManager()
    {
        
    }
    
    public void RegisterDriver()
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } 
        catch (ClassNotFoundException ex) 
        {
            System.out.println("Error: unable to load driver class!");
            System.out.println(ex.getMessage());
        }
    }
    
    public void OpenConn()
    {
        String URL = "jdbc:oracle:thin:@192.168.15.50:1521:fhictora";
        String USER = "dbi298273";
        String PASS = "LxbdRdlzEx";
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
    
    public void CloseConn()
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
    
    public List<Player> GetPlayers()
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
    
    public boolean CheckPassword(String username, String password)
    {
        boolean succes = false;
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
    
    public boolean AddPlayer(String name, String password, String email)
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
    
    public String TestCon()
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
