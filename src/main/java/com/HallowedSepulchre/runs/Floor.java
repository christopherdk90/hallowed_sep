package com.HallowedSepulchre.runs;

import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.helpers.TimeHelper;

public class Floor {
    
    public Floor(int floor, Variations variation, int ticks){
        this.floor = floor;
        this.variation = variation;
        this.ticks = ticks;
    }

    public int floor;
    public Variations variation;
    public int ticks;

    public String DisplayName(){
        return floor + " " + VarHelper.VarToString(variation);
    }

    public String DisplayTime(){

        return TimeHelper.GetTimeFromTicks(ticks);

    }

}   
