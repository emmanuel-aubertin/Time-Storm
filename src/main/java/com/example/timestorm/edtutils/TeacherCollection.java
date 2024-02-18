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
 * Represents a collection of teachers at Avignon University.
 * Provides methods to interact with and retrieve information about teachers.
 * This class fetches data from Avignon University's API.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class TeacherCollection {
    // Dictionary with letter and list of teachers
    private final Dictionary<String, ArrayList<Teacher>> teacherDict = new Hashtable<>();
    private final String API_URL = "https://edt-api.univ-avignon.fr/api/enseignants";

    /**
     * Constructs a TeacherCollection object and populates it with data
     * retrieved from Avignon University's API using the provided LoginProvider.
     *
     * @param user The LoginProvider used to authenticate the API request.
     */
    public TeacherCollection(LoginProvider user) {
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

            JSONObject teacherJSON = new JSONObject(responseData);
            JSONArray resultsArray = teacherJSON.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = resultsArray.getJSONObject(i);

                JSONArray namesArray = resultObj.getJSONArray("names");
                ArrayList<Teacher> tempTeachers = new ArrayList<>();

                for (int j = 0; j < namesArray.length(); j++) {
                    JSONObject teacherObj = namesArray.getJSONObject(j);

                    String name = teacherObj.getString("name");
                    String code = teacherObj.getString("code");
                    String uapvRH = teacherObj.getString("uapvRH");
                    String searchString = teacherObj.getString("searchString");
                    tempTeachers.add(new Teacher(name, code, uapvRH, searchString));
                }

                String letter = resultObj.getString("letter");
                teacherDict.put(letter, tempTeachers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of teachers whose search string matches the provided input string.
     *
     * @param inputStr The input string to match against teacher search strings.
     * @return An ArrayList of Teacher objects whose search strings match the input string.
     *         Returns null if the input string is empty.
     */
    public ArrayList<Teacher> getTeacherLike(String inputStr){
        if(inputStr.isEmpty()){
            return null;
        }
        inputStr = inputStr.toUpperCase();
        ArrayList<Teacher> teachers = teacherDict.get(String.valueOf(inputStr.charAt(0)));
        if(inputStr.length() == 1){
            return teachers;
        }
        ArrayList<Teacher> output = new ArrayList<Teacher>();
        for(int i=0; i < teachers.size(); i++){
            System.out.println("Checking : "+ teachers.get(i).getSearchString());
            if(inputStr.length() > teachers.get(i).getSearchString().length()) {continue;}
            for(int j=0; j < inputStr.length(); j++){
                if( inputStr.charAt(j) == teachers.get(i).getSearchString().charAt(j)){
                    if(j ==  inputStr.length()-1){
                        output.add(teachers.get(i));
                    }
                    continue;
                }
                break;
            }
        }
        return output;
    }
}
