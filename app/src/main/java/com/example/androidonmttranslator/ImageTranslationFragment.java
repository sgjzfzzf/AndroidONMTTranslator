package com.example.androidonmttranslator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ImageTranslationFragment extends Fragment {

    static final String TAG = "Debug";
    View view;
    Spinner srcLangSpinner;
    Spinner dstLangSpinner;
    Button cameraButton;
    Button localImageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_translation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srcLangSpinner = view.findViewById(R.id.src_lang);
        dstLangSpinner = view.findViewById(R.id.dst_lang);
        cameraButton = view.findViewById(R.id.camera_button);
        localImageButton = view.findViewById(R.id.local_button);

        cameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Activity activity = getActivity();
            if (activity == null) {
                Log.d(TAG, "activity null");
            } else {
                activity.startActivityForResult(intent, MainActivity.IMAGE_CAPTURE_REQUEST_CODE);
            }
        });

        localImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Activity activity = getActivity();
            if (activity == null) {
                Log.d(TAG, "activity null");
            } else {
                activity.startActivityForResult(intent, MainActivity.IMAGE_SELECT_REQUEST_CODE);
            }
        });

    }

}
