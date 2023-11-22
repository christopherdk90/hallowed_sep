package com.HallowedSepulchre.data;

import java.util.Map;

import com.HallowedSepulchre.Variation;
import com.HallowedSepulchre.runs.Floor;

public class LoadFloorArgs {
    
    public Map<Integer, Map<Variation, Floor>> bestFloorMap;

    public Map<Integer, Integer> goalMap;

    public Floor currentFloor;

    public int highestFloor;

    public int bestFloorTicks;
    public int goalFloorTicks;

    public int bufferInConfig;

}
