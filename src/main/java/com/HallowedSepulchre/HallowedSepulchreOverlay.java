package com.HallowedSepulchre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;

import com.HallowedSepulchre.configs.LobbyDisplay;
import com.HallowedSepulchre.configs.TimeDisplay;
import com.HallowedSepulchre.helpers.TimeHelper;
import com.HallowedSepulchre.helpers.VarHelper;
import com.HallowedSepulchre.runs.Run;
import com.HallowedSepulchre.runs.Floor;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class HallowedSepulchreOverlay extends OverlayPanel
{
	private final HallowedSepulchrePlugin plugin;
	private final HallowedSepulchreConfig config;

	private final Color PAUSED_COLOR = new Color(105, 163, 251, 255);

	@Inject
	private HallowedSepulchreOverlay(HallowedSepulchrePlugin plugin, HallowedSepulchreConfig config)
	{
		super(plugin);
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.BOTTOM_LEFT);
		addMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Hallowed Sepulchre overlay");
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		// Player not in sepulchre, show nothing
		if (!plugin.isInSepulchre())
		{
			return null;
		}

		// Player is in lobby
		if (plugin.isInLobby()){
			showInLobby();
		}
		// Player is in a floor
		else {
			showInFloor();			
		}

		return super.render(graphics);
	}

	private void showInLobby(){
		
		switch(config.lobbyDisplayType()) {
			case RUN_INFO:
				displayRunInfo();
				return;
			case FLOOR_INFO:
				displayFloorInfo();
				return;
			case HIDE:
			return;
		}

	}

	private void showInFloor(){

		if (plugin.timer == null) return;

		setTitle(plugin.getStateDescriptor(), Color.WHITE);

		if (config.showFloorTime()){
			setLine("Floor:", getFloorTime(), plugin.isPaused() ? PAUSED_COLOR : Color.WHITE);
		}

		if (config.showBestTime()){
			setLine("Best:", displayTime(plugin.timer.bestFloorTicks), Color.LIGHT_GRAY);
		}
		
		if (config.showGoalTime()){
			setLine("Goal:", displayTime(plugin.timer.goalFloorTicks), Color.LIGHT_GRAY);
		}
	
		if (config.showTotalTime()){
			setLine("Total:", getCumulativeTime(), plugin.isPaused() ? PAUSED_COLOR : Color.WHITE);
		}

		boolean showProjected = config.showProjectedTime();
		boolean showRemaining = config.showRemainingTime();

		if ((showProjected || showRemaining) && config.timeDisplay() == TimeDisplay.TIME){
			// if either projected or remaining are enabled, and we are displaying seconds, need to process first
			plugin.timer.processProjectedAndRemainingSeconds();
		}
		
		if (showProjected){
			setLine("Projected:", getProjectedTime(), Color.LIGHT_GRAY);
		}

		if (showRemaining){
			setLine("Remaining:", getRemainingTime(), Color.LIGHT_GRAY);
		}			
		

		if (config.showPlayerCoords()){
			setTitle(plugin.DebugCoords(), Color.WHITE);
		}

	}

	private void displayRunInfo(){

		if (plugin.timer == null) return;

		runDisplay(plugin.timer.lastRun, "Last Run", config.lastRunDisplay());
		
		runDisplay(plugin.timer.bestRun, "Best Run", config.bestOverallRunDisplay());
		
		runDisplay(plugin.timer.optRun, "Optimal Goal Run", config.optimalRunDisplay());
		
	}

	private void displayFloorInfo(){

		int floors = plugin.getHighestFloor();

		if (floors == 0){
			displayNoInfo();
			return;
		}

		if (config.showFirstFloorInfo() && floors >= 1){
			setTitle("Floor 1", Color.WHITE);
			displayVariation(1, Variation.North);
			displayVariation(1, Variation.East);
			displayVariation(1, Variation.South);
			displayVariation(1, Variation.West);
		}

		if (config.showSecondFloorInfo() && floors >= 2) {
			setTitle("Floor 2", Color.WHITE);
			displayVariation(2, Variation.North);
			displayVariation(2, Variation.East);
			displayVariation(2, Variation.South);
			displayVariation(2, Variation.West);
		}

		if (config.showThirdFloorInfo() && floors >= 3) {
			setTitle("Floor 3", Color.WHITE);
			displayVariation(3, Variation.East);
			displayVariation(3, Variation.West);
		}

		if (config.showFourthFloorInfo() && floors >= 4) {
			setTitle("Floor 4", Color.WHITE);
			displayVariation(4, Variation.North);
			displayVariation(4, Variation.South);
		}

		if (config.showFifthFloorInfo() && floors >= 5) {
			setTitle("Floor 5", Color.WHITE);
			displayVariation(5, Variation.North);
		}
		
	}

	private void displayVariation(int floor, Variation variation) {

		Floor floorObj = plugin.safeGetBestFloor(floor, variation);
		// Shouldn't happen due to checks in calling function
		if (floorObj == null) return;
		
		setLine(VarHelper.VarToString(variation), displayTime(floorObj.GetTicks(0)), Color.WHITE);

		int possibleLoots = Floor.possibleLoots(floor);

		String coffin = "Coffin";
		String coffins = "Coffins";

		// 1-indexed
		for (int i = 1; i <= possibleLoots; i++){
			int ticks = floorObj.GetTicks(i);
			// Loot variation never completed
			if (ticks < 0){
				continue;
			}
			setLine(i + " " + (i == 1 ? coffin : coffins ), displayTime(ticks), Color.LIGHT_GRAY);
		}

	}

	private void displayBasicRun(Run run){

		if (run == null) return;
		if (run.totalFloors < 1) return;

		setLine(getFloorString(run.totalFloors), displayTime(run.totalTicks), Color.GREEN);

	}

	private void displayDetailedRun(Run run){

		if (run == null) return;
		if (run.totalFloors < 1) return;

		if (run.first != null) {
			setLine(run.first.DisplayName(), displayTime(run.first.GetTicks(0)), Color.WHITE);
		}
		if (run.second != null) {
			setLine(run.second.DisplayName(), displayTime(run.second.GetTicks(0)), Color.WHITE);
		}
		if (run.third != null) {
			setLine(run.third.DisplayName(), displayTime(run.third.GetTicks(0)), Color.WHITE);
		}
		if (run.fourth != null) {
			setLine(run.fourth.DisplayName(), displayTime(run.fourth.GetTicks(0)), Color.WHITE);
		}
		if (run.fifth != null) {
			setLine(run.fifth.DisplayName(), displayTime(run.fifth.GetTicks(0)), Color.WHITE);
		}

		setLine("Total:", displayTime(run.totalTicks), Color.GREEN);

	}

	private void displayNoInfo(Run run){
		if (run == null || run.totalFloors < 1){
			displayNoInfo();
		}
	}

	private void displayNoInfo(){
		setLine("No info", "", Color.WHITE);
	}

	private void runDisplay(Run run, String label, LobbyDisplay runDisplay) {

		if (runDisplay == LobbyDisplay.HIDE){
			return;
		}

		setTitle(label, Color.WHITE);

		displayNoInfo(run);

		if (runDisplay == LobbyDisplay.BASIC){
			displayBasicRun(run);
		} else if (runDisplay == LobbyDisplay.DETAILED){
			displayDetailedRun(run);
		}

	}


	/*
		Helper methods
	*/

	private String getFloorTime(){

		if (plugin.timer == null){
			return "err";
		}
		if (config.timeDisplay() == TimeDisplay.TICKS){
			return plugin.timer.GetTicks() + "t";
		}
		return TimeHelper.GetTimeFromSysTime(plugin.timer.GetSystemTimeStart(), plugin.timer.GetSystemTimeEnd(), 0);
	}

	public String getCumulativeTime(){
		if (plugin.timer == null){
			return "err";
		}
		if (config.timeDisplay() == TimeDisplay.TICKS){
			return (plugin.timer.GetTicks() + plugin.timer.GetCumulativeTicks()) + "t";
		}
		return TimeHelper.GetTimeFromSysTime(plugin.timer.GetSystemTimeStart(), plugin.timer.GetSystemTimeEnd(), plugin.timer.GetCumulativeSystemTime());
	}

	public String getProjectedTime(){
		if (plugin.timer == null){
			return "err";
		}
		int ticks = plugin.timer.getProjectedTicks();
		TimeDisplay display = config.timeDisplay();
		if (ticks < 0){
			return TimeHelper.nilTime(display);
		}
		if (display == TimeDisplay.TICKS){
			return ticks + "t";
		}
		if (plugin.timer.GetTicks() <= plugin.timer.goalFloorTicks){
			return TimeHelper.GetTimeFromTicks(ticks);
		}

		return TimeHelper.GetTimeFromSysTime(plugin.timer.projectedFloorSeconds, 0);
	}

	public String getRemainingTime(){

		if (plugin.timer == null){
			return "err";
		}
		int ticks = plugin.timer.remainingFloorTicks;
		TimeDisplay display = config.timeDisplay();
		if (ticks < 0){
			return TimeHelper.nilTime(display);
		}
		
		int elapsedTicks = plugin.timer.GetTicks();

		// If the timer is under the optimal, then the calculated estimate is still accurate
		if (elapsedTicks <= plugin.timer.goalFloorTicks){

			if (display == TimeDisplay.TICKS){
				return ticks + "t";
			}

			return TimeHelper.GetTimeFromTicks(ticks);

		}

		if (display == TimeDisplay.TICKS){
				return (plugin.timer.totalFloorTicks - plugin.timer.getProjectedTicks()) + "t";
		}

		return TimeHelper.GetTimeFromSysTime(plugin.timer.remainingFloorSeconds, 0);
	}

	private String getFloorString(int floor){

		String floorStr = floor + " Floor";

		if (floor > 1) {
			floorStr += "s";
		}

		return floorStr;

	}

	private String displayTime(int ticks){

		if (ticks < 0){
			return TimeHelper.nilTime(config.timeDisplay());
		}

		if (config.timeDisplay() == TimeDisplay.TICKS){
			return ticks + "t";
		}

		return TimeHelper.GetTimeFromTicks(ticks);

	}

	/*
		Draw text
	*/

	private void setTitle(String text, Color color){
		panelComponent.getChildren().add(TitleComponent.builder()
			.text(text)
			.color(color)
			.build());
	}

	private void setLine(String leftText, String rightText, Color color){
		panelComponent.getChildren().add(LineComponent.builder()
			.left(leftText)
			.leftColor(color)
			.right(rightText)
			.rightColor(color)
			.build());	

	}

}
