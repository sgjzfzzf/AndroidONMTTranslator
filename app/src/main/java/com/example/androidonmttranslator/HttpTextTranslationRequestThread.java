package com.example.androidonmttranslator;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpTextTranslationRequestThread extends Thread {

    static final String TAG = "Debug";

    private final String from;
    private final String to;
    private final String q;
    private final String url;
    private JSONParser jsonParser;

    public HttpTextTranslationRequestThread(String from, String to, String q, String url) {
        this.from = from;
        this.to = to;
        this.q = q;
        this.url = url;
    }

    public void sendTextTranslation() {
        try {
            Map<String, String> data = new HashMap<>();
            data.put("from", this.from);
            data.put("to", this.to);
            data.put("q", this.q);
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "open connection");
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.d(TAG, "try to connect");
            conn.connect();
            Log.d(TAG, "succeed");
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(transfer_correct_query(data).getBytes());
            outputStream.close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "http 200");
                InputStream inputStream = conn.getInputStream();
                InputStreamReader in = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                str = stringBuilder.toString();
                Log.d(TAG, String.format("raw data: %s", str));
                inputStream.close();
                in.close();
                reader.close();
                conn.disconnect();
                this.jsonParser = this.toJSONParser(str);
            } else {
                Log.d(TAG, "not http 200");
            }
        } catch (IOException e) {
            Log.d(TAG, "fail to connect");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d(TAG, "fail to parse json");
            e.printStackTrace();
        }
    }

    static String transfer_correct_query(Map<String, String> data) {
        StringBuilder queryBuilder = new StringBuilder();
        for (Map.Entry<String, String> item : data.entrySet()) {
            queryBuilder.append(String.format("%s=%s&", item.getKey(), item.getValue()));
        }
        return queryBuilder.toString();
    }

    public JSONParser toJSONParser(String json) throws JSONException {
        JSONParser jsonParser = new JSONParser();
        jsonParser.parseJson(json);
        return jsonParser;
    }

    @Override
    public void run() {
        super.run();
        this.sendTextTranslation();
    }

    public JSONParser getJsonParser() {
        return this.jsonParser;
    }
}
