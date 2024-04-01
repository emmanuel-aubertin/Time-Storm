package com.example.timestorm.edtutils;

import com.example.timestorm.HelloApplication;
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
 * Implemented as a singleton to ensure a single instance throughout the application.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class ClassroomCollection {
    private static final Dictionary<String, ArrayList<Classroom>> classroomDict = new Hashtable<>();
    private static final ArrayList<String> siteList = new ArrayList<>();
    private static ClassroomCollection instance;
    private static boolean isInitialized = false;
    private final String API_URL = "https://edt-api.univ-avignon.fr/api/salles";

    /**
     * Private constructor to prevent instantiation.
     */
    private ClassroomCollection() {
    }

    /**
     * Initializes the singleton instance with data from the API.
     * @param user The LoginProvider used to authenticate the API request.
     */
    private void initialize() {
        if (!isInitialized) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(API_URL)
                        .method("GET", null)
                        .addHeader("token", HelloApplication.user.getToken())
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

                        String name = classroomObj.optString("name");
                        String code = classroomObj.optString("code");
                        String searchString = classroomObj.optString("searchString");
                        tempClassrooms.add(new Classroom(name, code, searchString));
                    }

                    String letter = resultObj.optString("letter");
                    siteList.add(letter);
                    classroomDict.put(letter, tempClassrooms);
                }
                isInitialized = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the singleton instance of ClassroomCollection, initializing it if necessary.
     * @param user The LoginProvider, used only if the instance is not initialized yet.
     * @return The singleton instance of ClassroomCollection.
     */
    public static synchronized ClassroomCollection getInstance() {
        if (instance == null) {
            instance = new ClassroomCollection();
            instance.initialize();
        }
        return instance;
    }

    public static boolean isIsInitialized() {
        return isInitialized;
    }

    /**
     * Retrieves a list of classrooms whose search string contains the provided input string.
     *
     * @param inputStr The input string to search for within classroom search strings.
     * @return An ArrayList of Classroom objects whose search strings contain the input string.
     *         Returns null if the input string is empty or if not initialized.
     */
    public ArrayList<Classroom> getClassroomLike(String inputStr) {
        if (inputStr.isEmpty() || !isInitialized) {
            return null;
        }
        inputStr = inputStr.toUpperCase();
        ArrayList<Classroom> output = new ArrayList<>();
        for (String site : siteList) {
            ArrayList<Classroom> classrooms = classroomDict.get(site);
            if (classrooms != null) {
                for (Classroom classroom : classrooms) {
                    if (classroom.getSearchString().contains(inputStr)) {
                        output.add(classroom);
                    }
                }
            }
        }
        return output;
    }
}
