package com.porvak.bracket.repository.impl;

import static com.google.common.base.Preconditions.*;

public class TournamentMetaData {
    
    public static String getRegionName(String regionId){
        regionId = checkNotNull(regionId);
        String regionName = "";
        if(regionId.equals("1")){
            regionName = "East";
        }
        else if(regionId.equals("2")){
            regionName = "West";
        }
        else if(regionId.equals("3")){
            regionName = "Midwest";
        }
        else if(regionId.equals("4")){
            regionName = "South";
        }
        else if (regionId.equals("5")){
            regionName = "Finals";
        }

        return regionName;
    }
}
