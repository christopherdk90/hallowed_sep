package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.runs.Run;

public class LoadingState extends State {
    
    public LoadingState(Run run, Timer timer, int floor) {
        super.run = run;
        super.floor = floor;
        super.descriptor = "Loading";
        super.inSepulchre = true;
        super.inLobby = false;
        super.timer = timer;

        if (super.run == null){
            super.run = new Run();
        }

        timer.SaveToCumulative();
        timer.ResetTicks();

    }

    public State nextState(int region){
        // First floor starts are broken up by region
        if (floor == Regions.FIRST_FLOOR) {
            if (region == Regions.FIRST_REGION_START_N){
                return new FirstState(run, timer, Variations.North);
            }
            else if (region == Regions.FIRST_REGION_START_E){
                return new FirstState(run, timer, Variations.East);
            }
            else if (region == Regions.FIRST_REGION_START_S){
            return new FirstState(run, timer, Variations.South);
            }
            else if (region == Regions.FIRST_REGION_START_W){
                return new FirstState(run, timer, Variations.West);
            }
        }
        //  Second floor starts in same region, but at different positions per variation
        if (floor == Regions.SECOND_FLOOR && region == Regions.SECOND_REGION_START) {
            if (Regions.SECOND_START_N.Equals(xPos, yPos)){
                return new SecondState(run, timer, Variations.North);
            }
            else if (Regions.SECOND_START_E.Equals(xPos, yPos)){
                return new SecondState(run, timer, Variations.East);
            }
            else if (Regions.SECOND_START_S.Equals(xPos, yPos)){
                return new SecondState(run, timer, Variations.South);
            }
            else if (Regions.SECOND_START_W.Equals(xPos, yPos)){
                return new SecondState(run, timer, Variations.West);
            }
        }
        //  Third floor starts in same region, but at different positions per variation
        if (floor == Regions.THIRD_FLOOR && region == Regions.THIRD_REGION_START) {
            if (Regions.THIRD_START_E.Equals(xPos, yPos)){
                return new ThirdState(run, timer, Variations.East);
            }
            else if (Regions.THIRD_START_W.Equals(xPos, yPos)){
                return new ThirdState(run, timer, Variations.West);
            }
        }
        //  Fourth floor starts in same region, but at different positions per variation
        if (floor == Regions.FOURTH_FLOOR && region == Regions.FOURTH_REGION_START) {
            if (Regions.FOURTH_START_N.Equals(xPos, yPos)){
                return new FourthState(run, timer, Variations.North);
            }
            else if (Regions.FOURTH_START_S.Equals(xPos, yPos)){
                return new FourthState(run, timer, Variations.South);
            }
        }
        //  Fifth floor has one start point
        if (floor == Regions.FIFTH_FLOOR && region == Regions.FIFTH_REGION_START) {
            if (Regions.FIFTH_START_COORD.Equals(xPos, yPos)){
                return new FifthState(run, timer, Variations.None);
            }
        }
        return this;
    }

}
