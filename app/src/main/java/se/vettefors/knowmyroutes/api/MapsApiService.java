package se.vettefors.knowmyroutes.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import se.vettefors.knowmyroutes.model.directionsApi.Directions;
import se.vettefors.knowmyroutes.model.geocodingApi.Geocode;

public interface MapsApiService {

    @GET("directions/json?key=AIzaSyB6YxIMTKfyeibeFr5fsnCquk2IBJ2NgB4")
    Call<Directions>
    getDirectionsBetween(@Query("origin") String origin, @Query("destination") String destination);

    @GET("geocode/json?key=AIzaSyB6YxIMTKfyeibeFr5fsnCquk2IBJ2NgB4")
    Call<Geocode>
    getLocationFromAddress(@Query("address") String address);

    @GET("geocode/json?key=AIzaSyB6YxIMTKfyeibeFr5fsnCquk2IBJ2NgB4")
    Call<Geocode>
    getAddressFromLocation(@Query("latlng") String location);
}
