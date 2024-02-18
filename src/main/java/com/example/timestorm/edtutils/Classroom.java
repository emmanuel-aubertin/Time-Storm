package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class Classroom {
    private String name;
    private String code;
    private String searchString;

    public Classroom(String name, String code, String searchString) {
        this.name = name;
        this.code = code;
        this.searchString = searchString.toUpperCase();
    }

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

    @Override
    public String toString() {
        return "Classroom{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", searchString='" + searchString + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getSearchString() {
        return searchString;
    }


}

