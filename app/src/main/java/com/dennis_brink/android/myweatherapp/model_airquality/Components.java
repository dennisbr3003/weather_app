
package com.dennis_brink.android.myweatherapp.model_airquality;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Components {

    @SerializedName("co")
    @Expose
    private Double co;
    @SerializedName("no")
    @Expose
    private Double no;
    @SerializedName("no2")
    @Expose
    private Double no2;
    @SerializedName("o3")
    @Expose
    private Double o3;
    @SerializedName("so2")
    @Expose
    private Double so2;
    @SerializedName("pm2_5")
    @Expose
    private Double pm25;
    @SerializedName("pm10")
    @Expose
    private Double pm10;
    @SerializedName("nh3")
    @Expose
    private Double nh3;

    public Double getCo() {
        return co;
    }

    public void setCo(Double co) {
        this.co = co;
    }

    public Double getNo() {
        return no;
    }

    public void setNo(Double no) {
        this.no = no;
    }

    public Double getNo2() {
        return no2;
    }

    public void setNo2(Double no2) {
        this.no2 = no2;
    }

    public Double getO3() {
        return o3;
    }

    public void setO3(Double o3) {
        this.o3 = o3;
    }

    public Double getSo2() {
        return so2;
    }

    public void setSo2(Double so2) {
        this.so2 = so2;
    }

    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public Double getNh3() {
        return nh3;
    }

    public void setNh3(Double nh3) {
        this.nh3 = nh3;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Components.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("co");
        sb.append('=');
        sb.append(((this.co == null)?"<null>":this.co));
        sb.append(',');
        sb.append("no");
        sb.append('=');
        sb.append(((this.no == null)?"<null>":this.no));
        sb.append(',');
        sb.append("no2");
        sb.append('=');
        sb.append(((this.no2 == null)?"<null>":this.no2));
        sb.append(',');
        sb.append("o3");
        sb.append('=');
        sb.append(((this.o3 == null)?"<null>":this.o3));
        sb.append(',');
        sb.append("so2");
        sb.append('=');
        sb.append(((this.so2 == null)?"<null>":this.so2));
        sb.append(',');
        sb.append("pm25");
        sb.append('=');
        sb.append(((this.pm25 == null)?"<null>":this.pm25));
        sb.append(',');
        sb.append("pm10");
        sb.append('=');
        sb.append(((this.pm10 == null)?"<null>":this.pm10));
        sb.append(',');
        sb.append("nh3");
        sb.append('=');
        sb.append(((this.nh3 == null)?"<null>":this.nh3));
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
        result = ((result* 31)+((this.no2 == null)? 0 :this.no2 .hashCode()));
        result = ((result* 31)+((this.no == null)? 0 :this.no.hashCode()));
        result = ((result* 31)+((this.o3 == null)? 0 :this.o3 .hashCode()));
        result = ((result* 31)+((this.pm25 == null)? 0 :this.pm25 .hashCode()));
        result = ((result* 31)+((this.so2 == null)? 0 :this.so2 .hashCode()));
        result = ((result* 31)+((this.pm10 == null)? 0 :this.pm10 .hashCode()));
        result = ((result* 31)+((this.nh3 == null)? 0 :this.nh3 .hashCode()));
        result = ((result* 31)+((this.co == null)? 0 :this.co.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Components) == false) {
            return false;
        }
        Components rhs = ((Components) other);
        return (((((((((this.no2 == rhs.no2)||((this.no2 != null)&&this.no2 .equals(rhs.no2)))&&((this.no == rhs.no)||((this.no!= null)&&this.no.equals(rhs.no))))&&((this.o3 == rhs.o3)||((this.o3 != null)&&this.o3 .equals(rhs.o3))))&&((this.pm25 == rhs.pm25)||((this.pm25 != null)&&this.pm25 .equals(rhs.pm25))))&&((this.so2 == rhs.so2)||((this.so2 != null)&&this.so2 .equals(rhs.so2))))&&((this.pm10 == rhs.pm10)||((this.pm10 != null)&&this.pm10 .equals(rhs.pm10))))&&((this.nh3 == rhs.nh3)||((this.nh3 != null)&&this.nh3 .equals(rhs.nh3))))&&((this.co == rhs.co)||((this.co!= null)&&this.co.equals(rhs.co))));
    }

}
