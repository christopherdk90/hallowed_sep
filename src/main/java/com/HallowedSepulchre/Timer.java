package com.HallowedSepulchre;

public class Timer {
    
    private int tickCounter;

    private int cumulativeTickCounter;

    private Long systemTimeStart;
    private Long systemTimeEnd;

    private long cumulativeSystemTime;

    private boolean paused;

    public void ResetTicks(){
        tickCounter = 0;
        systemTimeStart = null;
    }

    public void ResetAll(){
        tickCounter = 0;
        cumulativeTickCounter = 0;

        systemTimeStart = null;
        cumulativeSystemTime = 0;
    }

    public void Increment(){
        if (!paused && tickCounter < 10000){
            tickCounter++;
        }
    }

    public int GetTicks(){
        return tickCounter;
    }

    public int GetCumulativeTicks(){
        return cumulativeTickCounter;
    }

    public void TogglePause(boolean pause){
        this.paused = pause;
    }

    public void SaveToCumulative(){
        cumulativeTickCounter += tickCounter;
        if (systemTimeEnd != null && systemTimeStart != null) {
            cumulativeSystemTime += systemTimeEnd - systemTimeStart;
        }
        
        systemTimeEnd = null;
    }

    public void SaveSystemTimeStart(){
        systemTimeStart = System.currentTimeMillis();
    }

    public Long GetSystemTimeStart(){
        return systemTimeStart;
    }

    public void SaveSystemTimeEnd(){
        systemTimeEnd = System.currentTimeMillis();
    }

    public Long GetSystemTimeEnd(){
        return systemTimeEnd == null ? System.currentTimeMillis() : systemTimeEnd;
    }

    public long GetCumulativeSystemTime(){
        return cumulativeSystemTime;
    }

}
