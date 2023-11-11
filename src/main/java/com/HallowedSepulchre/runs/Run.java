package com.HallowedSepulchre.runs;

import com.HallowedSepulchre.helpers.TimeHelper;

public class Run {
    
    public int totalTicks;
    public int totalFloors;

    public Floor first;
    public Floor second;
    public Floor third;
    public Floor fourth;
    public Floor fifth;

    public void Process(){
        
        totalTicks = 0;

        if (first != null){
            totalFloors = 1;
            totalTicks += first.ticks;
        }
        if (second != null){
            totalFloors = 2;
            totalTicks += second.ticks;
        }
        if (third != null){
            totalFloors = 3;
            totalTicks += third.ticks;
        }
        if (fourth != null){
            totalFloors = 4;
            totalTicks += fourth.ticks;
        }
        if (fifth != null){
            totalFloors = 5;
            totalTicks += fifth.ticks;
        }
    }

    public String DisplayTime() {
        return TimeHelper.GetTimeFromTicks(totalTicks);
    }

    public static Run GetBestRun(Run original, Run newest){

        // No best run stored yet, so the newest run is best
        if (original == null){
            return newest;
        }

        // Return original best if the new run has less floors
        if (newest.totalFloors < original.totalFloors){
            return original;
        }

        // Return the newest if it has more floors than current best
        if (newest.totalFloors > original.totalFloors){
            return newest;
        }

        // Both runs now definitely have the same amount of floors completed

        // Return original if it was done in fewer ticks
        // Otherwise, return newer
        return original.totalTicks < newest.totalTicks ? original : newest;

    }

    public static Run GetOptRun(Run original, Run newest){

        // No opt run stored yet, so the newest run is opt
        if (original == null){
            return newest;
        }

        // Otherwise take best of each floor
        if (newest.first != null){
            if (original.first == null){
                original.first = newest.first;
            } else {
                original.first = original.first.ticks < newest.first.ticks ? original.first : newest.first;
            }
        }
        if (newest.second != null){
            if (original.second == null){
                original.second = newest.second;
            } else {
                original.second = original.second.ticks < newest.second.ticks ? original.second : newest.second;
            }
        }
        if (newest.third != null){
            if (original.third == null){
                original.third = newest.third;
            } else {
                original.third = original.third.ticks < newest.third.ticks ? original.third : newest.third;
            }
        }
        if (newest.fourth != null){
            if (original.fourth == null){
                original.fourth = newest.fourth;
            } else {
                original.fourth = original.fourth.ticks < newest.fourth.ticks ? original.fourth : newest.fourth;
            }
        }
        if (newest.fifth != null){
            if (original.fifth == null){
                original.fifth = newest.fifth;
            } else {
                original.fifth = original.fifth.ticks < newest.fifth.ticks ? original.fifth : newest.fifth;
            }
        }

        original.Process(); // recalculate

        return original;

    }

}
