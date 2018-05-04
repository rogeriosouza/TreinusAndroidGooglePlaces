package com.example.googleplaces.treinus;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.googleplaces.treinus.MapsServices.APIPlaces;
import com.example.googleplaces.treinus.MapsServices.ApiInterface;
import com.example.googleplaces.treinus.adapter.RecyclerViewAdapter;
import com.example.googleplaces.treinus.model.Photo;
import com.example.googleplaces.treinus.model.Places;
import com.example.googleplaces.treinus.model.ResultDistanceMatrix;
import com.example.googleplaces.treinus.model.StoreModel;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    ApiInterface  apiMapsService;
    RecyclerView recyclerView;
    CardView cardView;
    private final static int ALL_PERMISSIONS_RESULT = 101;


    String latLngString;
    LatLng latLng;
    Button button;

    List<Places.Custom> results;
    List<StoreModel> storeModels;
    List<Places.Custom> photosModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        //permissions.add(READ_EXTERNAL_STORAGE);

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            else {
                searchLocation();
            }
        } else {
            searchLocation();
        }


        apiMapsService = APIPlaces.getClient().create(ApiInterface.class);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        cardView = (CardView) findViewById(R.id.card_view);



        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gym = "gym|health";
                searchStores(gym);

            }
        });


    }

    private void searchStores(String placeType) {

        Call<Places.Source> call = apiMapsService.doPlaces(placeType,latLngString,true,"distance",APIPlaces.GOOGLE_PLACE_API_KEY);

        call.enqueue(new Callback<Places.Source>() {
            @Override
            public void onResponse(Call<Places.Source> call, Response<Places.Source> response) {

                Places.Source source = response.body();


                if (response.isSuccessful()){
                    if (source.status.equalsIgnoreCase("OK")){

                        results = source.custom;


                           for (int i = 0; i < results.size();i++) {

                            if (i == 20)
                                break;
                            Places.Custom  info =  results.get(i);


                            storeModels = new ArrayList<>();


                            searchDistance (info);



                        }

                    }else {
                        Toast.makeText(getApplicationContext(), "Nenhuma Academia encontrada próximo(a) de você.", Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() != 200) {
                    Toast.makeText(getApplicationContext(), "Error " + response.code() + " Encontrado.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Places.Source> call, Throwable t) {

                call.cancel();
            }
        });
    }

    private void searchDistance( final Places.Custom info) {

        Call<ResultDistanceMatrix> call = apiMapsService.getDistance(APIPlaces.GOOGLE_PLACE_API_KEY,
                latLngString,info.geometry.locationA.lat+","+ info.geometry.locationA.lng);


        call.enqueue(new Callback<ResultDistanceMatrix>() {
            @Override
            public void onResponse(Call<ResultDistanceMatrix> call, Response<ResultDistanceMatrix> response) {

                ResultDistanceMatrix resultDistance = response.body();

                if("OK".equalsIgnoreCase(resultDistance.status)){

                    ResultDistanceMatrix.InfoDistanceMatrix infoDistanceMatrix = resultDistance.rows.get(0);

                    ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement distanceElement = infoDistanceMatrix.elements.get(0);


                    if("OK".equalsIgnoreCase(distanceElement.status)){

                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDuration = distanceElement.duration;
                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDistance = distanceElement.distance;
                        String distanceTotal = String.valueOf(itemDistance.text);
                        String durationTotal = String.valueOf(itemDuration.text);


                        Log.d("distanceTotal", distanceTotal);
                        Log.d("durationTotal", durationTotal);


                        storeModels.add(new StoreModel(info.name, info.vicinity, distanceTotal, durationTotal));



                        if (storeModels.size() == 20 || storeModels.size() == results.size()) {
                            RecyclerViewAdapter adapterStores = new RecyclerViewAdapter(results, storeModels);
                            recyclerView.setAdapter(adapterStores);



                        }


                    }



                }

            }

            @Override
            public void onFailure(Call<ResultDistanceMatrix> call, Throwable t) {

                call.cancel();

            }
        });
    }

    private void searchPhotos(final Places.Custom infoPhotos){



    }


    private void searchLocation() {

        SmartLocation.with(this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        latLngString = location.getLatitude() + "," + location.getLongitude();
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                });


    }


    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("Essas permissões são obrigatórias para o app. Permita o acesso.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
                else {
                    searchLocation();
                }

                break;
        }




        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
