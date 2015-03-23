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
public class Settings
{
    int soundLevel;
    int soundEffectLevel;
    Sorting roomSorting;
    
    /**
     * Initializes a new instance of settings
     */
    public Settings()
    {
        //TODO standaard waardes instellen
    }
    
    public void changeSound(int soundLevel)
    {
        this.soundLevel = soundLevel;
    }
    
    public void changeEffectLevel(int soundEffectLevel)
    {
        this.soundEffectLevel = soundEffectLevel;
    }
   
    public void changeSorting(Sorting sorting)
    {
        this.roomSorting = sorting;
    }
    
}
