/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

/**
 *
 * @author Saya
 */
public interface Chatbox
{
    //deze interface wel nuttig ?
    //wordt inderdaad nergens geimplementeerd.
    public void Send(String Message);

    
    public String[] Retrieve();
    
    
}
