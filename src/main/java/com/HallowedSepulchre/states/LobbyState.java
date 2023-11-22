package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.constants.Regions;
import com.HallowedSepulchre.runs.Run;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LobbyState extends State {
    
    public LobbyState(HallowedSepulchrePlugin plugin, Run run, Timer timer) {
        super.floor = 0;
        super.run = run;
        super.descriptor = "In Lobby";
        super.inSepulchre = true;
        super.inLobby = true;
        super.timer = timer;
        
        super.plugin = plugin;

        plugin.Initialize();

        log.debug("Entered Hallowed Sepulchre lobby");

        timer.ResetAll();

        plugin.processLastRun(run);

    }

    public State nextState(){
        if (regionID == Regions.LOBBY) {
            return this;
        }
        else if (regionID == Regions.FIRST_TRANSITION_REGION){
            return new LoadingState(super.plugin, new Run(), timer, 1);
        }
        else {
            return new WorldState(plugin, timer);
        }
    }

}
