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
 * Represents a collection of teachers at Avignon University.
 * Provides methods to interact with and retrieve information about teachers.
 * This class fetches data from Avignon University's API.
 * Singleton pattern implemented to ensure only one instance exists.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class TeacherCollection {
    private static final Dictionary<String, ArrayList<Teacher>> teacherDict = new Hashtable<>();
    private static TeacherCollection instance;
    private static boolean isInitialized = false;

    /**
     * Private constructor to prevent instantiation.
     */
    private TeacherCollection() {
    }

    public static boolean isIsInitialized(){
        return isInitialized;
    }

    /**
     * Initializes the singleton instance with data from the API.
     * @param user The LoginProvider used to authenticate the API request.
     */
    private void initialize() {
        if (!isInitialized) {
            try {
                OkHttpClient client = new OkHttpClient();
                String API_URL = "https://edt-api.univ-avignon.fr/api/enseignants";
                Request request = new Request.Builder()
                        .url(API_URL)
                        .method("GET", null)
                        .addHeader("token", HelloApplication.user.getToken())
                        .addHeader("Referer", "https://edt.univ-avignon.fr")
                        .addHeader("Origin", "https://edt.univ-avignon.fr/")
                        .build();


                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                System.out.println(responseData);
                JSONObject teacherJSON = new JSONObject(responseData);
                JSONArray resultsArray = teacherJSON.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject resultObj = resultsArray.getJSONObject(i);

                    JSONArray namesArray = resultObj.getJSONArray("names");
                    ArrayList<Teacher> tempTeachers = new ArrayList<>();

                    for (int j = 0; j < namesArray.length(); j++) {
                        JSONObject teacherObj = namesArray.getJSONObject(j);

                        String name = teacherObj.getString("name");
                        String code = teacherObj.optString("code");
                        String uapvRH = teacherObj.getString("uapvRH");
                        String searchString = teacherObj.getString("searchString");
                        tempTeachers.add(new Teacher(name, code, uapvRH, searchString));
                    }

                    String letter = resultObj.optString("letter");
                    teacherDict.put(letter, tempTeachers);
                }
                isInitialized = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the singleton instance of TeacherCollection, initializing it if necessary.
     * @param user The LoginProvider, used only if the instance is not initialized yet.
     * @return The singleton instance of TeacherCollection.
     */
    public static synchronized TeacherCollection getInstance() {
        if (instance == null) {
            instance = new TeacherCollection();
            instance.initialize();
        }
        return instance;
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    public ArrayList<Teacher> getTeacherLike(String inputStr){
        if(inputStr.isEmpty() || !isInitialized){
            return null;
        }
        inputStr = inputStr.toUpperCase();
        ArrayList<Teacher> teachers = teacherDict.get(String.valueOf(inputStr.charAt(0)));
        if(teachers == null){
            return new ArrayList<>();
        }
        if(inputStr.length() == 1){
            return teachers;
        }
        ArrayList<Teacher> output = new ArrayList<>();
        for(Teacher teacher : teachers){
            if(inputStr.length() > teacher.getSearchString().length()) continue;
            if(teacher.getSearchString().startsWith(inputStr)){
                output.add(teacher);
            }
        }
        return output;
    }
}
