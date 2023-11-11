package com.HallowedSepulchre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;

import com.HallowedSepulchre.configs.LobbyDisplay;
import com.HallowedSepulchre.runs.Run;

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
		if (!plugin.IsInSepulchre())
		{
			return null;
		}

		// Player is in lobby
		if (plugin.IsInLobby()){
			showInLobby();
		}
		// Player is in a floor
		else {
			panelComponent.getChildren().add(TitleComponent.builder()
				.text(plugin.GetStateDescriptor())
				.color(Color.WHITE)
				.build());

			if (config.showFloorTime()){
				panelComponent.getChildren().add(LineComponent.builder()
					.left("Floor:")
					.leftColor(Color.WHITE)
					.right(plugin.GetFloorTime())
					.rightColor(plugin.IsPaused() ? PAUSED_COLOR : Color.WHITE)
					.build());
			}
			
			if (config.showBestTime()){
				panelComponent.getChildren().add(LineComponent.builder()
					.left("Best:")
					.leftColor(Color.LIGHT_GRAY)
					.right(plugin.getBestFloorTime())
					.rightColor(Color.LIGHT_GRAY)
					.build());
			}	
			
			if (config.showTotalTime()){
				panelComponent.getChildren().add(LineComponent.builder()
					.left("Total:")
					.leftColor(Color.WHITE)
					.right(plugin.GetCumulativeTime())
					.rightColor(plugin.IsPaused() ? PAUSED_COLOR : Color.WHITE)
					.build());
			}

			if (config.showPlayerCoords()){
				panelComponent.getChildren().add(TitleComponent.builder()
					.text(plugin.DebugCoords())
					.color(Color.WHITE)
					.build());
			}
			
		}

		return super.render(graphics);
	}

	private void showInLobby(){
		
		
		if (config.lastRunDisplay() == LobbyDisplay.HIDE){
			// Do not display anything for last run
		}
		else {
			runDisplay(plugin.lastRun, "Last Run", config.lastRunDisplay());
		}


		if (config.bestRunDisplay() == LobbyDisplay.HIDE){
			// Do not display anything for best run
		}
		else {
			runDisplay(plugin.bestRun, "Best Run", config.bestRunDisplay());
		}

		if (config.optimalRunDisplay() == LobbyDisplay.HIDE){
			// Do not display anything for optimal run
		}
		else {
			runDisplay(plugin.optRun, "Optimal Run", config.optimalRunDisplay());
		}
		
	}

	private String getFloorString(int floor){

		String floorStr = floor + " Floor";

		if (floor > 1) {
			floorStr += "s";
		}

		return floorStr;

	}

	private void displayBasicRun(Run run){

		if (run == null) return;
		if (run.totalFloors < 1) return;

		panelComponent.getChildren().add(LineComponent.builder()
			.left(getFloorString(run.totalFloors))
			.leftColor(Color.GREEN)
			.right(run.DisplayTime())
			.rightColor(Color.GREEN)
			.build());

	}

	private void displayDetailedRun(Run run){

		if (run == null) return;
		if (run.totalFloors < 1){
			return;
		}

		if (run.first != null) {
				panelComponent.getChildren().add(LineComponent.builder()
					.left(run.first.DisplayName())
					.leftColor(Color.WHITE)
					.right(run.first.DisplayTime())
					.rightColor(Color.WHITE)
					.build());
			}
		if (run.second != null) {
			panelComponent.getChildren().add(LineComponent.builder()
				.left(run.second.DisplayName())
				.leftColor(Color.WHITE)
				.right(run.second.DisplayTime())
				.rightColor(Color.WHITE)
				.build());
		}
		if (run.third != null) {
			panelComponent.getChildren().add(LineComponent.builder()
				.left(run.third.DisplayName())
				.leftColor(Color.WHITE)
				.right(run.third.DisplayTime())
				.rightColor(Color.WHITE)
				.build());
		}
		if (run.fourth != null) {
			panelComponent.getChildren().add(LineComponent.builder()
				.left(run.fourth.DisplayName())
				.leftColor(Color.WHITE)
				.right(run.fourth.DisplayTime())
				.rightColor(Color.WHITE)
				.build());
		}
		if (run.fifth != null) {
			panelComponent.getChildren().add(LineComponent.builder()
				.left(run.fifth.DisplayName())
				.leftColor(Color.WHITE)
				.right(run.fifth.DisplayTime())
				.rightColor(Color.WHITE)
				.build());
		}

		
		panelComponent.getChildren().add(LineComponent.builder()
			.left("Total:")
			.leftColor(Color.GREEN)
			.right(run.DisplayTime())
			.rightColor(Color.GREEN)
			.build());	

	}

	private void displayNoInfo(Run run){

		if (run == null || run.totalFloors < 1){
			panelComponent.getChildren().add(LineComponent.builder()
				.left("No info")
				.leftColor(Color.WHITE)
				.build());	
		}

	}

	private void runDisplay(Run run, String label, LobbyDisplay runDisplay) {

		panelComponent.getChildren().add(TitleComponent.builder()
				.text(label)
				.color(Color.WHITE)
				.build());

			displayNoInfo(run);

			if (runDisplay == LobbyDisplay.BASIC){
				displayBasicRun(run);
			} else if (runDisplay == LobbyDisplay.DETAILED){
				displayDetailedRun(run);
			}

	}

}
