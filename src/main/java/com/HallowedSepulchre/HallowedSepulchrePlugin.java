package com.HallowedSepulchre;

import com.HallowedSepulchre.states.State;
import com.HallowedSepulchre.states.WorldState;
import com.HallowedSepulchre.runs.Run;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.configs.OneCoffinDisplay;
import com.HallowedSepulchre.configs.TimeDisplay;
import com.HallowedSepulchre.helpers.TimeHelper;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.data.DataManager;
import com.HallowedSepulchre.data.LoadFloorArgs;
import com.HallowedSepulchre.data.RunManager;

import com.google.gson.Gson;
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

	private State playerState;

	public Timer timer;

	private boolean initialized = false;

	private Floor currentFloor;

	private Map<Integer, Map<Variation, Floor>> bestFloorMap;

	private Map<Integer, Integer> goalMap;

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

		timer = new Timer();
		playerState = new WorldState(this, timer);

	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Hallowed Sepulchre plugin stopped!");
		overlayManager.remove(overlay);
		playerState = null;
		bestFloorMap = null;
		initialized = false;
		timer = null;
		super.shutDown();
		
	}

	public void Initialize(){

		if (timer == null) {
			timer = new Timer();
		}

		if (initialized){
			log.debug("Already initialized");
			return;
		}

		log.debug("Initializing plugin...");

		timer.bestRun = DataManager.loadBestRunFromJson(configManager, gson);
		if (timer.bestRun == null) {
			log.debug("No best run loaded");
		}
		bestFloorMap = DataManager.loadBestFloorsMapFromJson(configManager, gson);
		if (bestFloorMap == null){
			log.debug("No best floor map loaded");
			bestFloorMap = new HashMap<Integer, Map<Variation,Floor>>();
		}
		timer.optRun = buildOptimalRun();
		if (timer.optRun == null){
			log.debug("No opt run built");
		}

		// Covert floor goals to integers
		if (goalMap == null){
			goalMap = buildGoalMap();
		}		

		initialized = true;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{

		Player player = client.getLocalPlayer();

		if (player == null) {
			return;
		}

		WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, player.getLocalLocation());

		playerState.xPos = worldPoint.getX();
		playerState.yPos = worldPoint.getY();
		playerState.plane = worldPoint.getPlane();
		playerState.regionID = worldPoint.getRegionID();

		playerState = playerState.nextState();

		playerState.Tick();

	}

	/*
		Status methods
	 */

	public boolean isInSepulchre(){
		if (playerState == null) return false;
		return playerState.inSepulchre;
	}

	public boolean isInLobby(){
		if (playerState == null) return false;
		return playerState.inLobby;
	}

	public boolean isPaused(){
		if (playerState == null) return false;
		return playerState.paused;
	}

	public int getHighestFloor(){

		if (bestFloorMap == null) return 0;

		return bestFloorMap.size();

	}

	/*
		For display in overlay	
	*/

	public String getStateDescriptor(){
		return playerState.descriptor;
	}

	public String DebugCoords(){

		return "X: " + playerState.xPos + " Y: " + playerState.yPos;

	}

	/*
		Processing
	*/

	public void loadFloor(int floor, Variation variation){

		if (timer == null) {
			return;
		}

		log.debug("Loading floor info");

		currentFloor = safeGetBestFloor(floor, variation);

		// Seeing floor for first time
		if (currentFloor == null){
			log.debug("Floor seen for first time");
			timer.LoadNewFloor();
			return;
		}

		LoadFloorArgs args = new LoadFloorArgs();
		args.bestFloorMap = bestFloorMap;
		args.goalMap = goalMap;
		args.currentFloor = currentFloor;
		args.highestFloor = getHighestFloor();
		args.bufferInConfig = config.projectedBuffer();
		args.bestFloorTicks = currentFloor.GetTicks(0);
		args.goalFloorTicks = currentFloor.GetTicks(goalMap.get(floor));

		timer.LoadVisitedFloor(args);
		
	}

	private Map<Integer, Integer> buildGoalMap(){

		Map<Integer, Integer> goalMap = new HashMap<Integer, Integer>();
			goalMap.put(1, config.firstFloorGoal().ordinal()); 
			goalMap.put(2, config.secondFloorGoal().ordinal()); 
			goalMap.put(3, config.thirdFloorGoal().ordinal()); 
			goalMap.put(4, config.fourthFloorGoal().ordinal()); 
			goalMap.put(5, config.fifthFloorGoal().ordinal()); 

		return goalMap;

	}

	private Run buildOptimalRun(){

		log.debug("Building optimal run...");

		Run opt = Run.buildOptimalRun(
			bestFloorMap, 
			config.firstFloorGoal().ordinal(), 
			config.secondFloorGoal().ordinal(), 
			config.thirdFloorGoal().ordinal(), 
			config.fourthFloorGoal().ordinal(), 
			config.fifthFloorGoal().ordinal()
			);

		opt.Process();

		return opt;
		
	}

	public void processLastRun(Run run){

		if (timer == null) {
			return;
		}

		// No run to process
		// This will happen when going from World State -> Lobby State
		if (run == null) return;
		// Run was started, but no floors completed
		// This will happen when going from First Floor -> Lobby before reaching the end goal
		if (run.first == null) return;

		log.debug("Run completed, processing...");

		// Process it before doing anything
		run.Process();
		
		// Save last run
		timer.lastRun = run;

		/* 
		 	This is strictly comparing run time, regardless of looting status
		*/

		// Evaluate last run against current best
		timer.bestRun = Run.GetBestRun(timer.bestRun, run);

		addRunToBestFloorMap(run);

		DataManager.saveBestRunToJson(configManager, gson, timer.bestRun);

		DataManager.saveBestFloorsMapToJson(configManager, gson, bestFloorMap);

		// Rebuild optimal run
		timer.optRun = buildOptimalRun();

		playerState.run = null;

	}

	private void addRunToBestFloorMap(Run run){

		// Null check done inside function
		safeAddFloorToBestFloorMap(run.first);
		safeAddFloorToBestFloorMap(run.second);
		safeAddFloorToBestFloorMap(run.third);
		safeAddFloorToBestFloorMap(run.fourth);
		safeAddFloorToBestFloorMap(run.fifth);
		
	}

	private void safeAddFloorToBestFloorMap(Floor newFloor){

		if (newFloor == null) return;

		if (bestFloorMap == null) return;

		Map<Variation, Floor> variationsMap = bestFloorMap.get(newFloor.floor);
		// If floor hasn't been seen yet, make a map for it
		if (variationsMap == null){
			variationsMap = new HashMap<Variation, Floor>();
			bestFloorMap.put(newFloor.floor, variationsMap);
		}
		
		Floor current = variationsMap.get(newFloor.variation);
		// If variation hasn't been seen yet, this new one is best
		if (current == null) {
			variationsMap.put(newFloor.variation, newFloor);
			log.debug("First time seeing variation for: " + newFloor.DisplayName() + " | Ticks: " + newFloor.GetTicks(0));
			return;
		}

		// Now we have the current best and the newest run
		// Compare overall time ticks and overwrite if new is better

		for (int i = 0; i <= 3; i++) {

			if (current.GetTicks(i) > newFloor.GetTicks(i)){
			current.SetLootTicks(newFloor.GetTicks(i), i);
			log.debug("New best for floor: " + newFloor.DisplayName() + " | Loot: " + i + " | Ticks: " + newFloor.GetTicks(0));
			}

		}

		// Save new floor values
		variationsMap.put(newFloor.variation, current);
		
	}

	public Floor safeGetBestFloor(int floor, Variation variation){

		if (bestFloorMap == null) return null;

		Map<Variation, Floor> variations = bestFloorMap.get(floor);

		if (variations == null) return null;

		return variations.get(variation);

	}

	@Provides
	HallowedSepulchreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HallowedSepulchreConfig.class);
	}

}