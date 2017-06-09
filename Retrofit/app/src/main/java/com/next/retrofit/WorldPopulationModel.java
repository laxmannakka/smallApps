package com.next.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by next on 25/5/17.
 */
public class WorldPopulationModel
{
    @SerializedName("worldpopulation")
    List<Country> worldpopulation;

    public List<Country> getWorldpopulation()
    {
        return worldpopulation;
    }

    public void setWorldpopulation(List<Country> worldpopulation)
    {
        this.worldpopulation = worldpopulation;
    }
}
