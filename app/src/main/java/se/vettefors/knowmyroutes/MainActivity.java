package se.vettefors.knowmyroutes;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.vettefors.knowmyroutes.api.MapClient;
import se.vettefors.knowmyroutes.model.directionsApi.Directions;
import se.vettefors.knowmyroutes.model.geocodingApi.Geocode;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private TextView mRouteInformation;
    List<LatLng> mPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRouteInformation = findViewById(R.id.route_information);
        mPoints = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        Geocoder geo = new Geocoder(this);

        try {
            List<Address> list = geo.getFromLocationName("Centralstationen", 1);

            String cleanAddress = list.get(0).getAddressLine(0).split(",")[0];
            System.out.println(cleanAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //getLocationFromAddress("Konsultgatan 7");
        //getLocationFromAddress("55.614256,12.989117");

    }

    private void showFragment(Fragment fragment) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);

        LatLng malmoe = new LatLng(55.576131, 12.991073);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(malmoe, 15));
    }

    private void getDirections(String origin, String destination) {

        MapClient.getInstance().getMapsApiService()
                .getDirectionsBetween(origin, destination)
                .enqueue(new Callback<Directions>() {
                    @Override
                    public void onResponse(Call<Directions> call, Response<Directions> response) {
                        Directions directions = response.body();
                        List<LatLng> route = PolyUtil.decode(directions.getRoutes().get(0)
                                .getOverviewPolyline().getPoints());

                        mMap.addPolyline(new PolylineOptions()
                                .addAll(route)
                                .width(10)
                                .color(Color.GREEN)
                                .geodesic(true));

                        String distance = directions.getRoutes().get(0).getLegs().get(0)
                                .getDistance().getText();
                        String duration = directions.getRoutes().get(0).getLegs().get(0)
                                .getDuration().getText();

                        mRouteInformation
                                .setText(String.format("Distance: %s, Duration: %s", distance, duration));

                    }

                    @Override
                    public void onFailure(Call<Directions> call, Throwable t) {

                    }
                });
    }

    private void getLocationFromAddress(String address) {
        MapClient.getInstance().getMapsApiService().getLocationFromAddress(address).enqueue(new Callback<Geocode>() {
            @Override
            public void onResponse(@NonNull Call<Geocode> call, @NonNull Response<Geocode> response) {
                Geocode geocode = response.body();
                System.out.println(geocode.getResults().get(0).getFormattedAddress());
            }

            @Override
            public void onFailure(Call<Geocode> call, Throwable t) {

            }
        });
    }

    private void getAddressFromLocation(String location) {
        MapClient.getInstance().getMapsApiService().getAddressFromLocation(location).enqueue(new Callback<Geocode>() {
            @Override
            public void onResponse(@NonNull Call<Geocode> call, @NonNull Response<Geocode> response) {
                Geocode geocode = response.body();
                System.out.println(geocode.getResults().get(0).getFormattedAddress());
            }

            @Override
            public void onFailure(Call<Geocode> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {

        if (mPoints.size() > 1) {
            mMap.clear();
            mPoints.clear();
            mRouteInformation.setText("");
        }

        mPoints.add(latLng);
        mMap.addMarker(new MarkerOptions().position(latLng).title("A marker"));

        if (mPoints.size() >= 2) {

            String origin = mPoints.get(0).latitude + "," + mPoints.get(0).longitude;
            String dest = mPoints.get(1).latitude + "," + mPoints.get(1).longitude;

            getDirections(origin, dest);
        }


    }
}
