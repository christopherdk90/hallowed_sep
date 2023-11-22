package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variation;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FifthState extends State {
    
    private boolean portaled;
    private boolean grappled;
    private boolean bridged;

    public FifthState(HallowedSepulchrePlugin plugin, Run run, Timer timer, Variation var) {
        super.floor = 5;
        super.run = run;
        super.variation = var;
        super.descriptor = "Fifth Floor";
        super.inSepulchre = true;
        super.timer = timer;
        super.paused = true;

        timer.ResetTicks();

        super.plugin = plugin;

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

        // Either returned to lobby or finished floor
        if (regionID == Regions.LOBBY){
            return new LobbyState(super.plugin, run, timer);
        }
        if (plane == Regions.FIFTH_MIDDLE_PLANE){
            if (!portaled && Regions.FIFTH_FLOOR_PORTAL.Equals(xPos, yPos)){
                portaled = true;
                log.debug(super.descriptor + " looted");
            }
            else if (!grappled && Regions.FIFTH_FLOOR_GRAPPLE.Equals(xPos, yPos)){
                grappled = true;
                log.debug(super.descriptor + " looted");
            }
        }
        // 
        else if (plane == Regions.FIFTH_FINISH_PLANE){
            if (!bridged && Regions.FIFTH_FLOOR_BRIDGE.Equals(xPos, yPos)){
                bridged = true;
                log.debug(super.descriptor + " looted");
            }
            else if (Regions.FIFTH_FINISH_COORD.Equals(xPos, yPos)) {
                Save();
            }
        }
        // else if in world
        else if (!Regions.FIFTH_REGIONS.contains(regionID)){
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
        if (run.fifth != null) return;

        looted = 0;
        if (portaled) {
            looted++;
        }
        if (grappled){
            looted++;
        }
        if (bridged){
            looted++;
        }

        run.fifth = new Floor(floor, variation, timer.GetTicks(), looted);
        timer.SaveSystemTimeEnd();
    }

}
