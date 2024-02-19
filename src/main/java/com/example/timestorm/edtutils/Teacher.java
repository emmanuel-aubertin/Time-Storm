package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Represents a teacher at Avignon University.
 * Provides methods to retrieve information about the teacher and their schedule.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class Teacher {
    private String Name;
    private String Code;
    private String UapvHR;
    private String SearchString;

    /**
     * Constructs a Teacher object with the specified parameters.
     *
     * @param name         The full name of the teacher.
     * @param code         The unique code associated with the teacher.
     * @param uapvHR       The UapvHR (University Avignon HR) identifier for the teacher.
     * @param searchString The search string associated with the teacher (used for matching).
     */
    public Teacher(String name, String code, String uapvHR, String searchString) {
        Name = name;
        Code = code;
        UapvHR = uapvHR;
        SearchString = searchString.toUpperCase();
    }

    /**
     * Gets the name of the teacher.
     *
     * @return The last name of the teacher.
     */
    public String getName() {
        return Name;
    }

    /**
     * Gets the unique code associated with the teacher.
     *
     * @return The code of the teacher.
     */
    public String getCode() {
        return Code;
    }

    /**
     * Gets the UapvHR identifier of the teacher.
     *
     * @return The UapvHR identifier of the teacher.
     */
    public String getUapvHR() {
        return UapvHR;
    }

    /**
     * Gets the search string associated with the teacher.
     *
     * @return The search string of the teacher.
     */
    public String getSearchString() {
        return SearchString;
    }

    /**
     * Retrieves the schedule of the teacher from the Avignon University's API using the provided LoginProvider.
     *
     * @param user The LoginProvider used to authenticate the API request.
     * @return A JSONObject representing the schedule of the teacher.
     * @throws RuntimeException If an IOException occurs during the API request.
     */
    public JSONObject getTeacherEdt(LoginProvider user) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://edt-api.univ-avignon.fr/api/events_enseignant/" + this.getUapvHR())
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
     * Returns a string representation of the Teacher object.
     *
     * @return A string representation of the Teacher object.
     */
    @Override
    public String toString() {
        return "Teacher{" +
                "Name='" + Name + '\'' +
                ", Code='" + Code + '\'' +
                ", UapvHR='" + UapvHR + '\'' +
                ", SearchString='" + SearchString + '\'' +
                '}';
    }
}
