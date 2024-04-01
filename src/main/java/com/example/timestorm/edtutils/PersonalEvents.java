package com.example.timestorm.edtutils;

import com.example.timestorm.HelloApplication;
import com.example.timestorm.LoginProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonalEvents {
    private List<Event> persoEvents = new ArrayList<>();

    public PersonalEvents(LoginProvider user) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:5000/event/get/personal")
                    .get()
                    .addHeader("Authorization", "Bearer " + HelloApplication.user.getToken())
                    .build();

            Response response = client.newCall(request).execute();

            JSONObject resJSON = new JSONObject(response.body().string());
            JSONArray resultsArray = resJSON.getJSONArray("results");

            // Create a thread pool
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<Callable<Void>> tasks = new ArrayList<>();

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultObj = resultsArray.getJSONObject(i);
                tasks.add(() -> {
                    System.out.println(resultObj.optString("title"));
                    persoEvents.add(new Event("", resultObj.optString("start"),
                            resultObj.optString("end"), resultObj.optString("title"),
                            "", user));
                    return null;
                });
            }

            // Invoke all tasks
            executor.invokeAll(tasks);

            // Shutdown the executor
            executor.shutdown();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}