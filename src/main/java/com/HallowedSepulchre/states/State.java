package com.HallowedSepulchre.states;

import com.HallowedSepulchre.Timer;
import com.HallowedSepulchre.Variations;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

public abstract class State {

    public int floor = -1;

    public int buffer = 4;

    public Variations variation = Variations.None;

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

    public State nextState(int region){
        return this;
    }

    public void Tick(){}

}