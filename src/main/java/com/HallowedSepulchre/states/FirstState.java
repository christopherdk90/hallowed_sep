package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

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
        else if (region == Regions.FIRST_TRANSITION 
            && plane == Regions.FIRST_END_PLANE
            && Regions.FIRST_END_E.Equals(xPos, yPos)) {
            Save();
        }
        else if (region == Regions.FIRST_TRANSITION 
            && plane == Regions.FIRST_END_PLANE
            && Regions.FIRST_END_W.Equals(xPos, yPos)) {
            Save();
        }
        // Player has clicked the stairs
        else if (region == Regions.SECOND_START){
            return new LoadingState(run, timer, 2);
        }
        // else if region is in group of first regions, still in first floor
        // else in world
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

        run.first = new Floor(floor, variation, timer.GetTicks());
        timer.SaveSystemTimeEnd();

    }

}