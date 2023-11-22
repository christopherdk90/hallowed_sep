package com.HallowedSepulchre.runs;

import java.util.Map;

import com.HallowedSepulchre.Variation;
import com.HallowedSepulchre.Regions;
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

		Run optimal = new Run();

		Map<Variation, Floor> first = bestFloorMap.get(Regions.FIRST_FLOOR);
        // First floor not seen, there is no optimal run
        if (first == null) return null;
        optimal.first = new Floor(1, Variation.None, -1, 0);
        for (Floor variation : first.values()) {
            if (optimal.first.GetTicks(0) < 0 || variation.GetTicks(firstGoal) < optimal.first.GetTicks(optimalTickStorage)){
                optimal.first.variation = variation.variation;
                optimal.first.SetLootTicks(variation.GetTicks(firstGoal), optimalTickStorage);
            }
        }

        Map<Variation, Floor> second = bestFloorMap.get(Regions.SECOND_FLOOR);
        // Second floor not seen
        if (second == null) return optimal;
        optimal.second = new Floor(2, Variation.None, -1, 0);
		for (Floor variation : second.values()) {
            if (optimal.second.GetTicks(optimalTickStorage) < 0 || variation.GetTicks(secondGoal) < optimal.second.GetTicks(optimalTickStorage)){
                optimal.second.variation = variation.variation;
                optimal.second.SetLootTicks(variation.GetTicks(secondGoal), optimalTickStorage);
            }
        }


		Map<Variation, Floor> third = bestFloorMap.get(Regions.THIRD_FLOOR);
        // Third floor not seen
        if (third == null) return optimal;
        optimal.third = new Floor(3, Variation.None, -1, 0);
		for (Floor variation : third.values()) {
            if (optimal.third.GetTicks(optimalTickStorage) < 0 || variation.GetTicks(thirdGoal) < optimal.third.GetTicks(optimalTickStorage)){
                optimal.third.variation = variation.variation;
                optimal.third.SetLootTicks(variation.GetTicks(thirdGoal), optimalTickStorage);
            }
        }


		Map<Variation, Floor> fourth = bestFloorMap.get(Regions.FOURTH_FLOOR);
        // Fourth floor not seen
        if (fourth == null) return optimal;
		optimal.fourth = new Floor(4, Variation.None, -1, 0);
		for (Floor variation : fourth.values()) {
            if (optimal.fourth.GetTicks(optimalTickStorage) < 0 || variation.GetTicks(fourthGoal) < optimal.fourth.GetTicks(optimalTickStorage)){
                optimal.fourth.variation = variation.variation;
                optimal.fourth.SetLootTicks(variation.GetTicks(fourthGoal), optimalTickStorage);
            }
        }

		Map<Variation, Floor> fifth = bestFloorMap.get(Regions.FIFTH_FLOOR);
		// Fifth floor not seen
        if (fifth == null) return optimal;
		optimal.fifth = new Floor(5, Variation.None, -1, 0);
		for (Floor variation : fifth.values()) {
            if (optimal.fifth.GetTicks(optimalTickStorage) < 0 || variation.GetTicks(fifthGoal) < optimal.fifth.GetTicks(optimalTickStorage)){
                optimal.fifth.variation = variation.variation;
                optimal.fifth.SetLootTicks(variation.GetTicks(fifthGoal), optimalTickStorage);
            }
        }

		return optimal;

	}


}
