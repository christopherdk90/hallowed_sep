package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.constants.Regions;
import com.HallowedSepulchre.constants.Variation;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecondState extends State {
    
    public SecondState(HallowedSepulchrePlugin plugin, Run run, Timer timer, Variation var) {
        super.floor = 2;
        super.run = run;
        super.variation = var;
        super.descriptor = "Second Floor " + VarHelper.VarToString(var);
        super.inSepulchre = true;
        super.timer = timer;
        super.paused = true;

        super.plugin = plugin;

        timer.ResetTicks();

        log.debug("Starting " + super.descriptor);

        plugin.loadFloor(floor, var);

    }

    public State nextState(){

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

        if (regionID == Regions.LOBBY){
            return new LobbyState(super.plugin, run, timer);
        }
        else if (regionID == Regions.SECOND_REGION_START){
            if (looted == 0 && plane == Regions.START_PLANE){
                if (Regions.SECOND_FLOOR_BRAZIER_E.Equals(xPos, yPos) ||
                    Regions.SECOND_FLOOR_BRAZIER_W.Equals(xPos, yPos) ||
                    Regions.SECOND_FLOOR_GRAPPLE_E.Equals(xPos, yPos) ||
                    Regions.SECOND_FLOOR_GRAPPLE_W.Equals(xPos, yPos)){
                    looted = 1;
                    log.debug(super.descriptor + " looted");
                }
            }
            else if (plane == Regions.FINISH_PLANE){
                // Four finishing jump tiles trigger clock to pause
                if (Regions.SECOND_FINISH_N.Equals(xPos, yPos) ||
                    Regions.SECOND_FINISH_E.Equals(xPos, yPos) ||
                    Regions.SECOND_FINISH_S.Equals(xPos, yPos) ||
                    Regions.SECOND_FINISH_W.Equals(xPos, yPos)){
                    Save();
                }
            }
        }
        // Player has clicked the stairs
        else if (regionID == Regions.THIRD_REGION_START){
            return new LoadingState(super.plugin, run, timer, 3);
        }
        // else if in world
        else if (!Regions.SECOND_REGIONS.contains(regionID)){
            return new WorldState(super.plugin, timer);
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

        run.second = new Floor(floor, variation, timer.GetTicks(), looted);
        timer.SaveSystemTimeEnd();
    }

}
