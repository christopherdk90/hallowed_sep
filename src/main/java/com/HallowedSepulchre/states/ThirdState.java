package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThirdState extends State {
    
    public ThirdState(Run run, Timer timer, Variations var) {
        super.floor = 3;
        super.run = run;
        super.variation = var;
        super.descriptor = "Third Floor " + VarHelper.VarToString(var);
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

        if (region == Regions.LOBBY){
            return new LobbyState(run, timer);
        }
        // Two finishing jump tiles trigger clock to pause
        else if (plane == Regions.FINISH_PLANE 
            && Regions.THIRD_FINISH_E.Equals(xPos, yPos)) {
            Save();
        }
        else if (plane == Regions.FINISH_PLANE 
            && Regions.THIRD_FINISH_W.Equals(xPos, yPos)) {
            Save();
        }
        // Player has clicked the stairs
        else if (region == Regions.FOURTH_REGION_START){
            return new LoadingState(run, timer, 4);
        }
        // else if in world
        else if (region != Regions.THIRD_REGION_START){
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
        if (run.third != null) return;

        run.third = new Floor(floor, variation, timer.GetTicks(), super.looted);
        timer.SaveSystemTimeEnd();
    }

}
