package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Regions;
import com.HallowedSepulchre.Timer;

public class WorldState extends State {

    public WorldState(Timer timer) {
        super.floor = -1;
        super.inSepulchre = false;
        super.inLobby = false;
        super.timer = timer;
    }

    public State nextState(int region){
        if (region == Regions.LOBBY) {
            return new LobbyState(null, timer);
        }
        return this;
    }
    
}
