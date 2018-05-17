package se.vettefors.knowmyroutes.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapClient {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static MapClient mapClientInstance;
    private MapsApiService mapsApiService;

    private MapClient() {
        init();
    }

    public static MapClient getInstance() {
        if (mapClientInstance == null) {
            mapClientInstance = new MapClient();
        }
        return mapClientInstance;
    }


    private void init() {
        mapsApiService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MapsApiService.class);
    }

    public MapsApiService getMapsApiService(){
        return mapsApiService;
    }
}
