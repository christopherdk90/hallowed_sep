package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

public class FourthState extends State {
    
    public FourthState(Run run, Timer timer, Variations var) {
        super.floor = 4;
        super.run = run;
        super.variation = var;
        super.descriptor = "Fourth Floor " + VarHelper.VarToString(var);
        super.inSepulchre = true;
        super.timer = timer;
        super.paused = true;

        timer.ResetTicks();
        
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
            && Regions.FOURTH_FINISH_COORD.Equals(xPos, yPos)) {
            Save();
        }
        else if (region == Regions.FIFTH_REGION_START){
            return new LoadingState(run, timer, 5);
        }
        // else if in world
        else if (!Regions.FOURTH_REGIONS.contains(region)){
            return new WorldState(timer);
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
        if (run.fourth != null) return;

        run.fourth = new Floor(floor, variation, timer.GetTicks());
        timer.SaveSystemTimeEnd();
    }

}
