package com.HallowedSepulchre;

import com.HallowedSepulchre.configs.TimeDisplay;
import com.HallowedSepulchre.configs.TwoCoffinDisplay;
import com.HallowedSepulchre.configs.LobbyDisplay;
import com.HallowedSepulchre.configs.LobbyDisplayType;
import com.HallowedSepulchre.configs.OneCoffinDisplay;
import com.HallowedSepulchre.configs.ThreeCoffinDisplay;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("HallowedSepulchre")
public interface HallowedSepulchreConfig extends Config
{

	int runGoalsPosition = 1;
	int inLobbyPosition = 2;
	int runInfoPosition = 3;
	int floorInfoPosition = 4;
	int inFloorsPosition = 5;
	int debugPosition = 6;

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

	@ConfigItem(
		name = "Time Reset",
		keyName = "time-reset",
		description = "Choose to display either game ticks or time.",
		position = 0
	)
	default boolean timeReset()
	{
		return false;
	}

	@ConfigSection(
		name = "Run Goals",
		description = "Set your run goals in order to determine which values will be shown during a run. See the readme for a full explanation.",
		position = runGoalsPosition
	) String GOALS = "Run Goals";

	@ConfigItem(
		name = "First Floor",
		keyName = "first-floor-goal",
		description = "Set your goal for the first floor.",
		section = GOALS,
		position = 0
	)
	default OneCoffinDisplay firstFloorGoal()
	{
		return OneCoffinDisplay.NO_LOOTING;
	}

	@ConfigItem(
		name = "Second Floor",
		keyName = "second-floor-goal",
		description = "Set your goal for the second floor.",
		section = GOALS,
		position = 1
	)
	default OneCoffinDisplay secondFloorGoal()
	{
		return OneCoffinDisplay.NO_LOOTING;
	}

	@ConfigItem(
		name = "Third Floor",
		keyName = "third-floor-goal",
		description = "Set your goal for the third floor.",
		section = GOALS,
		position = 2
	)
	default TwoCoffinDisplay thirdFloorGoal()
	{
		return TwoCoffinDisplay.TWO_COFFINS;
	}

	@ConfigItem(
		name = "Fourth Floor",
		keyName = "fourth-floor-goal",
		description = "Set your goal for the fourth floor.",
		section = GOALS,
		position = 3
	)
	default TwoCoffinDisplay fourthFloorGoal()
	{
		return TwoCoffinDisplay.TWO_COFFINS;
	}

	@ConfigItem(
		name = "Fifth Floor",
		keyName = "fifth-floor-goal",
		description = "Set your goal for the fifth floor.",
		section = GOALS,
		position = 4
	)
	default ThreeCoffinDisplay fifthFloorGoal()
	{
		return ThreeCoffinDisplay.THREE_COFFINS;
	}


	@ConfigSection(
		name = "Lobby Display",
		description = "Configure info to display while in the lobby.",
		position = inLobbyPosition
	) String LOBBY = "Lobby";

	@ConfigItem(
		name = "Display Type",
		keyName = "lobby-display-type",
		description = "Choose what set of information to display.",
		section = LOBBY,
		position = 0
	)
	default LobbyDisplayType lobbyDisplayType()
	{
		return LobbyDisplayType.RUN_INFO;
	}

	@ConfigSection(
		name = "Lobby - Run Info Options",
		description = "Configure what is shown for while opting to display Run Info while in lobby.",
		position = 3
	) String RUN_INFO = "Run Info";

	@ConfigItem(
		name = "Last Run",
		keyName = "last-run",
		description = "Choose to display information about your last run.",
		section = RUN_INFO,
		position = 1
	)
	default LobbyDisplay lastRunDisplay()
	{
		return LobbyDisplay.DETAILED;
	}

	@ConfigItem(
		name = "Best Overall Run",
		keyName = "best-run",
		description = "Choose to display information about your quickest overall run.",
		section = RUN_INFO,
		position = 2
	)
	default LobbyDisplay bestOverallRunDisplay()
	{
		return LobbyDisplay.DETAILED;
	}

	@ConfigItem(
		name = "Optimal Goal Run",
		keyName = "optimal-run",
		description = "Choose to display information about your optimal run based on your current goals.",
		section = RUN_INFO,
		position = 3
	)
	default LobbyDisplay optimalRunDisplay()
	{
		return LobbyDisplay.DETAILED;
	}

	@ConfigSection(
		name = "Lobby - Floor Info Options",
		description = "Configure what is shown for while opting to display Floor Info while in lobby.",
		position = floorInfoPosition
	) String FLOOR_INFO = "Floor Info";

	@ConfigItem(
		name = "Show First",
		keyName = "show-first",
		description = "Choose to display the first floor while showing Floor Info.",
		section = FLOOR_INFO,
		position = 0
	)
	default boolean showFirstFloorInfo()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Second",
		keyName = "show-second",
		description = "Choose to display the second floor while showing Floor Info.",
		section = FLOOR_INFO,
		position = 1
	)
	default boolean showSecondFloorInfo()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Third",
		keyName = "show-third",
		description = "Choose to display the third floor while showing Floor Info.",
		section = FLOOR_INFO,
		position = 2
	)
	default boolean showThirdFloorInfo()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Fourth",
		keyName = "show-fourth",
		description = "Choose to display the fourth floor while showing Floor Info.",
		section = FLOOR_INFO,
		position = 3
	)
	default boolean showFourthFloorInfo()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Fifth",
		keyName = "show-fifth",
		description = "Choose to display the fifth floor while showing Floor Info.",
		section = FLOOR_INFO,
		position = 4
	)
	default boolean showFifthFloorInfo()
	{
		return true;
	}

	@ConfigSection(
		name = "Floors Display",
		description = "Configure info to display while in the floor.",
		position = inFloorsPosition
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
		name = "Show goal time",
		keyName = "show-goal-time",
		description = "Show the goal time for the current variation of the floor.",
		section = FLOOR,
		position = 2
	)
	default boolean showGoalTime()
	{
		return true;
	}

	@ConfigItem(
		name = "Show total time",
		keyName = "show-total-time",
		description = "Show the total elapsed time for the run.",
		section = FLOOR,
		position = 3
	)
	default boolean showTotalTime()
	{
		return true;
	}

	@ConfigItem(
		name = "Show projected time",
		keyName = "show-projected-time",
		description = "Show the projected total time for the run.",
		section = FLOOR,
		position = 4
	)
	default boolean showProjectedTime()
	{
		return true;
	}

	@Range(max = 100, min = 0)
	@ConfigItem(
		name = "Tick buffer",
		keyName = "projected-buffer",
		description = "Ticks to add to projected time for each remaining floor. Each tick is 0.6 seconds.",
		section = FLOOR,
		position = 5
	)
	default int projectedBuffer()
	{
		return 0;
	}
		
	@ConfigItem(
		name = "Show remaining time",
		keyName = "show-remaining-time",
		description = "Show the estimated remaining time for the run.",
		section = FLOOR,
		position = 6
	)
	default boolean showRemainingTime()
	{
		return true;
	}

	@ConfigSection(
		name = "Debug",
		description = "Configure debug options.",
		position = debugPosition
	) String DEBUG = "Debug";

		@ConfigItem(
		name = "Show player coords",
		keyName = "show-player-coords",
		description = "Shoow player coordinates while in a run",
		section = DEBUG,
		position = 0
	)
	default boolean showPlayerCoords()
	{
		return true;
	}

}
