package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LobbyState extends State {
    
    public LobbyState(Run run, Timer timer) {
        super.floor = 0;
        super.run = run;
        super.descriptor = "In Lobby";
        super.inSepulchre = true;
        super.inLobby = true;
        super.timer = timer;

        log.debug("Entered Hallowed Sepulchre lobby");

        timer.ResetAll();

    }

    public State nextState(int region){
        if (region == Regions.LOBBY) {
            return this;
        }
        else if (region == Regions.FIRST_TRANSITION_REGION){
            return new LoadingState(run, timer, 1);
        }
        else {
            return new WorldState(timer, region);
        }
    }

}
