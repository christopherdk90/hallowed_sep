package com.HallowedSepulchre;

import com.HallowedSepulchre.configs.TimeDisplay;
import com.HallowedSepulchre.configs.LobbyDisplay;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("HallowedSepulchre")
public interface HallowedSepulchreConfig extends Config
{

	@ConfigItem(
		name = "Time Display",
		keyName = "time-display",
		description = "Choose to display either game ticks or time.",
		position = 0
	)
	default TimeDisplay timeDisplay()
	{
		return TimeDisplay.TIME;
	}


	@ConfigSection(
		name = "In Lobby",
		description = "Configure info to display while in the lobby.",
		position = 1
	) String LOBBY = "Lobby";

	@ConfigItem(
		name = "Last Run",
		keyName = "last-run",
		description = "Choose to display information about your last run.",
		section = LOBBY,
		position = 0
	)
	default LobbyDisplay lastRunDisplay()
	{
		return LobbyDisplay.DETAILED;
	}

	@ConfigItem(
		name = "Best Run",
		keyName = "best-run",
		description = "Choose to display information about your best run.",
		section = LOBBY,
		position = 1
	)
	default LobbyDisplay bestRunDisplay()
	{
		return LobbyDisplay.DETAILED;
	}

	@ConfigItem(
		name = "Optimal Run",
		keyName = "optimal-run",
		description = "Choose to display information about your optimal run.",
		section = LOBBY,
		position = 2
	)
	default LobbyDisplay optimalRunDisplay()
	{
		return LobbyDisplay.DETAILED;
	}

	@ConfigSection(
		name = "In Floors",
		description = "Configure info to display while in the floor.",
		position = 2
	) String FLOOR = "Floor";

		@ConfigItem(
		name = "Show floor time",
		keyName = "show-floor-time",
		description = "Show the elapsed time for the floor.",
		section = FLOOR,
		position = 0
	)
	default boolean showFloorTime()
	{
		return true;
	}

	@ConfigItem(
		name = "Show best time",
		keyName = "show-best-time",
		description = "Show the best time for the current variation of the floor.",
		section = FLOOR,
		position = 1
	)
	default boolean showBestTime()
	{
		return true;
	}



	@ConfigItem(
		name = "Show total time",
		keyName = "show-total-time",
		description = "Show the total elapsed time for the run.",
		section = FLOOR,
		position = 2
	)
	default boolean showTotalTime()
	{
		return true;
	}

	@ConfigSection(
		name = "Debug",
		description = "Configure debug options.",
		position = 3
	) String DEBUG = "Debug";

	@ConfigItem(
		name = "Log RegionID every Game Tick",
		keyName = "log-region-id",
		description = "Log region ID on every game tick, useful for figuring out the loading region",
		section = DEBUG,
		position = 0
	)
	default boolean logRegionID()
	{
		return false;
	}

		@ConfigItem(
		name = "Show player coords",
		keyName = "show-player-coords",
		description = "Shoow player coordinates while in a run",
		section = DEBUG,
		position = 1
	)
	default boolean showPlayerCoords()
	{
		return true;
	}

}
