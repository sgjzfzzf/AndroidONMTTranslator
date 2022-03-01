package com.example.androidonmttranslator;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpImageTranslationRequestThread extends Thread {

    static final int BUFFER_SIZE = 0x1000;
    static final String TAG = "Debug";
    static final String BOUNDARY = "8ffV5qFM0HiG0qA3JCQeeWBTSwAEsxPD";
    static final String PARAM_PART_FORMAT = "\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"\r\n\r\n%s";
    static final String FILE_PART_FORMAT = "\r\n--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\n\r\n";
    static final String[] ALLOWED_IMAGE_FORM = {"jpg", "jpeg", "png"};

    private final String from;
    private final String to;
    private final String filename;
    private final String url;
    private JSONParser jsonParser;

    public HttpImageTranslationRequestThread(String from, String to, String filename, String url) {
        this.from = from;
        this.to = to;
        this.filename = filename;
        this.url = url;
    }


    public void sendImageTranslation() {
        try {
            boolean flag = false;
            for (String allowedForm : ALLOWED_IMAGE_FORM) {
                if (this.filename.equals(allowedForm)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return;
            }
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    String.format("multipart/form-data; boundary=%s", BOUNDARY));
            conn.setRequestProperty("Connection", "close");
            conn.connect();
            OutputStream outputStream = conn.getOutputStream();
            Map<String, String> params = new HashMap<>();
            params.put("from", from);
            params.put("to", to);
            Map<String, byte[]> files = new HashMap<>();
            FileInputStream is = new FileInputStream(filename);
            byte[] content = new byte[BUFFER_SIZE];
            if (is.read(content) < 0) {
                return;
            }
            files.put(filename, content);
            is.close();
            send_files(outputStream, params, files);
            outputStream.close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                InputStreamReader in = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                str = stringBuilder.toString();
                inputStream.close();
                in.close();
                reader.close();
                conn.disconnect();
                this.jsonParser = this.toJSONParser(str);
            } else {
                Log.d(TAG, "sendImageTranslation: ");
            }
        } catch (IOException e) {
            Log.d(TAG, "fail to open connection");
            e.printStackTrace();
        } catch (FileFormatErrorException e) {
            Log.d(TAG, "fail to format file");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d(TAG, "fail to parse json");
            e.printStackTrace();
        }
    }


    static String transfer_correct_query(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> item : params.entrySet()) {
            String key = item.getKey();
            String value = item.getValue();
            builder.append(String.format(PARAM_PART_FORMAT, HttpImageTranslationRequestThread.BOUNDARY, key, value));
        }
        return builder.toString();
    }

    static void send_files(OutputStream outputStream, Map<String, String> params, Map<String, byte[]> files)
            throws IOException, FileFormatErrorException {
        outputStream.write(transfer_correct_query(params).getBytes());
        for (Map.Entry<String, byte[]> file : files.entrySet()) {
            String filename = file.getKey();
            byte[] content = file.getValue();
            outputStream.write(String.format(FILE_PART_FORMAT, HttpImageTranslationRequestThread.BOUNDARY, filename, filename).getBytes());
            outputStream.write(content);
        }
        outputStream.write(String.format("\r\n--%s\r\n", HttpImageTranslationRequestThread.BOUNDARY).getBytes());
    }

    public JSONParser toJSONParser(String json) throws JSONException {
        JSONParser jsonParser = new JSONParser();
        jsonParser.parseJson(json);
        return jsonParser;
    }

    @Override
    public void run() {
        super.run();
        this.sendImageTranslation();
    }

    public JSONParser getJsonParser() {
        return this.jsonParser;
    }

}


class FileFormatErrorException extends Exception {
}