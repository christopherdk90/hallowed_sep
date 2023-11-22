package com.HallowedSepulchre.data;

import net.runelite.client.config.ConfigManager;

import com.google.gson.Gson;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import com.HallowedSepulchre.constants.Variation;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.runs.Run;

public class DataManager {

    private static final String CONFIG_GROUP = "hallowedSepulchre";

	private static final String BEST_RUN_KEY = "bestRun";
	private static final String BEST_FLOORS = "bestFloors";
    
    public static Run loadBestRunFromJson(ConfigManager configManager, Gson gson){

        String json = configManager.getConfiguration(CONFIG_GROUP, BEST_RUN_KEY);
		if (Strings.isNullOrEmpty(json))
		{
			return null;
		}

		Run run = gson.fromJson(json, new TypeToken<Run>(){}.getType());

		if (run != null){
			run.Process();
		}

		return run;

    }

    public static void saveBestRunToJson(ConfigManager configManager, Gson gson, Run run){

		String json = gson.toJson(run);
		configManager.setConfiguration(CONFIG_GROUP, BEST_RUN_KEY, json);

	}

    public static void saveBestFloorsMapToJson(ConfigManager configManager, Gson gson, Map<Integer,Map<Variation,Floor>> bestFloors){

		String json = gson.toJson(bestFloors);
		configManager.setConfiguration(CONFIG_GROUP, BEST_FLOORS, json);

	}

	public static Map<Integer,Map<Variation,Floor>> loadBestFloorsMapFromJson(ConfigManager configManager, Gson gson){

		String json = configManager.getConfiguration(CONFIG_GROUP, BEST_FLOORS);
		if (Strings.isNullOrEmpty(json))
		{
			return null;
		}

		Map<Integer,Map<Variation,Floor>> bestFloorMap = gson.fromJson(json, new TypeToken<Map<Integer,Map<Variation,Floor>>>(){}.getType());

		return bestFloorMap;

	}

}
