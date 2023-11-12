package com.HallowedSepulchre;

import com.HallowedSepulchre.states.State;
import com.HallowedSepulchre.states.WorldState;
import com.HallowedSepulchre.runs.Run;
import com.HallowedSepulchre.runs.Floor;
import com.HallowedSepulchre.configs.TimeDisplay;
import com.HallowedSepulchre.helpers.TimeHelper;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.print.attribute.standard.Fidelity;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.RSTimeUnit;
import net.runelite.api.MenuAction;
import net.runelite.api.coords.LocalPoint;

@Slf4j
@PluginDescriptor(
	name = "Hallowed Sepulchre"
)
public class HallowedSepulchrePlugin extends Plugin
{

	private State playerState;

	private Timer timer;

	public Run bestRun;
	public Run optRun;
	public Run lastRun;

	private Map<Integer, Map<Variations, Floor>> bestFloorMap = new HashMap<Integer, Map<Variations,Floor>>();

	@Inject
	private Client client;

	@Inject
	private HallowedSepulchreConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HallowedSepulchreOverlay overlay;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Hallowed Sepulchre plugin started!");
		overlayManager.add(overlay);

	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Hallowed Sepulchre plugin stopped!");
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
			playerState = new WorldState(timer);
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
