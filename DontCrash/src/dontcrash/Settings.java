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
    
    /**
     * Changes the sound level
     * @param soundLevel the new level of sound
     */
    public void changeSound(int soundLevel)
    {
        this.soundLevel = soundLevel;
    }
    
    /**
     * Changes the soundeffect level
     * @param soundEffectLevel the new level of soundeffect
     */
    public void changeEffectLevel(int soundEffectLevel)
    {
        this.soundEffectLevel = soundEffectLevel;
    }
   
    /**
     * Changes the sorting
     * @param sorting the new way to be sorted at
     */
    public void changeSorting(Sorting sorting)
    {
        this.roomSorting = sorting;
    }
    
}
