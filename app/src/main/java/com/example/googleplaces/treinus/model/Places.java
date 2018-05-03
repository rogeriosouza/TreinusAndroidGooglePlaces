package com.example.googleplaces.treinus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/***
 * @author rogerio
 * @since 01/05/18
 *
 */
public class Places {

    public class Source implements Serializable {

        @SerializedName("results")
        public List<Custom> custom = new ArrayList<>();
        @SerializedName("status")
        public String status;
    }

    public class Custom implements Serializable {


        @SerializedName("geometry")
        public Geometry geometry;
        @SerializedName("vicinity")
        public String vicinity;
        @SerializedName("name")
        public String name;

    }

    public class Geometry implements Serializable{

        @SerializedName("location")
        public LocationA locationA;

    }

    public class LocationA implements Serializable {

        @SerializedName("lat")
        public String lat;
        @SerializedName("lng")
        public String lng;


    }



}
