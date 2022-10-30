
package com.dennis_brink.android.myweatherapp.model_forecast;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Wind {

    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("deg")
    @Expose
    private int deg;
    @SerializedName("gust")
    @Expose
    private double gust;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public double getGust() {
        return gust;
    }

    public void setGust(double gust) {
        this.gust = gust;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Wind.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("speed");
        sb.append('=');
        sb.append(this.speed);
        sb.append(',');
        sb.append("deg");
        sb.append('=');
        sb.append(this.deg);
        sb.append(',');
        sb.append("gust");
        sb.append('=');
        sb.append(this.gust);
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
        result = ((result* 31)+((int)(Double.doubleToLongBits(this.speed)^(Double.doubleToLongBits(this.speed)>>> 32))));
        result = ((result* 31)+ this.deg);
        result = ((result* 31)+((int)(Double.doubleToLongBits(this.gust)^(Double.doubleToLongBits(this.gust)>>> 32))));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Wind) == false) {
            return false;
        }
        Wind rhs = ((Wind) other);
        return (((Double.doubleToLongBits(this.speed) == Double.doubleToLongBits(rhs.speed))&&(this.deg == rhs.deg))&&(Double.doubleToLongBits(this.gust) == Double.doubleToLongBits(rhs.gust)));
    }

}
