
package com.dennis_brink.android.myweatherapp.model_forecast;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class City {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("population")
    @Expose
    private int population;
    @SerializedName("timezone")
    @Expose
    private int timezone;
    @SerializedName("sunrise")
    @Expose
    private int sunrise;
    @SerializedName("sunset")
    @Expose
    private int sunset;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(City.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(this.id);
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("coord");
        sb.append('=');
        sb.append(((this.coord == null)?"<null>":this.coord));
        sb.append(',');
        sb.append("country");
        sb.append('=');
        sb.append(((this.country == null)?"<null>":this.country));
        sb.append(',');
        sb.append("population");
        sb.append('=');
        sb.append(this.population);
        sb.append(',');
        sb.append("timezone");
        sb.append('=');
        sb.append(this.timezone);
        sb.append(',');
        sb.append("sunrise");
        sb.append('=');
        sb.append(this.sunrise);
        sb.append(',');
        sb.append("sunset");
        sb.append('=');
        sb.append(this.sunset);
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.country == null)? 0 :this.country.hashCode()));
        result = ((result* 31)+((this.coord == null)? 0 :this.coord.hashCode()));
        result = ((result* 31)+ this.sunrise);
        result = ((result* 31)+ this.timezone);
        result = ((result* 31)+ this.sunset);
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+ this.id);
        result = ((result* 31)+ this.population);
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof City) == false) {
            return false;
        }
        City rhs = ((City) other);
        return (((((((((this.country == rhs.country)||((this.country!= null)&&this.country.equals(rhs.country)))&&((this.coord == rhs.coord)||((this.coord!= null)&&this.coord.equals(rhs.coord))))&&(this.sunrise == rhs.sunrise))&&(this.timezone == rhs.timezone))&&(this.sunset == rhs.sunset))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&(this.id == rhs.id))&&(this.population == rhs.population));
    }

}
