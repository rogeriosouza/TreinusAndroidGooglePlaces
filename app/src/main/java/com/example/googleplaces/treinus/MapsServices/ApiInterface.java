package com.example.googleplaces.treinus.MapsServices;

import com.example.googleplaces.treinus.model.Places;
import com.example.googleplaces.treinus.model.ResultDistanceMatrix;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("place/nearbysearch/json?")
    Call<Places.Source> doPlaces(@Query(value = "type", encoded = true) String type, @Query(value = "location", encoded = true) String location, @Query(value = "opennow", encoded = true) boolean opennow, @Query(value = "rankby", encoded = true) String rankby, @Query(value = "key", encoded = true) String key);


    @GET("distancematrix/json?") // origins/destinations:  LatLng as string
    Call<ResultDistanceMatrix> getDistance(@Query("key") String key, @Query("origins") String origins, @Query("destinations") String destinations);

    /*@GET("place/photo?maxwidth=400")
    Call<Places.Photos> getPhotos (@Query("photoreference")String photoReference);*/



}
