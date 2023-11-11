package com.HallowedSepulchre.helpers;

public class TimeHelper {
    
    public static String GetTimeFromTicks(int ticks){
		
		int totalSeconds = getSecondsFromTicks(ticks);
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;

		return minutes + ":" + String.format("%02d", seconds);

	}

	private static int getSecondsFromTicks(int ticks){

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

}
