package com.HallowedSepulchre;

import com.HallowedSepulchre.states.State;
import com.HallowedSepulchre.states.WorldState;
import com.HallowedSepulchre.runs.Run;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.configs.TimeDisplay;
import com.HallowedSepulchre.helpers.TimeHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.common.base.Strings;
import com.google.inject.Provides;

import javax.inject.Inject;
import java.util.*;

import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PluginDescriptor(
	name = "Hallowed Sepulchre"
)
public class HallowedSepulchrePlugin extends Plugin
{

	private static final String CONFIG_GROUP = "hallowedSepulchre";

	private static final String BEST_RUN_KEY = "bestRun";
	private static final String BEST_FLOORS = "bestFloors";

	private State playerState;

	private Timer timer;

	public Run bestRun;
	public Run optRun;
	public Run lastRun;

	private Map<Integer, Map<Variations, Floor>> bestFloorMap;

	@Inject
	private Client client;

	@Inject
	private HallowedSepulchreConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private Gson gson;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HallowedSepulchreOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Hallowed Sepulchre plugin started!");
		overlayManager.add(overlay);

		// TODO: move this to trigger load when hits lobby for first time
		bestRun = loadBestRunFromJson();
		bestFloorMap = loadBestFloorsMapFromJson();
		optRun = buildOptimalRun();
		if (bestFloorMap == null){
			bestFloorMap = new HashMap<Integer, Map<Variations,Floor>>();
		}

	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Hallowed Sepulchre plugin stopped!");
		super.shutDown();
		overlayManager.remove(overlay);
	}

	public boolean IsInSepulchre(){
		if (playerState == null) return false;
		return playerState.inSepulchre;
	}

	public boolean IsInLobby(){
		if (playerState == null) return false;
		return playerState.inLobby;
	}

	public boolean IsPaused(){
		if (playerState == null) return false;
		return playerState.paused;
	}

	public String GetStateDescriptor(){
		return playerState.descriptor;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{

		Player player = client.getLocalPlayer();

		if (player == null) {
			return;
		}
		if (playerState == null) {
			timer = new Timer();
			playerState = new WorldState(timer, 0);
		}

		// Player returned to lobby after completing a run
		if (playerState.inLobby && playerState.run != null){
			
			log.debug("Run completed, processing...");

			// Process it before doing anything
			playerState.run.Process();
			
			// Save last run and clear from state
			lastRun = playerState.run;
			playerState.run = null;

			// Evaluate last run against current best
			bestRun = Run.GetBestRun(bestRun, lastRun);

			// Evaluate last run floors against current opt
			optRun = Run.GetOptRun(optRun, lastRun);

			addRunToBestFloorMap(lastRun);

			saveBestRunToJson(bestRun);

			saveBestFloorsMapToJson(bestFloorMap);

			if (lastRun.first != null){
				log.debug("Looted: " + lastRun.first.looted);
			}

		}

		WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, player.getLocalLocation());

		playerState.xPos = worldPoint.getX();
		playerState.yPos = worldPoint.getY();
		playerState.plane = worldPoint.getPlane();
		int regionID = worldPoint.getRegionID();

		if (config.logRegionID()){
			log.debug("RegionID: " + regionID);
		}

		playerState = playerState.nextState(regionID);

		playerState.Tick();

	}

	public String GetFloorTime(){

		if (timer == null){
			return "";
		}
		if (config.timeDisplay() == TimeDisplay.TICKS){
			return timer.GetTicks() + "";
		}
		return TimeHelper.GetTimeFromSysTime(timer.GetSystemTimeStart(), timer.GetSystemTimeEnd(), 0);
	}

	public String GetCumulativeTime(){
		if (timer == null){
			return "";
		}
		if (config.timeDisplay() == TimeDisplay.TICKS){
			return (timer.GetTicks() + timer.GetCumulativeTicks()) + "";
		}
		return TimeHelper.GetTimeFromSysTime(timer.GetSystemTimeStart(), timer.GetSystemTimeEnd(), timer.GetCumulativeSystemTime());
	}

	public String DebugCoords(){

		return "X: " + playerState.xPos + " Y: " + playerState.yPos;

	}

	private void addRunToBestFloorMap(Run run){

		// Null check done inside function
		safeAddFloorToBestFloorMap(run.first);
		safeAddFloorToBestFloorMap(run.second);
		safeAddFloorToBestFloorMap(run.third);
		safeAddFloorToBestFloorMap(run.fourth);
		safeAddFloorToBestFloorMap(run.fifth);
		
	}

	private void saveBestRunToJson(Run run){

		String json = gson.toJson(run);
		configManager.setConfiguration(CONFIG_GROUP, BEST_RUN_KEY, json);

	}

	private Run loadBestRunFromJson(){

		String json = configManager.getConfiguration(CONFIG_GROUP, BEST_RUN_KEY);
		if (Strings.isNullOrEmpty(json))
		{
			return null;
		}

		// CHECKSTYLE:OFF
		return gson.fromJson(json, new TypeToken<Run>(){}.getType());
		// CHECKSTYLE:ON

	}

	private void saveBestFloorsMapToJson(Map<Integer,Map<Variations,Floor>> bestFloors){

		String json = gson.toJson(bestFloors);
		log.debug("Floors map: " + json);
		configManager.setConfiguration(CONFIG_GROUP, BEST_FLOORS, json);

	}

	private Map<Integer,Map<Variations,Floor>> loadBestFloorsMapFromJson(){

		String json = configManager.getConfiguration(CONFIG_GROUP, BEST_FLOORS);
		if (Strings.isNullOrEmpty(json))
		{
			return null;
		}

		Map<Integer,Map<Variations,Floor>> bestFloorMap = gson.fromJson(json, new TypeToken<Map<Integer,Map<Variations,Floor>>>(){}.getType());

		if (bestFloorMap != null){
			log.debug("Loaded best floor map");
		}

		// CHECKSTYLE:OFF
		return bestFloorMap;
		// CHECKSTYLE:ON

	}

	private void safeAddFloorToBestFloorMap(Floor newFloor){

		if (newFloor == null) return;

		Map<Variations, Floor> variationsMap = bestFloorMap.get(newFloor.floor);
		// If floor hasn't been seen yet, make a map for it
		if (variationsMap == null){
			variationsMap = new HashMap<Variations, Floor>();
			bestFloorMap.put(newFloor.floor, variationsMap);
		}
		
		Floor current = variationsMap.get(newFloor.variation);
		// If variation hasn't been seen yet, this new one is best
		if (current == null) {
			variationsMap.put(newFloor.variation, newFloor);
			return;
		}

		// Now we have the current best and the newest run
		// Compare ticks and overwrite if new is better
		if (current.ticks > newFloor.ticks){
			variationsMap.put(newFloor.variation, newFloor);
		}
		
	}

	private Run buildOptimalRun(){

		if (bestFloorMap == null) {
			return null;
		}

		Run optimal = new Run();

		Map<Variations, Floor> first = bestFloorMap.get(Regions.FIRST_FLOOR);
		if (first != null){
			for (Floor variation : first.values()) {
				if (optimal.first == null || variation.ticks < optimal.first.ticks){
					optimal.first = variation;
				}
			}
		}
		Map<Variations, Floor> second = bestFloorMap.get(Regions.SECOND_FLOOR);
		if (second != null){
			for (Floor variation : second.values()) {
				if (optimal.second == null || variation.ticks < optimal.second.ticks){
					optimal.second = variation;
				}
			}
		}
		Map<Variations, Floor> third = bestFloorMap.get(Regions.THIRD_FLOOR);
		if (third != null){
			for (Floor variation : third.values()) {
				if (optimal.third == null || variation.ticks < optimal.third.ticks){
					optimal.third = variation;
				}
			}
		}
		Map<Variations, Floor> fourth = bestFloorMap.get(Regions.FOURTH_FLOOR);
		if (fourth != null){
			for (Floor variation : fourth.values()) {
				if (optimal.fourth == null || variation.ticks < optimal.fourth.ticks){
					optimal.fourth = variation;
				}
			}
		}
		Map<Variations, Floor> fifth = bestFloorMap.get(Regions.FIFTH_FLOOR);
		if (fifth != null){
			for (Floor variation : fifth.values()) {
				if (optimal.fifth == null || variation.ticks < optimal.fifth.ticks){
					optimal.fifth = variation;
				}
			}
		}

		optimal.Process();

		return optimal;

	}

	public String getBestFloorTime(){

		if (playerState == null) return "";

		if (playerState.floor < 1) return "";

		Map<Variations, Floor> variationMap = bestFloorMap.get(playerState.floor);

		// Floor has not been seen
		if (variationMap == null) {
			return "--:--";
		}

		Floor floor = variationMap.get(playerState.variation);

		// This variation has not been seen
		if (floor == null) {
			return "--:--";
		}

		return TimeHelper.GetTimeFromTicks(floor.ticks);

	}

	@Provides
	HallowedSepulchreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HallowedSepulchreConfig.class);
	}

}
