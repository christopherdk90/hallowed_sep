package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorldState extends State {

    public WorldState(Timer timer, int region) {
        super.floor = -1;
        super.inSepulchre = false;
        super.inLobby = false;
        super.timer = timer;

        log.debug("Not in Hallowed Sepulchre, regionID: " + region);

    }

    public State nextState(int region){
        if (region == Regions.LOBBY) {
            log.debug("Moving to lobby state");
            return new LobbyState(null, timer);
        }
        return this;
    }
    
}
