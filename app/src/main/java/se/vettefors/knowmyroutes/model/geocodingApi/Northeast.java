
package se.vettefors.knowmyroutes.model.geocodingApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Northeast {

    @SerializedName("lat")
    @Expose
    private float lat;
    @SerializedName("lng")
    @Expose
    private float lng;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Northeast{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
