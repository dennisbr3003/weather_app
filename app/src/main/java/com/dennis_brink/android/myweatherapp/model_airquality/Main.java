
package com.dennis_brink.android.myweatherapp.model_airquality;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Main {

    @SerializedName("aqi")
    @Expose
    private Integer aqi;

    public Integer getAqi() {
        return aqi;
    }

    public void setAqi(Integer aqi) {
        this.aqi = aqi;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Main.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("aqi");
        sb.append('=');
        sb.append(((this.aqi == null)?"<null>":this.aqi));
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
        result = ((result* 31)+((this.aqi == null)? 0 :this.aqi.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Main) == false) {
            return false;
        }
        Main rhs = ((Main) other);
        return ((this.aqi == rhs.aqi)||((this.aqi!= null)&&this.aqi.equals(rhs.aqi)));
    }

}
