package com.example.mvopo.tsekapp.Model;

import org.json.JSONObject;

/**
 * Created by mvopo on 11/21/2017.
 */

public class ServiceAvailed {
    public String id;
    public JSONObject request;

    public ServiceAvailed(String id, JSONObject request) {
        this.id = id;
        this.request = request;
    }
}
