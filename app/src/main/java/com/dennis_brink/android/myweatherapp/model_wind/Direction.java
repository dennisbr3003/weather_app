
package com.dennis_brink.android.myweatherapp.model_wind;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "direction",
    "degrees"
})
@Generated("jsonschema2pojo")
public class Direction {

    @JsonProperty("direction")
    private String direction;
    @JsonProperty("degrees")
    private Double degrees;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Direction() {
    }

    /**
     * 
     * @param degrees
     * @param direction
     */
    public Direction(String direction, Double degrees) {
        super();
        this.direction = direction;
        this.degrees = degrees;
    }

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonProperty("degrees")
    public Double getDegrees() {
        return degrees;
    }

    @JsonProperty("degrees")
    public void setDegrees(Double degrees) {
        this.degrees = degrees;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Direction.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("direction");
        sb.append('=');
        sb.append(((this.direction == null)?"<null>":this.direction));
        sb.append(',');
        sb.append("degrees");
        sb.append('=');
        sb.append(((this.degrees == null)?"<null>":this.degrees));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
