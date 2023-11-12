package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirstState extends State {

    public FirstState(Run run, Timer timer, Variations var) {
        super.floor = 1;
        super.run = run;
        super.variation = var;
        super.descriptor = "First Floor " + VarHelper.VarToString(var);
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
        else if (region == Regions.FIRST_TRANSITION_REGION 
            && plane == Regions.FINISH_PLANE) {
                // Two finishing jump tiles trigger clock to pause
                if (Regions.FIRST_END_E.Equals(xPos, yPos) 
                || Regions.FIRST_END_W.Equals(xPos, yPos)) {
                    Save();
                }
                else if (Regions.FIRST_FLOOR_BRIDGE.Equals(xPos, yPos) 
                || Regions.FIRST_FLOOR_GRAPPLE.Equals(xPos, yPos)){
                    super.looted = 1;
                }
        }

        // Player has clicked the stairs
        else if (region == Regions.SECOND_REGION_START){
            return new LoadingState(run, timer, 2);
        }
        // else if in world
        else if (!Regions.FIRST_REGIONS.contains(region)) {
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
        if (run.first != null) return;

        run.first = new Floor(floor, variation, timer.GetTicks(), super.looted);
        timer.SaveSystemTimeEnd();

    }

}
