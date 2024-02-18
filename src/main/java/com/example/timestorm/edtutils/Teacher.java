package com.example.timestorm.edtutils;

import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class Teacher {
    private String FirstName;
    private String LastName;
    private String Code;
    private String UapvHR;
    private String SearchString;

    public Teacher(String name, String code, String uapvHR, String searchString) {
        String[] nameParts = name.split(" ");
        FirstName = nameParts[0];
        LastName = nameParts.length > 1 ? nameParts[1] : "";
        Code = code;
        UapvHR = uapvHR;
        SearchString = searchString.toUpperCase();
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getCode() {
        return Code;
    }

    public String getUapvHR() {
        return UapvHR;
    }

    public String getSearchString() {
        return SearchString;
    }

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

    @Override
    public String toString() {
        return "Teacher{" +
                "FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Code='" + Code + '\'' +
                ", UapvHR='" + UapvHR + '\'' +
                ", SearchString='" + SearchString + '\'' +
                '}';
    }
}


