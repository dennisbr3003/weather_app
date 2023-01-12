
package com.dennis_brink.android.myweatherapp.model_wind;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "directions"
})
@Generated("jsonschema2pojo")
public class WindDirection {

    @JsonProperty("directions")
    private List<Direction> directions = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public WindDirection() {
    }

    /**
     * 
     * @param directions
     */
    public WindDirection(List<Direction> directions) {
        super();
        this.directions = directions;
    }

    @JsonProperty("directions")
    public List<Direction> getDirections() {
        return directions;
    }

    @JsonProperty("directions")
    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(WindDirection.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("directions");
        sb.append('=');
        sb.append(((this.directions == null)?"<null>":this.directions));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
