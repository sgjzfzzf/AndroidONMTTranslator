package com.example.androidonmttranslator;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONParser {

    static final String TAG = "Debug";

    private String from;
    private String to;
    private String errorInfo;
    private Map<String, String> translationsMap;

    public void parseJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject resultObject = jsonObject.getJSONObject("result");
        errorInfo = jsonObject.getString("error_info");
        from = resultObject.getString("from");
        to = resultObject.getString("to");
        JSONArray resultArray = resultObject.getJSONArray("trans_result");
        translationsMap = new HashMap<>();
        for (int i = 0; i < resultArray.length(); ++i) {
            JSONObject translationObject = (JSONObject) resultArray.get(i);
            translationsMap.put(translationObject.get("src").toString(), translationObject.get("dst").toString());
        }
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("from: %s, to: %s, error info: %s, content: %s", from, to, errorInfo, translationsMap.toString());
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public Map<String,String> getContent() {
        return translationsMap;
    }
}