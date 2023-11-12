package com.HallowedSepulchre;

import java.util.Arrays;
import java.util.HashSet;

public final class Regions {

    public static int WORLD_FLOOR = -1;
    public static int LOBBY_FLOOR = 0;
    public static int FIRST_FLOOR = 1;
    public static int SECOND_FLOOR = 2;
    public static int THIRD_FLOOR = 3;
    public static int FOURTH_FLOOR = 4;
    public static int FIFTH_FLOOR = 5;

    public static int LOBBY = 9565;

    // Floors 1-4 start and end on these planes
    public static int START_PLANE = 2;
    public static int FINISH_PLANE = 1;

    // First floor loads into transition region, then moves to different regions based on variation
    public static int FIRST_TRANSITION_REGION = 9053;

    public static int FIRST_REGION_START_N = 9054;
    public static int FIRST_REGION_START_E = 9309;
    public static int FIRST_REGION_START_S = 9052;
    public static int FIRST_REGION_START_W = 8797;

    public static HashSet<Integer> FIRST_REGIONS = new HashSet<Integer>(
        Arrays.asList(
            FIRST_TRANSITION_REGION,
            FIRST_REGION_START_N,
            FIRST_REGION_START_E,
            FIRST_REGION_START_S,
            FIRST_REGION_START_W
        )
    );
    
    // First floor ends in transition region
    public static Coord FIRST_END_E = new Coord(2279, 5984);    
    public static Coord FIRST_END_W = new Coord(2265, 5984);


    public static int SECOND_REGION_START = 10077;
    public static int SECOND_REGION_B = 9821;
    public static int SECOND_REGION_C = 10078;

    public static HashSet<Integer> SECOND_REGIONS = new HashSet<Integer>(
        Arrays.asList(
            SECOND_REGION_START,
            SECOND_REGION_B,
            SECOND_REGION_C
        )
    );

    public static Coord SECOND_START_N = new Coord(2528, 5988);
    public static Coord SECOND_START_E = new Coord(2532, 5984);
    public static Coord SECOND_START_S = new Coord(2528, 5980);
    public static Coord SECOND_START_W = new Coord(2524, 5984);

    public static Coord SECOND_FINISH_N = new Coord(2528, 5991);
    public static Coord SECOND_FINISH_E = new Coord(2535, 5984); 
    public static Coord SECOND_FINISH_S = new Coord(2528, 5977);
    public static Coord SECOND_FINISH_W = new Coord(2521, 5984);



    public static int THIRD_REGION_START = 9563;

    public static Coord THIRD_START_E = new Coord(2404, 5856);
    public static Coord THIRD_START_W = new Coord(2396, 5856);

    public static Coord THIRD_FINISH_E = new Coord(2405, 5856);
    public static Coord THIRD_FINISH_W = new Coord(2395, 5856);


    public static int FOURTH_REGION_START = 10075;
    public static int FOURTH_REGION_B = 10074;

    public static HashSet<Integer> FOURTH_REGIONS = new HashSet<Integer>(
        Arrays.asList(
            FOURTH_REGION_START,
            FOURTH_REGION_B
        )
    );

    public static Coord FOURTH_START_N = new Coord(2528, 5860);
    public static Coord FOURTH_START_S = new Coord(2528,5852);

    public static Coord FOURTH_FINISH_COORD = new Coord(2528, 5845);


    public static int FIFTH_REGION_START = 9051;
    public static int FIFTH_REGION_B = 9307;
    public static int FIFTH_REGION_C = 9052;

    public static HashSet<Integer> FIFTH_REGIONS = new HashSet<Integer>(
        Arrays.asList(
            FIFTH_REGION_START,
            FIFTH_REGION_B,
            FIFTH_REGION_C
        )
    );

    public static int FIFTH_START_PLANE = 2;
    public static int FIFTH_FINISH_PLANE = 0;

    public static Coord FIFTH_START_COORD = new Coord(2272, 5884);
    public static Coord FIFTH_FINISH_COORD = new Coord(2272, 5863);


}




