
package com.dennis_brink.android.myweatherapp.model_weather;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Clouds {

    @SerializedName("all")
    @Expose
    private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Clouds.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("all");
        sb.append('=');
        sb.append(((this.all == null)?"<null>":this.all));
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
        result = ((result* 31)+((this.all == null)? 0 :this.all.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Clouds) == false) {
            return false;
        }
        Clouds rhs = ((Clouds) other);
        return ((this.all == rhs.all)||((this.all!= null)&&this.all.equals(rhs.all)));
    }

}
