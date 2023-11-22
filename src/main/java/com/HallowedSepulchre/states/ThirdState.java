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
public class ThirdState extends State {
    
    private boolean grappled;
    private boolean second_looted;

    public ThirdState(HallowedSepulchrePlugin plugin, Run run, Timer timer, Variation var) {
        super.floor = 3;
        super.run = run;
        super.variation = var;
        super.descriptor = "Third Floor " + VarHelper.VarToString(var);
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
        if (plane == Regions.START_PLANE){
            if (!grappled && (Regions.THIRD_FLOOR_GRAPPLE_E.Equals(xPos, yPos) ||
                Regions.THIRD_FLOOR_GRAPPLE_W.Equals(xPos, yPos))){
                grappled = true;
                log.debug(super.descriptor + " looted");
            }
            else if (!second_looted && (Regions.THIRD_FLOOR_BRAZIER.Equals(xPos, yPos) || 
                Regions.THIRD_FLOOR_PORTAL.Equals(xPos, yPos))){
                second_looted = true;
                log.debug(super.descriptor + " looted");
            }
        }
        else if (plane == Regions.FINISH_PLANE){
            // Two finishing jump tiles trigger clock to pause
            if (Regions.THIRD_FINISH_E.Equals(xPos, yPos) ||
                Regions.THIRD_FINISH_W.Equals(xPos, yPos)){
                Save();
            }
        }
        // Player has clicked the stairs
        else if (regionID == Regions.FOURTH_REGION_START){
            return new LoadingState(super.plugin, run, timer, 4);
        }
        // else if in world
        else if (regionID != Regions.THIRD_REGION_START){
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
        if (run.third != null) return;

        looted = 0;
        if (grappled) {
            looted++;
        }
        if (second_looted){
            looted++;
        }

        run.third = new Floor(floor, variation, timer.GetTicks(), looted);
        timer.SaveSystemTimeEnd();
    }

}
