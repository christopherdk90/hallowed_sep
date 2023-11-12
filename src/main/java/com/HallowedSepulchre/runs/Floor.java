package com.HallowedSepulchre.runs;

import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.helpers.TimeHelper;

public class Floor {
    
    public Floor(int floor, Variations variation, int ticks, int looted){
        this.floor = floor;
        this.variation = variation;
        this.ticks = ticks;
        this.looted = looted;
    }

    public int floor;
    public Variations variation;
    public int ticks;
    public int looted;

    public String DisplayName(){
        return floor + " " + VarHelper.VarToString(variation);
    }

    public String DisplayTime(){

        return TimeHelper.GetTimeFromTicks(ticks);

    }

}   
