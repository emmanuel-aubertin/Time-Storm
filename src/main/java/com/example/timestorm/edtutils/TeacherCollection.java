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

public class TeacherCollection {
    private final Dictionary<String, ArrayList<Teacher>> teacherDict = new Hashtable<>();
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
