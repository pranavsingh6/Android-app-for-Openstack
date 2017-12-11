package com.example.pranav.stack;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pranav on 11/08/15.
 */
public class App_urls {
    public final String TAG = App_urls.class.getSimpleName();
    public static int status = 0;
    public static String TenantName = "admin";
    public static String TenantID = "";
    public static String TokenID = "";
    public static String Username = "";
    public static JSONObject loginJSON = new JSONObject();
    public ArrayList<JSONObject> keystone_response = new ArrayList<JSONObject>();
    public static JSONObject computeJSONObject = new JSONObject();
    public static Map<String, String> allURLs = new HashMap();

    //Server login url
    public static String HOST="10.0.2.2";
    public static String HOST_URL="http://".concat(HOST);
    public static String URL_LOGIN = HOST_URL.concat(":5000/v2.0/tokens");




    public String getTokenID(JSONObject jsonObject) {
        String tokenID = "";
        try {
            tokenID = jsonObject.getJSONObject("access").getJSONObject("token").getString("id");
            TokenID = tokenID;
        } catch (JSONException e) {
            Log.d(TAG, "Error Occurred while building JSON");
        }
        return tokenID;
    }

    public String getTenantID(JSONObject jsonObject) {
        try {
            TenantID= jsonObject.getJSONObject("access").getJSONObject("token").getJSONObject("tenant").getString("id");

        } catch (JSONException e) {
            Log.d(TAG, "Error Occurred while building JSON");
        }
        return TenantID;
    }

    public String mapLocalhost(String s){
        if(s.contains("127.0.0.1"))
        {
            return s.replace("127.0.0.1", "10.0.2.2");
        }/*
        else if(s.contains("172.16.69.180")){

            return s.replace("172.16.69.180", "10.0.2.2");
        }
        else

            */
        return s;
    }

    public void getAllURLs(JSONObject jsonObject) {
        try {
            getTenantID(jsonObject);
            getTokenID(jsonObject);
            JSONArray jsonArray = jsonObject.getJSONObject("access").getJSONArray("serviceCatalog");
            for (int j = 0; j < jsonArray.length(); j++) {
                keystone_response.add(jsonArray.getJSONObject(j));
                    for (int i = 0; i < jsonArray.getJSONObject(j).getJSONArray("endpoints").length(); i++) {
                        allURLs.put(jsonArray.getJSONObject(j).getString("type"), mapLocalhost(jsonArray.getJSONObject(j).getJSONArray("endpoints").getJSONObject(0).getString("adminURL")));
                        //Instance.computeURL = mapLocalhost(Instance.computeURL);
                        //Log.d(TAG,"---------------ALL------------------\n");
                        //Log.d(TAG,jsonArray.getJSONObject(j).getString("type"));
                        //Log.d(TAG,jsonArray.getJSONObject(j).getJSONArray("endpoints").getJSONObject(0).getString("adminURL"));
                        //Log.d(TAG,"---------------End------------------\n");

                    }
            }
        } catch (JSONException e) {
            Log.d(TAG, "Error Occurred while building JSON");
        }
    }
}
