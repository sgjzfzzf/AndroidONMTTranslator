package com.example.androidonmttranslator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TextTranslationFragment extends Fragment {

    ImageView projectLogoView;
    SearchView searchView;
    Spinner srcLangSpinner;
    Spinner dstLangSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_translation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        projectLogoView = view.findViewById(R.id.project_logo);
        searchView = view.findViewById(R.id.search_view);
        srcLangSpinner = view.findViewById(R.id.src_lang);
        dstLangSpinner = view.findViewById(R.id.dst_lang);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Activity activity = getActivity();
                if (activity != null) {
                    String[] flags = getResources().getStringArray(R.array.flags);
                    Intent intent = new Intent(activity.getApplicationContext(), TextTranslationResultActivity.class);
                    intent.putExtra("query", s);
                    intent.putExtra("src", flags[srcLangSpinner.getSelectedItemPosition()]);
                    intent.putExtra("dst", flags[dstLangSpinner.getSelectedItemPosition()]);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

}
