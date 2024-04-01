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
 * Represents a collection of Promotions at Avignon University.
 * Provides methods to interact with and retrieve information about Promotions.
 * This class fetches data from Avignon University's API.
 * Implemented as a singleton to ensure a single instance throughout the application.
 *
 * @author Emmanuel Aubertin (from athomisos.com)
 */
public class PromotionCollection {
    private static final Dictionary<String, ArrayList<Promotion>> PromotionDict = new Hashtable<>();
    private static final ArrayList<String> siteList = new ArrayList<>();
    private static PromotionCollection instance;
    private static boolean isInitialized = false;
    private final String API_URL = "https://edt-api.univ-avignon.fr/api/elements";

    /**
     * Private constructor to prevent instantiation.
     */
    private PromotionCollection() {
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

                JSONObject PromotionJSON = new JSONObject(responseData);
                JSONArray resultsArray = PromotionJSON.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject resultObj = resultsArray.getJSONObject(i);

                    JSONArray namesArray = resultObj.getJSONArray("names");
                    ArrayList<Promotion> tempPromotions = new ArrayList<>();

                    for (int j = 0; j < namesArray.length(); j++) {
                        JSONObject PromotionObj = namesArray.getJSONObject(j);

                        String name = PromotionObj.optString("name");
                        String code = PromotionObj.optString("code");
                        String searchString = PromotionObj.optString("searchString");
                        tempPromotions.add(new Promotion(name, code, searchString));
                    }

                    String letter = resultObj.optString("letter");
                    siteList.add(letter);
                    PromotionDict.put(letter, tempPromotions);
                }
                isInitialized = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the singleton instance of PromotionCollection, initializing it if necessary.
     * @param user The LoginProvider, used only if the instance is not initialized yet.
     * @return The singleton instance of PromotionCollection.
     */
    public static synchronized PromotionCollection getInstance() {
        if (instance == null) {
            instance = new PromotionCollection();
            instance.initialize();
        }
        return instance;
    }

    public static boolean isIsInitialized() {
        return isInitialized;
    }

    /**
     * Retrieves a list of Promotions whose search string contains the provided input string.
     *
     * @param inputStr The input string to search for within Promotion search strings.
     * @return An ArrayList of Promotion objects whose search strings contain the input string.
     *         Returns null if the input string is empty or if not initialized.
     */
    public ArrayList<Promotion> getPromotionLike(String inputStr) {
        if (inputStr.isEmpty() || !isInitialized) {
            return null;
        }
        inputStr = inputStr.toUpperCase();
        ArrayList<Promotion> output = new ArrayList<>();
        for (String site : siteList) {
            ArrayList<Promotion> Promotions = PromotionDict.get(site);
            if (Promotions != null) {
                for (Promotion Promotion : Promotions) {
                    if (Promotion.getSearchString().contains(inputStr)) {
                        output.add(Promotion);
                    }
                }
            }
        }
        return output;
    }
}
