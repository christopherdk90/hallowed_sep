package com.HallowedSepulchre.states;

import com.HallowedSepulchre.HallowedSepulchrePlugin;
import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.constants.Variation;
import com.HallowedSepulchre.runs.Run;

public abstract class State {

    public int floor = -1;

    public int buffer = 4;

    public Variation variation = Variation.None;

    public int regionID = 0;

    public int xPos = -1;
    public int yPos = -1;

    public int plane = 0;

    public int looted = 0;

    public String descriptor;

    public boolean inSepulchre;
    public boolean inLobby;

    public boolean paused;

    public Timer timer;

    public Run run;

    protected HallowedSepulchrePlugin plugin;

    public State nextState(){
        return this;
    }

    public void Tick(){}

}