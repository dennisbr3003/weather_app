
package com.dennis_brink.android.myweatherapp.model_airquality;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class List {

    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("components")
    @Expose
    private Components components;
    @SerializedName("dt")
    @Expose
    private Integer dt;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(List.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("main");
        sb.append('=');
        sb.append(((this.main == null)?"<null>":this.main));
        sb.append(',');
        sb.append("components");
        sb.append('=');
        sb.append(((this.components == null)?"<null>":this.components));
        sb.append(',');
        sb.append("dt");
        sb.append('=');
        sb.append(((this.dt == null)?"<null>":this.dt));
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
        result = ((result* 31)+((this.dt == null)? 0 :this.dt.hashCode()));
        result = ((result* 31)+((this.main == null)? 0 :this.main.hashCode()));
        result = ((result* 31)+((this.components == null)? 0 :this.components.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof List) == false) {
            return false;
        }
        List rhs = ((List) other);
        return ((((this.dt == rhs.dt)||((this.dt!= null)&&this.dt.equals(rhs.dt)))&&((this.main == rhs.main)||((this.main!= null)&&this.main.equals(rhs.main))))&&((this.components == rhs.components)||((this.components!= null)&&this.components.equals(rhs.components))));
    }

}
