package com.HallowedSepulchre.runs;

import java.util.Map;

import com.HallowedSepulchre.constants.Regions;
import com.HallowedSepulchre.constants.Variation;
import com.HallowedSepulchre.helpers.TimeHelper;

public class Run {
    
    public int totalTicks;
    public int totalSeconds;
    public int totalFloors;

    public Floor first;
    public Floor second;
    public Floor third;
    public Floor fourth;
    public Floor fifth;

    public void Process(){
        
        totalTicks = 0;
        totalSeconds = 0;

        if (first != null){
            totalFloors = 1;
            totalTicks += first.GetTicks(0);
            totalSeconds += TimeHelper.getSecondsFromTicks(first.GetTicks(0));
        }
        if (second != null){
            totalFloors = 2;
            totalTicks += second.GetTicks(0);
            totalSeconds += TimeHelper.getSecondsFromTicks(second.GetTicks(0));
        }
        if (third != null){
            totalFloors = 3;
            totalTicks += third.GetTicks(0);
            totalSeconds += TimeHelper.getSecondsFromTicks(third.GetTicks(0));
        }
        if (fourth != null){
            totalFloors = 4;
            totalTicks += fourth.GetTicks(0);
            totalSeconds += TimeHelper.getSecondsFromTicks(fourth.GetTicks(0));
        }
        if (fifth != null){
            totalFloors = 5;
            totalTicks += fifth.GetTicks(0);
            totalSeconds += TimeHelper.getSecondsFromTicks(fifth.GetTicks(0));
        }
    }

    public String DisplayTime() {
        return TimeHelper.GetTimeFromSeconds(totalSeconds);
    }

    public static Run GetBestRun(Run original, Run newest){

        // No best run stored yet, so the newest run is best
        if (original == null){
            return newest;
        }

        // Return original best if the new run has less floors
        if (newest.totalFloors < original.totalFloors){
            return original;
        }

        // Return the newest if it has more floors than current best
        if (newest.totalFloors > original.totalFloors){
            return newest;
        }

        // Both runs now definitely have the same amount of floors completed

        // Return original if it was done in fewer ticks
        // Otherwise, return newer
        return original.totalTicks < newest.totalTicks ? original : newest;

    }

    public static Run buildOptimalRun(
        Map<Integer,Map<Variation,Floor>> bestFloorMap, 
        int firstGoal, 
        int secondGoal, 
        int thirdGoal, 
        int fourthGoal, 
        int fifthGoal
        ){

        // No runs have been completed
		if (bestFloorMap == null || bestFloorMap.size() == 0) {
			return null;
		}

        // By convention, the optimal floors always have the goal ticks stored as best ticks (loot = 0)
        int optimalTickStorage = 0;

        boolean set = false;

		Run optimal = new Run();

        int optimalTicks;
        int variationTicks;

		Map<Variation, Floor> first = bestFloorMap.get(Regions.FIRST_FLOOR);
        // First floor not seen, there is no optimal run
        if (first == null) return null;
        optimal.first = new Floor(1, Variation.None, -1, 0);
        for (Floor variation : first.values()) {
            optimalTicks = optimal.first.GetTicks(optimalTickStorage);
            variationTicks = variation.GetTicks(firstGoal);

            // Don't consider variations without goal data
            if (variationTicks < 0) continue;

            if (optimalTicks < 0 || variationTicks < optimalTicks){
                optimal.first.variation = variation.variation;
                optimal.first.SetLootTicks(variationTicks, optimalTickStorage);
                set = true;
            }
        }

        if (!set) return null;
        set = false;

        Map<Variation, Floor> second = bestFloorMap.get(Regions.SECOND_FLOOR);
        // Second floor not seen
        if (second == null) return optimal;
        optimal.second = new Floor(2, Variation.None, -1, 0);
		for (Floor variation : second.values()) {
            optimalTicks = optimal.second.GetTicks(optimalTickStorage);
            variationTicks = variation.GetTicks(secondGoal);

            // Don't consider variations without goal data
            if (variationTicks < 0) continue;

            if (optimalTicks < 0 || variationTicks < optimalTicks){
                optimal.second.variation = variation.variation;
                optimal.second.SetLootTicks(variationTicks, optimalTickStorage);
                set = true;
            }
        }

        if (!set) return null;
        set = false;

		Map<Variation, Floor> third = bestFloorMap.get(Regions.THIRD_FLOOR);
        // Third floor not seen
        if (third == null) return optimal;
        optimal.third = new Floor(3, Variation.None, -1, 0);
		for (Floor variation : third.values()) {
            optimalTicks = optimal.third.GetTicks(optimalTickStorage);
            variationTicks = variation.GetTicks(thirdGoal);

            // Don't consider variations without goal data
            if (variationTicks < 0) continue;

            if (optimalTicks < 0 || variationTicks < optimalTicks){
                optimal.third.variation = variation.variation;
                optimal.third.SetLootTicks(variationTicks, optimalTickStorage);
                set = true;
            }
        }

        if (!set) return null;
        set = false;

		Map<Variation, Floor> fourth = bestFloorMap.get(Regions.FOURTH_FLOOR);
        // Fourth floor not seen
        if (fourth == null) return optimal;
		optimal.fourth = new Floor(4, Variation.None, -1, 0);
		for (Floor variation : fourth.values()) {
            optimalTicks = optimal.fourth.GetTicks(optimalTickStorage);
            variationTicks = variation.GetTicks(fourthGoal);

            // Don't consider variations without goal data
            if (variationTicks < 0) continue;

            if (optimalTicks < 0 || variationTicks < optimalTicks){
                optimal.fourth.variation = variation.variation;
                optimal.fourth.SetLootTicks(variationTicks, optimalTickStorage);
                set = true;
            }
        }

        if (!set) return null;
        set = false;

		Map<Variation, Floor> fifth = bestFloorMap.get(Regions.FIFTH_FLOOR);
		// Fifth floor not seen
        if (fifth == null) return optimal;
		optimal.fifth = new Floor(5, Variation.None, -1, 0);
		for (Floor variation : fifth.values()) {
            optimalTicks = optimal.fifth.GetTicks(optimalTickStorage);
            variationTicks = variation.GetTicks(fifthGoal);

            // Don't consider variations without goal data
            if (variationTicks < 0) continue;

            if (optimalTicks < 0 || variationTicks < optimalTicks){
                optimal.fifth.variation = variation.variation;
                optimal.fifth.SetLootTicks(variationTicks, optimalTickStorage);
                set = true;
            }
        }

        if (!set) return null;

		return optimal;

	}


}
