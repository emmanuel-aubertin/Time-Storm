package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a classroom at Avignon University.
 * Provides methods to retrieve information about the classroom and its schedule.
 * This class interacts with Avignon University's API to fetch data.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class Classroom {
    private final String name;
    private final String code;
    private final String searchString;

    /**
     * Constructs a Classroom object with the specified parameters.
     *
     * @param name         The name of the classroom.
     * @param code         The unique code associated with the classroom.
     * @param searchString The search string associated with the classroom (used for matching).
     */
    public Classroom(String name, String code, String searchString) {
        this.name = name;
        this.code = code;
        this.searchString = searchString.toUpperCase();
    }

    /**
     * Retrieves the schedule of the classroom from Avignon University's API using the provided LoginProvider.
     *
     * @param user The LoginProvider used to authenticate the API request.
     * @return An ArrayList<Event> representing the schedule of the classroom.
     * @throws RuntimeException If an IOException occurs during the API request.
     */
    public ArrayList<Event> getClassroomEdt(LoginProvider user){
        System.out.println("https://edt-api.univ-avignon.fr/api/salles/" + this.code);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://edt-api.univ-avignon.fr/api/events_salle/" + this.code)
                    .method("GET", null)
                    .addHeader("token", user.getToken())
                    .addHeader("Referer", "https://edt.univ-avignon.fr")
                    .addHeader("Origin", "https://edt.univ-avignon.fr/")
                    .build();

            Response response = client.newCall(request).execute();

            JSONObject resJSON = new JSONObject(response.body().string());
            JSONArray resultsArray = resJSON.getJSONArray("results");
            ArrayList<Event> output = new ArrayList<Event>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = resultsArray.getJSONObject(i);
                output.add(new Event(resultObj.getString("code"), resultObj.getString("start"),
                        resultObj.getString("end"), resultObj.getString("title"),
                        resultObj.getString("favorite"), user));
            }
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a string representation of the Classroom object.
     *
     * @return A string representation of the Classroom object.
     */
    @Override
    public String toString() {
        return "Classroom{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", searchString='" + searchString + '\'' +
                '}';
    }

    /**
     * Gets the name of the classroom.
     *
     * @return The name of the classroom.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique code associated with the classroom.
     *
     * @return The code of the classroom.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the search string associated with the classroom.
     *
     * @return The search string of the classroom.
     */
    public String getSearchString() {
        return searchString;
    }
}
