package com.example.googleplaces.treinus;

import com.example.googleplaces.treinus.MapsServices.ApiInterface;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LocalTestServerContext {

    ApiInterface api;
    private final MockWebServer server;

    public LocalTestServerContext(MockWebServer server) {
        this.server = server;
    }




    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        //api.doPlaces()

    }

    /*LocalTestServerContext(String responseBody) throws IOException {

        this.server = new MockWebServer();
        MockResponse response = new MockResponse();
        response.setHeader("Content-Type", "application/json");
        response.setBody(responseBody);
        server.enqueue(response);
        server.start();

        this.context =
                new GeoApiContext.Builder()
                        .apiKey("AIzaSyBSBCGrebQ_4zna97UYDLybHlT13U1P8Dg")
                        .baseUrlForTesting("http://127.0.0.1:" + server.getPort())
                        .build();
    }*/



}