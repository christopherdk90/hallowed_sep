package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorldState extends State {

    public WorldState(HallowedSepulchrePlugin plugin, Timer timer) {
        super.floor = -1;
        super.inSepulchre = false;
        super.inLobby = false;
        super.timer = timer;

        super.plugin = plugin;

        log.debug("Not in Hallowed Sepulchre, regionID: ");

    }

    public State nextState(){
        if (regionID == Regions.LOBBY) {
            log.debug("Moving to lobby state");
            return new LobbyState(super.plugin, null, timer);
        }
        return this;
    }
    
}
