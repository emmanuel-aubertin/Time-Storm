package com.example.timestorm.edtutils;

import com.example.timestorm.HelloApplication;
import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a promotion (i.e. a group of students) at Avignon University.
 * Provides methods to retrieve information about the promotion and their schedule.
 *
 * @author Your Name
 */
public class Promotion {
    private final String name;
    private final String code;
    private final String searchString;

    /**
     * Constructs a Promotion object with the specified parameters.
     *
     * @param name         The name of the promotion.
     * @param code         The unique code associated with the promotion.
     * @param searchString A map of search strings associated with the promotion (used for matching).
     */
    public Promotion(String name, String code, String searchString) {
        this.name = name;
        this.code = code;
        this.searchString = searchString;
    }

    /**
     * Gets the name of the promotion.
     *
     * @return The name of the promotion.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique code associated with the promotion.
     *
     * @return The code of the promotion.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the search strings associated with the promotion.
     *
     * @return The search strings of the promotion.
     */
    public String getsearchString() {
        return searchString;
    }

    /**
     * Retrieves the schedule of the promotion from the Avignon University's API using the provided LoginProvider.
     *
     * @param user The LoginProvider used to authenticate the API request.
     * @return An ArrayList<Event> representing the schedule of the promotion.
     * @throws RuntimeException If an IOException occurs during the API request.
     */
    public ArrayList<Event> getPromotionEvents(LoginProvider user) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:5000/event/get/promotion/" + this.code)
                    .get()
                    .addHeader("Authorization", "Bearer " + HelloApplication.user.getToken())
                    .build();

            Response response = client.newCall(request).execute();

            JSONObject resJSON = new JSONObject(response.body().string());
            JSONArray resultsArray = resJSON.getJSONArray("results");
            ArrayList<Event> output = new ArrayList<Event>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = resultsArray.getJSONObject(i);

                Event event = new Event(resultObj.optString("code"), resultObj.optString("start"),
                        resultObj.optString("end"), resultObj.optString("title"),
                        resultObj.optString("memo"),
                        user);

                output.add(event);
            }
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a string representation of the Promotion object.
     *
     * @return A string representation of the Promotion object.
     */
    @Override
    public String toString() {
        return "Promotion{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", searchString=" + searchString +
                '}';
    }
}

