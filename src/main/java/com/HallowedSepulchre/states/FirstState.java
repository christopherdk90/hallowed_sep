package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variation;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;

@Slf4j
public class FirstState extends State {

    public FirstState(HallowedSepulchrePlugin plugin, Run run, Timer timer, Variation var) {
        super.floor = 1;
        super.run = run;
        super.variation = var;
        super.descriptor = "First Floor " + VarHelper.VarToString(var);
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
        else if (regionID == Regions.FIRST_TRANSITION_REGION 
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
        else if (regionID == Regions.SECOND_REGION_START){
            return new LoadingState(super.plugin, run, timer, 2);
        }
        // else if in world
        else if (!Regions.FIRST_REGIONS.contains(regionID)) {
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
        if (run.first != null) return;

        run.first = new Floor(floor, variation, timer.GetTicks(), super.looted);
        timer.SaveSystemTimeEnd();

    }

}
