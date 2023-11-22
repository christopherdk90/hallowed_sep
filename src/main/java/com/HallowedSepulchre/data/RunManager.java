package com.HallowedSepulchre.data;

import java.util.Map;

import com.HallowedSepulchre.constants.Variation;
import com.HallowedSepulchre.runs.Floor;

public class RunManager {
    
    public static int averageFutureGoalFloors(LoadFloorArgs args){

		int ticks = 0;

		for (int i = args.currentFloor.floor + 1; i <= args.highestFloor; i++){

			Map<Variation, Floor> variations = args.bestFloorMap.get(i);

			// Shouldn't happen because the floors to visit were derived from the map, but just to avoid a NPE
			if (variations == null){
				return -1;
			}

			Integer ticksToRequest = args.goalMap.get(i);
			if (ticksToRequest == null){
				ticksToRequest = 0;
			}
			
			int floorTicks = 0;
			int counted = 0;
			for (Floor floor : variations.values()){

				int thisTicks = floor.GetTicks(ticksToRequest);
				if (thisTicks > 0){
					floorTicks += thisTicks;
					counted++;
				}

			}

			int averaged = floorTicks / counted;

			ticks += averaged;		

		}

		return ticks;

	}

}
