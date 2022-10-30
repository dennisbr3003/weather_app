
package com.dennis_brink.android.myweatherapp.model_forecast;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Coord {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Coord.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lat");
        sb.append('=');
        sb.append(this.lat);
        sb.append(',');
        sb.append("lon");
        sb.append('=');
        sb.append(this.lon);
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
        result = ((result* 31)+((int)(Double.doubleToLongBits(this.lat)^(Double.doubleToLongBits(this.lat)>>> 32))));
        result = ((result* 31)+((int)(Double.doubleToLongBits(this.lon)^(Double.doubleToLongBits(this.lon)>>> 32))));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Coord) == false) {
            return false;
        }
        Coord rhs = ((Coord) other);
        return ((Double.doubleToLongBits(this.lat) == Double.doubleToLongBits(rhs.lat))&&(Double.doubleToLongBits(this.lon) == Double.doubleToLongBits(rhs.lon)));
    }

}
