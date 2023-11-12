package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

public class SecondState extends State {
    
    public SecondState(Run run, Timer timer, Variations var) {
        super.floor = 2;
        super.run = run;
        super.variation = var;
        super.descriptor = "Second Floor " + VarHelper.VarToString(var);
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
        // Four finishing jump tiles trigger clock to pause
        else if (plane == Regions.FINISH_PLANE 
            && Regions.SECOND_FINISH_N.Equals(xPos, yPos)) {
            Save();
        }
        else if (plane == Regions.FINISH_PLANE 
            && Regions.SECOND_FINISH_E.Equals(xPos, yPos)) {
            Save();
        }
        else if (plane == Regions.FINISH_PLANE 
            && Regions.SECOND_FINISH_S.Equals(xPos, yPos)) {
            Save();
        }
        else if (plane == Regions.FINISH_PLANE 
            && Regions.SECOND_FINISH_W.Equals(xPos, yPos)) {
            Save();
        }
        // Player has clicked the stairs
        else if (region == Regions.THIRD_REGION_START){
            return new LoadingState(run, timer, 3);
        }
        // else if in world
        else if (!Regions.SECOND_REGIONS.contains(region)){
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
        if (run.second != null) return;

        run.second = new Floor(floor, variation, timer.GetTicks());
        timer.SaveSystemTimeEnd();
    }

}
