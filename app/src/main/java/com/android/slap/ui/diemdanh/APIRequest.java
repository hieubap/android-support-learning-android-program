package com.android.slap.ui.diemdanh;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

public class APIRequest {
    public APIRequest() {}

    public void callGet(){
        try{
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Create URL
                    try{
                        URL githubEndpoint = new URL("https://firebase.googleapis.com/v1alpha/projects/-/apps/1:592900011890:web:6c46b518a66d5656548feb/webConfig");
// Create connection
                        HttpsURLConnection myConnection =
                                (HttpsURLConnection) githubEndpoint.openConnection();
                        myConnection.setRequestProperty("X-Goog-Api-Key", "AIzaSyD1Dr_T9bFDX2Vc7BK1s-uEeVxskk_qtvA");
                        if (myConnection.getResponseCode() == 200) {
                            InputStream responseBody = myConnection.getInputStream();
                            InputStreamReader responseBodyReader =
                                    new InputStreamReader(responseBody, "UTF-8");
                            JsonReader jsonReader = new JsonReader(responseBodyReader);
                            jsonReader.beginObject(); // Start processing the JSON object
                            Map<String,Object> map = new HashMap<>();
                            while (jsonReader.hasNext()) { // Loop through all keys
                                String key = jsonReader.nextName(); // Fetch the next key
                                map.put(key,jsonReader.nextString());
                            }
                            map.size();
                            // Success
                            // Further processing here
                        } else {
                            // Error handling code goes here
                        }
                    }catch(Exception e){
                        int i = 0;
                    }

                }
            });
        }catch(Exception e){

        }

    }

    String boundary = "-------------" + System.currentTimeMillis();
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    public void callPost(File file){
        try{
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    // Create URL
                    try{
                        URL url = new URL("");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                        // Set request headers or parameters if needed
                        // connection.setRequestProperty("HeaderKey", "HeaderValue");

                        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + lineEnd);
                        outputStream.writeBytes(lineEnd);

                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        outputStream.writeBytes(lineEnd);
                        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        outputStream.flush();
                        outputStream.close();


                        if (connection.getResponseCode() == 200) {
                            InputStream responseBody = connection.getInputStream();
                            InputStreamReader responseBodyReader =
                                    new InputStreamReader(responseBody, "UTF-8");
                            JsonReader jsonReader = new JsonReader(responseBodyReader);
                            jsonReader.beginObject(); // Start processing the JSON object
                            Map<String,Object> map = new HashMap<>();
                            while (jsonReader.hasNext()) { // Loop through all keys
                                String key = jsonReader.nextName(); // Fetch the next key
                                map.put(key,jsonReader.nextString());
                            }
                            map.size();
                            // Success
                            // Further processing here
                        } else {
                            // Error handling code goes here
                        }
                    }catch(Exception e){
                        int i = 0;
                    }

                }
            });
        }catch(Exception e){

        }

    }
}
