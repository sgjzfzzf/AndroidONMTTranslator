package com.example.androidonmttranslator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

public class TextTranslationResultActivity extends AppCompatActivity {

    static final String TAG = "Debug";
    TextView searchText;
    ListView searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_result);
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        String src = intent.getStringExtra("src");
        String dst = intent.getStringExtra("dst");
        searchText = findViewById(R.id.search_text);
        searchResults = findViewById(R.id.search_results);
        searchText.setText(query);
        HttpTextTranslationRequestThread httpTranslationRequest = new HttpTextTranslationRequestThread(src, dst, query, "http://192.168.3.48:8000/android/text/");
        httpTranslationRequest.start();
        try {
            httpTranslationRequest.join();
        } catch (InterruptedException e) {
            Log.d(TAG, "cannot join the thread");
            e.printStackTrace();
        }
        JSONParser json = httpTranslationRequest.getJsonParser();
        if (json == null) {
            Log.d(TAG, "JSON fails");
        } else if (!json.getErrorInfo().equals("")) {
            Log.d(TAG, String.format("error: %s", json.getErrorInfo()));
        } else {
            Log.d(TAG, String.format("%s", json));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_view, R.id.search_result_list_view);
            searchResults.setAdapter(arrayAdapter);
            for (Map.Entry<String, String> item : json.getContent().entrySet())
                arrayAdapter.add(String.format("%s: %s", item.getKey(), item.getValue()));
        }
    }
}