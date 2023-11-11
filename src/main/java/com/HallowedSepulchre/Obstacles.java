package com.HallowedSepulchre;

import java.util.Set;
import net.runelite.api.ObjectID;

import com.google.common.collect.ImmutableSet;

public class Obstacles {
    
    static final Set<Integer> SEPULCHRE_OBSTACLE_IDS = ImmutableSet.of(
        ObjectID.PLATFORM_38455, 
        ObjectID.PLATFORM_38457, 
        
        // First floor platforms
        ObjectID.PLATFORM_38456, 
        ObjectID.PLATFORM_38458,
        
        
        ObjectID.PLATFORM_38459,
		ObjectID.PLATFORM_38470, ObjectID.PLATFORM_38477
	);

}
