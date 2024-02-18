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
import java.util.List;

public class TeacherCollection {
    private final Dictionary<String, List<Teacher>> teacherDict = new Hashtable<>();
    private final String API_URL = "https://edt-api.univ-avignon.fr/api/enseignants";

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
            System.out.println(resultsArray.length());
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = resultsArray.getJSONObject(i);
                System.out.println(resultObj.length());
                JSONArray namesArray = resultObj.getJSONArray("names");
                List<Teacher> tempTeachers = new ArrayList<>();
                System.out.println("namesArray " + namesArray.length());
                for (int j = 0; j < namesArray.length(); j++) {
                    JSONObject teacherObj = namesArray.getJSONObject(j);
                    System.out.println("teacherObj " + teacherObj.length());
                    System.out.println(teacherObj);
                    String name = teacherObj.getString("name");
                    String code = teacherObj.getString("code");
                    String uapvRH = teacherObj.getString("uapvRH");
                    String searchString = teacherObj.getString("searchString");
                    tempTeachers.add(new Teacher(name, code, uapvRH, searchString));
                    System.out.println("Teacher " + name + " added");
                }

                String letter = resultObj.getString("letter");
                teacherDict.put(letter, tempTeachers);
                System.out.println(letter + " Complete");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
