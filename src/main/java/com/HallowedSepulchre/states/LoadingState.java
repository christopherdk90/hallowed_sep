package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variation;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadingState extends State {
    
    public LoadingState(HallowedSepulchrePlugin plugin, Run run, Timer timer, int floor) {
        super.run = run;
        super.floor = floor;
        super.descriptor = "Loading";
        super.inSepulchre = true;
        super.inLobby = false;
        super.timer = timer;

        super.plugin = plugin;

        log.debug("Entering loading state");

        timer.SaveToCumulative();
        timer.ResetTicks();

    }

    public State nextState(){
        // First floor starts are broken up by region
        if (floor == Regions.FIRST_FLOOR) {
            if (regionID == Regions.FIRST_REGION_START_N){
                return new FirstState(super.plugin, run, timer, Variation.North);
            }
            else if (regionID == Regions.FIRST_REGION_START_E){
                return new FirstState(super.plugin, run, timer, Variation.East);
            }
            else if (regionID == Regions.FIRST_REGION_START_S){
            return new FirstState(super.plugin, run, timer, Variation.South);
            }
            else if (regionID == Regions.FIRST_REGION_START_W){
                return new FirstState(super.plugin, run, timer, Variation.West);
            }
        }
        //  Second floor starts in same region, but at different positions per variation
        if (floor == Regions.SECOND_FLOOR && regionID == Regions.SECOND_REGION_START) {
            if (Regions.SECOND_START_N.Equals(xPos, yPos)){
                return new SecondState(super.plugin, run, timer, Variation.North);
            }
            else if (Regions.SECOND_START_E.Equals(xPos, yPos)){
                return new SecondState(super.plugin, run, timer, Variation.East);
            }
            else if (Regions.SECOND_START_S.Equals(xPos, yPos)){
                return new SecondState(super.plugin, run, timer, Variation.South);
            }
            else if (Regions.SECOND_START_W.Equals(xPos, yPos)){
                return new SecondState(super.plugin, run, timer, Variation.West);
            }
        }
        //  Third floor starts in same region, but at different positions per variation
        if (floor == Regions.THIRD_FLOOR && regionID == Regions.THIRD_REGION_START) {
            if (Regions.THIRD_START_E.Equals(xPos, yPos)){
                return new ThirdState(super.plugin, run, timer, Variation.East);
            }
            else if (Regions.THIRD_START_W.Equals(xPos, yPos)){
                return new ThirdState(super.plugin, run, timer, Variation.West);
            }
        }
        //  Fourth floor starts in same region, but at different positions per variation
        if (floor == Regions.FOURTH_FLOOR && regionID == Regions.FOURTH_REGION_START) {
            if (Regions.FOURTH_START_N.Equals(xPos, yPos)){
                return new FourthState(super.plugin, run, timer, Variation.North);
            }
            else if (Regions.FOURTH_START_S.Equals(xPos, yPos)){
                return new FourthState(super.plugin, run, timer, Variation.South);
            }
        }
        //  Fifth floor has one start point
        if (floor == Regions.FIFTH_FLOOR && regionID == Regions.FIFTH_REGION_START) {
            if (Regions.FIFTH_START_COORD.Equals(xPos, yPos)){
                return new FifthState(super.plugin, run, timer, Variation.North);
            }
        }
        return this;
    }

}
