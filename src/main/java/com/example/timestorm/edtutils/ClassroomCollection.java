package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Represents a collection of classrooms at Avignon University.
 * Provides methods to interact with and retrieve information about classrooms.
 * This class fetches data from Avignon University's API.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class ClassroomCollection {
    private final Dictionary<String, ArrayList<Classroom>> classroomDict = new Hashtable<>();
    private final ArrayList<String> siteList = new ArrayList<String>();
    private final String API_URL = "https://edt-api.univ-avignon.fr/api/salles";

    /**
     * Constructs a ClassroomCollection object and populates it with data
     * retrieved from Avignon University's API using the provided LoginProvider.
     *
     * @param user The LoginProvider used to authenticate the API request.
     */
    public ClassroomCollection(LoginProvider user) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(API_URL)
                    .method("GET", null)
                    .addHeader("token", user.getToken())
                    .addHeader("Referer", "https://edt.univ-avignon.fr")
                    .addHeader("Origin", "https://edt.univ-avignon.fr/")
                    .build();

            Response response = client.newCall(request).execute();
            String responseData = response.body().string();

            JSONObject classroomJSON = new JSONObject(responseData);
            JSONArray resultsArray = classroomJSON.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = resultsArray.getJSONObject(i);

                JSONArray namesArray = resultObj.getJSONArray("names");

                ArrayList<Classroom> tempClassrooms = new ArrayList<>();

                for (int j = 0; j < namesArray.length(); j++) {
                    JSONObject classroomObj = namesArray.getJSONObject(j);

                    String name = classroomObj.getString("name");
                    String code = classroomObj.getString("code");
                    String searchString = classroomObj.getString("searchString");
                    tempClassrooms.add(new Classroom(name, code, searchString));
                }

                String letter = resultObj.getString("letter");
                siteList.add(letter);
                classroomDict.put(letter, tempClassrooms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of classrooms whose search string contains the provided input string.
     *
     * @param inputStr The input string to search for within classroom search strings.
     * @return An ArrayList of Classroom objects whose search strings contain the input string.
     *         Returns null if the input string is empty.
     */
    public ArrayList<Classroom> getClassroomLike(String inputStr){
        if(inputStr.isEmpty()) { return null; }
        inputStr = inputStr.toUpperCase();
        ArrayList<Classroom> output = new ArrayList<>();
        for (String site: siteList) {
            for (Classroom classroom: classroomDict.get(site)) {
                if(classroom.getSearchString().contains(inputStr)){
                    output.add(classroom);
                }
            }
        }
        return output;
    }
}
