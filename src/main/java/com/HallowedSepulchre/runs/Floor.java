package com.HallowedSepulchre.runs;

import com.HallowedSepulchre.Variation;
import com.HallowedSepulchre.helpers.VarHelper;

public class Floor {
    
    public static int TICKS_PER_FLOOR = 200; // 0.6 ticks / second * 60 seconds / minute * 2 minutes = 200 ticks per floor

    public Floor(int floor, Variation variation, int ticks, int looted){
        this.floor = floor;
        this.variation = variation;
        this.ticks = ticks;
        this.looted_count = looted;

        if (looted == 1){
            loot_1_ticks = ticks;
        } else if (looted == 2){
            loot_2_ticks = ticks;
        } else if (looted == 3){
            loot_3_ticks = ticks;
        }

    }

    public int floor;
    public Variation variation;

    private int ticks;

    public int loot_1_ticks = -1;
    public int loot_2_ticks = -1;
    public int loot_3_ticks = -1;

    public int looted_count;

    public String DisplayName(){
        return floor + " " + VarHelper.VarToString(variation);
    }

    public int GetTicks(Integer loot){

        if (loot == null){
            loot = 0;
        }

        if (loot > possibleLoots(floor)) return -1;

        switch(loot){
            case 0:
                return ticks;
            case 1:
                return loot_1_ticks;
            case 2:
                return loot_2_ticks;
            case 3:
                return loot_3_ticks;
            default:
                return -1;
        }

    }

    public void SetLootTicks(int ticks, int loot){

        switch(loot){
            case 0:
                this.ticks = ticks;
            case 1:
                loot_1_ticks = ticks;
            case 2:
                loot_2_ticks = ticks;
            case 3:
                loot_3_ticks = ticks;
        }

    }

    public static int possibleLoots(int floor){

        switch(floor){
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
                return 2;
            case 5:
                return 3;
            default:
                return 0;
        }

    }

}   
