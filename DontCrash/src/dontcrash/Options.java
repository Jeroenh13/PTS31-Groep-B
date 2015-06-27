/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dontcrash;

import java.io.Serializable;

/**
 *
 * @author Bas
 */
public class Options implements Serializable {

    public Options(double music,double effect, RoomSort rs)
    {
        this.musicLevel = music;
        this.effectLevel = effect;
        this.roomSort = rs;
    }
    
    public static Options readFile() {
        return null;
    }
    
    public static void writeFile(Options o)
    {
    
    }

    private double musicLevel;
    private double effectLevel;
    private RoomSort roomSort;

    /**
     * gets the music volume level
     *
     * @return music volume level
     */
    public double getMusicLevel() {
        return musicLevel;
    }

    /**
     * sets the music volume level
     *
     * @param musicLevel level to be set
     */
    public void setMusicLevel(double musicLevel) {
        this.musicLevel = musicLevel;
    }

    /**
     * gets the effect volume level
     *
     * @return level of the effect volume level
     */
    public double getEffectLevel() {
        return effectLevel;
    }

    /**
     * sets the effect level for volume
     *
     * @param effectLevel volume level to be set
     */
    public void setEffectLevel(double effectLevel) {
        this.effectLevel = effectLevel;
    }

    /**
     * gets the way rooms are to be sorted
     *
     * @return way to sort
     */
    public RoomSort getRoomSort() {
        return roomSort;
    }

    /**
     * sets the way rooms should be sorted
     *
     * @param roomSort roomsort.
     */
    public void setRoomSort(RoomSort roomSort) {
        this.roomSort = roomSort;
    }
}
