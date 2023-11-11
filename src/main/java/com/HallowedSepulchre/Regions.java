package com.HallowedSepulchre;

public final class Regions {

    public static int WORLD_FLOOR = -1;
    public static int LOBBY_FLOOR = 0;
    public static int FIRST_FLOOR = 1;
    public static int SECOND_FLOOR = 2;
    public static int THIRD_FLOOR = 3;

    public static int LOBBY = 9565;

    // First floor loads into transition region, then moves to different regions based on variation
    public static int FIRST_TRANSITION = 9053;
    public static int FIRST_START_PLANE = 2;
    public static int FIRST_START_N = 9054;
    public static int FIRST_START_E = 9309;
    public static int FIRST_START_S = 9052;
    public static int FIRST_START_W = 8797;
    public static int FIRST_END_PLANE = 1;
    // First floor ends in transition region
    public static Coord FIRST_END_E = new Coord(2279, 5984);    
    public static Coord FIRST_END_W = new Coord(2265, 5984);


    public static int SECOND_START = 10077;
    public static int SECOND_START_PLANE = 2;
    public static Coord SECOND_START_N = new Coord(2528, 5988);
    public static Coord SECOND_START_E = new Coord(2532, 5984);
    public static Coord SECOND_START_S = new Coord(2528, 5980);
    public static Coord SECOND_START_W = new Coord(2524, 5984);

    public static int SECOND_END_PLANE = 1;
    public static Coord SECOND_FINISH_N = new Coord(2528, 5991);
    public static Coord SECOND_FINISH_E = new Coord(2535, 5984); 
    public static Coord SECOND_FINISH_S = new Coord(2528, 5977);
    public static Coord SECOND_FINISH_W = new Coord(2521, 5984);

    public static int SECOND_A = 9821;
    public static int SECOND_B = 10078;

    public static int THIRD_START = 9563;
    public static int THIRD_STATE_PLANE = 2;
    public static Coord THIRD_START_E = new Coord(2404, 5856);
    public static Coord THIRD_START_W = new Coord(2396, 5856);

    public static int THIRD_FINISH_PLANE = 1;
    public static Coord THIRD_FINISH_E = new Coord(2405, 5856);
    public static Coord THIRD_FINISH_W = new Coord(2395, 5856);

}




