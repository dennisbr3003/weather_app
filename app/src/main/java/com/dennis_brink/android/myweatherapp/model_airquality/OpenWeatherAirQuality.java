
package com.dennis_brink.android.myweatherapp.model_airquality;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class OpenWeatherAirQuality {

    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("list")
    @Expose
    private java.util.List<List> list = null;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OpenWeatherAirQuality.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("coord");
        sb.append('=');
        sb.append(((this.coord == null)?"<null>":this.coord));
        sb.append(',');
        sb.append("list");
        sb.append('=');
        sb.append(((this.list == null)?"<null>":this.list));
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
        result = ((result* 31)+((this.list == null)? 0 :this.list.hashCode()));
        result = ((result* 31)+((this.coord == null)? 0 :this.coord.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OpenWeatherAirQuality) == false) {
            return false;
        }
        OpenWeatherAirQuality rhs = ((OpenWeatherAirQuality) other);
        return (((this.list == rhs.list)||((this.list!= null)&&this.list.equals(rhs.list)))&&((this.coord == rhs.coord)||((this.coord!= null)&&this.coord.equals(rhs.coord))));
    }

}
