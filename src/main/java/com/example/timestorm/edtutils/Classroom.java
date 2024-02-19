package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Represents a classroom at Avignon University.
 * Provides methods to retrieve information about the classroom and its schedule.
 * This class interacts with Avignon University's API to fetch data.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class Classroom {
    private String name;
    private String code;
    private String searchString;

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
     * @return A JSONObject representing the schedule of the classroom.
     * @throws RuntimeException If an IOException occurs during the API request.
     */
    public JSONObject getClassroomEdt(LoginProvider user){
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
            return new JSONObject(response.body().string());
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
