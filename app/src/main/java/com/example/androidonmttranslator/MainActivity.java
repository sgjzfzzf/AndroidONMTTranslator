package com.example.androidonmttranslator;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;

import com.google.android.material.tabs.TabLayout;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int IMAGE_CAPTURE_REQUEST_CODE = 0x0;
    static final int IMAGE_SELECT_REQUEST_CODE = 0x1;
    static final String TAG = "Debug";

    TextTranslationFragment textTranslationFragment;
    ImageTranslationFragment imageTranslationFragment;
    ArrayList<Pair<String, Fragment>> fragments;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_CAPTURE_REQUEST_CODE:
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    Intent intent = new Intent(MainActivity.this, TextTranslationResultActivity.class);
                    String[] allowedLanguages = getResources().getStringArray(R.array.allowed_languages);
                    intent.putExtra("file", bitmap);
                    intent.putExtra("src", allowedLanguages[imageTranslationFragment.srcLangSpinner.getSelectedItemPosition()]);
                    intent.putExtra("dst", allowedLanguages[imageTranslationFragment.dstLangSpinner.getSelectedItemPosition()]);
                }
                break;
            case IMAGE_SELECT_REQUEST_CODE:
                if (data != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments = new ArrayList<>();
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        textTranslationFragment = new TextTranslationFragment();
        imageTranslationFragment = new ImageTranslationFragment();
        fragments.add(new Pair<>(getString(R.string.text), textTranslationFragment));
        fragments.add(new Pair<>(getString(R.string.image), imageTranslationFragment));
        ContentFragmentPaperAdapter contentFragmentPaperAdapter = new ContentFragmentPaperAdapter(getSupportFragmentManager(), R.id.view_pager, fragments);
        viewPager.setAdapter(contentFragmentPaperAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
