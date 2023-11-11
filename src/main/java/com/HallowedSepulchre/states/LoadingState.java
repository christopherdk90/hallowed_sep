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
        if (floor == 1) {
            if (region == Regions.FIRST_START_N){
                return new FirstState(run, timer, Variations.North);
            }
            else if (region == Regions.FIRST_START_E){
                return new FirstState(run, timer, Variations.East);
            }
            else if (region == Regions.FIRST_START_S){
            return new FirstState(run, timer, Variations.South);
            }
            else if (region == Regions.FIRST_START_W){
                return new FirstState(run, timer, Variations.West);
            }
        }
        //  Second floor starts in same region, but at different positions per variation
        if (floor == 2 && region == Regions.SECOND_START) {
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
        if (floor == 3 && region == Regions.THIRD_START) {
            if (Regions.THIRD_START_E.Equals(xPos, yPos)){
                return new ThirdState(run, timer, Variations.East);
            }
            else if (Regions.THIRD_START_W.Equals(xPos, yPos)){
                return new ThirdState(run, timer, Variations.West);
            }
        }
        return this;
    }

}
