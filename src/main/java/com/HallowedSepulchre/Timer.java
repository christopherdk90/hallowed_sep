package com.HallowedSepulchre;

import com.HallowedSepulchre.data.LoadFloorArgs;
import com.HallowedSepulchre.data.RunManager;
import com.HallowedSepulchre.helpers.TimeHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

public class Timer {
    
    // Should timer count time
    // Paused at start of run and ends
    private boolean paused;

    // Current floor
    private Floor currentFloor;

    // ticks since start of floor
    private int tickCounter;

    // ticks for previous floors in a run (does not include current)
    private int cumulativeTickCounter;

    // SysTime start and end
    private Long systemTimeStart;
    private Long systemTimeEnd;
    // Total seconds for previous floors
    private long cumulativeSystemTime;

    
    // Best overall for floor + variation
    public int bestFloorTicks;
    // Best for floor + variation + loot goal
    public int goalFloorTicks;

    public int futureFloorTicks;

    // 200 ticks or 120 seconds per floor that player will complete
	public int totalFloorTicks;
	public int totalFloorSeconds;

    // Estimation for how long it will take player to complete remaining floors
	public int projectedFloorTicks;
    public long projectedFloorSeconds;

    // Buffer for each floor
    public int bufferTicks;

    // Time left over (total - projected)
	public int remainingFloorTicks;
	public long remainingFloorSeconds;

    // Runs
    public Run bestRun = new Run();
    public Run optRun = new Run();
	public Run lastRun = new Run();


    /*
        Resetting and Loading
     */

    public void ResetTicks(){
        tickCounter = 0;
        systemTimeStart = null;
    }

    public void ResetAll(){
        tickCounter = 0;
        cumulativeTickCounter = 0;

        systemTimeStart = null;
        cumulativeSystemTime = 0;
    }

    public void LoadNewFloor(){
        bestFloorTicks = -1;
        goalFloorTicks = -1;
        bufferTicks = 0;
        futureFloorTicks = -1;
        projectedFloorTicks = -1;
        remainingFloorTicks = -1;
        remainingFloorTicks = -1;
    }

    public void LoadVisitedFloor(LoadFloorArgs args){

        totalFloorTicks = Floor.TICKS_PER_FLOOR * args.highestFloor; 
		totalFloorSeconds = 120 * args.highestFloor;

		bestFloorTicks = args.bestFloorTicks;
        goalFloorTicks = args.goalFloorTicks;
		
		int floorsRemaining = args.highestFloor - args.currentFloor.floor + 1; // Remaining + current
		bufferTicks = floorsRemaining * args.bufferInConfig;

		// Baseline projection
		futureFloorTicks = RunManager.averageFutureGoalFloors(args);

		projectedFloorTicks = GetCumulativeTicks() + goalFloorTicks + futureFloorTicks + bufferTicks;
		projectedFloorSeconds = Math.round(projectedFloorTicks * 0.6f);
		

		remainingFloorTicks = totalFloorTicks - projectedFloorTicks;
		remainingFloorSeconds = totalFloorSeconds - projectedFloorSeconds;


    }

    /*
        Processing
     */

    public void processProjectedAndRemainingSeconds(){

		// Uses pre-calculated times in this instance
		if (GetTicks() <= goalFloorTicks) return;

		projectedFloorSeconds = TimeHelper.GetTimeDelta(GetSystemTimeStart(), GetSystemTimeEnd()) + 
								TimeHelper.getSecondsFromTicks(GetCumulativeTicks() + futureFloorTicks + bufferTicks);
		remainingFloorSeconds = totalFloorSeconds - projectedFloorSeconds;

	}

    public int getProjectedTicks(){

		// This variation has not been seen
		if (currentFloor == null) {
			return -1;
		}

		// Goal not previously met on this variation
		if (goalFloorTicks < 0){
			return -1;
		}

		// If current floor tick time is less than goal, the calculated projected is accurate
		if (tickCounter <= goalFloorTicks) {
			return projectedFloorTicks;
		}

		// projected = cumulative + current time + goal optimal for future floors
		return cumulativeTickCounter + tickCounter + futureFloorTicks;

	}

    /*
        Timer functions
     */

    public void Increment(){
        if (!paused && tickCounter < 10000){
            tickCounter++;
        }
    }

    public int GetTicks(){
        return tickCounter;
    }

    public int GetCumulativeTicks(){
        return cumulativeTickCounter;
    }

    public void TogglePause(boolean pause){
        this.paused = pause;
    }

    public void SaveToCumulative(){
        cumulativeTickCounter += tickCounter;
        if (systemTimeEnd != null && systemTimeStart != null) {
            cumulativeSystemTime += systemTimeEnd - systemTimeStart;
        }
        
        systemTimeEnd = null;
    }

    public void SaveSystemTimeStart(){
        systemTimeStart = System.currentTimeMillis();
    }

    public Long GetSystemTimeNow(){
        return System.currentTimeMillis();
    }

    public Long GetSystemTimeStart(){
        return systemTimeStart;
    }

    public void SaveSystemTimeEnd(){
        systemTimeEnd = System.currentTimeMillis();
    }

    public Long GetSystemTimeEnd(){
        return systemTimeEnd == null ? System.currentTimeMillis() : systemTimeEnd;
    }

    public long GetCumulativeSystemTime(){
        return cumulativeSystemTime;
    }

}
