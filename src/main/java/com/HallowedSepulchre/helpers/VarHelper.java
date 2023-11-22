package com.HallowedSepulchre.helpers;

import com.HallowedSepulchre.constants.Variation;

public class VarHelper {
    
    public static String VarToString(Variation variation){

        switch(variation){
            case North: return "North";
            case East: return "East";
            case South: return "South";
            case West: return "West";
            default: 
                return "";
        }

    }

}
