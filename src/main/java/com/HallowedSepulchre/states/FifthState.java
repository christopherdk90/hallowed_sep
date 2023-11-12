package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FifthState extends State {
    
    public FifthState(Run run, Timer timer, Variations var) {
        super.floor = 5;
        super.run = run;
        super.variation = var;
        super.descriptor = "Fifth Floor";
        super.inSepulchre = true;
        super.timer = timer;
        super.paused = true;

        timer.ResetTicks();

        log.debug("Starting " + super.descriptor);
        
    }

    public State nextState(int region){

        if (buffer == -1){
            // do nothing
        }
        else if (buffer > 0) {
            buffer--;
        }
        else if (buffer == 0){
            super.paused = false;
            timer.SaveSystemTimeStart();
            buffer = -1;
        }

        // Either returned to lobby or finished floor
        if (region == Regions.LOBBY){
            return new LobbyState(run, timer);
        }
        // 
        else if (plane == Regions.FIFTH_FINISH_PLANE 
            && Regions.FIFTH_FINISH_COORD.Equals(xPos, yPos)) {
            Save();
        }
        // else if in world
        else if (!Regions.FIFTH_REGIONS.contains(region)){
            return new WorldState(timer, region);
        }

        return this;
        
    }

    public void Tick(){
        if (!paused) {
            timer.Increment();
        }        
    }

    private void Save(){
        paused = true;
        if (run == null) return;
        if (run.fifth != null) return;

        run.fifth = new Floor(floor, variation, timer.GetTicks(), super.looted);
        timer.SaveSystemTimeEnd();
    }

}
