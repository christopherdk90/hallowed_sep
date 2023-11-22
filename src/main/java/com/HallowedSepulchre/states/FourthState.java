package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variation;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FourthState extends State {
    
    private boolean first_looted;
    private boolean braziered;

    public FourthState(HallowedSepulchrePlugin plugin, Run run, Timer timer, Variation var) {
        super.floor = 4;
        super.run = run;
        super.variation = var;
        super.descriptor = "Fourth Floor " + VarHelper.VarToString(var);
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
        else if (!first_looted && plane == Regions.START_PLANE){
            if (Regions.FOURTH_FLOOR_BRIDGE.Equals(xPos, yPos) ||
                Regions.FOURTH_FLOOR_PORTAL.Equals(xPos, yPos)){
                first_looted = true;
                log.debug(super.descriptor + " looted");
            }
        }
        // Two finishing jump tiles trigger clock to pause
        else if (plane == Regions.FINISH_PLANE) {
            if (!braziered && Regions.FOURTH_FLOOR_BRAZIER.Equals(xPos, yPos)){
                braziered = true;
                log.debug(super.descriptor + " looted");
            }
            else if (Regions.FOURTH_FINISH_COORD.Equals(xPos, yPos)){
                Save();
            }            
        }
        else if (regionID == Regions.FIFTH_REGION_START){
            return new LoadingState(super.plugin, run, timer, 5);
        }
        // else if in world
        else if (!Regions.FOURTH_REGIONS.contains(regionID)){
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
        if (run.fourth != null) return;

        looted = 0;
        if (first_looted){
            looted++;
        }
        if (braziered){
            looted++;
        }

        run.fourth = new Floor(floor, variation, timer.GetTicks(), looted);
        timer.SaveSystemTimeEnd();
    }

}
