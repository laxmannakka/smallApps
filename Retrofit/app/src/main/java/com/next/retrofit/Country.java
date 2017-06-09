package com.next.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by next on 25/5/17.
 */
public class Country
{
    @SerializedName("rank")
    String rank;
    @SerializedName("population")
    String population;
    @SerializedName("flag")
    String flag;
    @SerializedName("country")
    String country;

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getRank()
    {
        return rank;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }


    public String getPopulation()
    {
        return population;
    }

    public void setPopulation(String population)
    {
        this.population = population;
    }

    public String getFlag()
    {
        return flag;
    }

    public void setFlag(String flag)
    {
        this.flag = flag;
    }
}
