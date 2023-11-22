package com.HallowedSepulchre.helpers;

import com.HallowedSepulchre.configs.TimeDisplay;

public class TimeHelper {
    
    public static String GetTimeFromTicks(int ticks){
		
		int totalSeconds = getSecondsFromTicks(ticks);
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;

		return minutes + ":" + String.format("%02d", seconds);

	}

	public static String GetTimeFromSeconds(int totalSeconds){
		
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;

		return minutes + ":" + String.format("%02d", seconds);

	}

	public static int getSecondsFromTicks(int ticks){

		return Math.round((ticks * 6) / 10f);

	}

	public static String GetTimeFromSysTime(Long start, long end, long cumulative){

		if (start == null) {
			start = end;
		}

		long delta = end - start; // ms since time

		long totalSeconds = Math.round(delta / 1000f); // s since time

		long totalCumulativeSeconds = Math.round(cumulative / 1000f); // s of cumulative
		totalSeconds += totalCumulativeSeconds;

		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60; 

		return minutes + ":" + String.format("%02d", seconds);

	}

	public static Long GetTimeDelta(Long start, long end){

		if (start == null) {
			return 0L;
		}

		long delta = end - start; // ms since time

		return (long)Math.round(delta / 1000f); // s since time

	}

	public static String GetTimeFromSysTime(Long start, long end, int cumulativeTicks){

		long totalSeconds = GetTimeDelta(start, end);

		totalSeconds += getSecondsFromTicks(cumulativeTicks);

		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60; 

		return minutes + ":" + String.format("%02d", seconds);

	}

	public static String GetTimeFromSysTime(long totalSeconds, int cumulativeTicks){

		totalSeconds += getSecondsFromTicks(cumulativeTicks);

		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60; 

		return minutes + ":" + String.format("%02d", seconds);

	}

	public static String nilTime(TimeDisplay display){

		if (display == TimeDisplay.TICKS){
			return "--";
		}

		return "--:--";

	}

	public static String FormatTimeFromTicks(TimeDisplay display, int ticks){
		if (display == TimeDisplay.TICKS){
            return ticks + "t";
        }

        return TimeHelper.GetTimeFromTicks(ticks);

	}
	

}
