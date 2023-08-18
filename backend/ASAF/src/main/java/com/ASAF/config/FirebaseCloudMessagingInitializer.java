package com.ASAF.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseCloudMessagingInitializer {
    public void initializeFirebaseApp() throws IOException {
        // 윈도우 경로를 사용하므로, 경로를 올바른 형태로 변경하였습니다.
//        String jsonPath = "C:\\Users\\SSAFY\\Desktop\\S09P12D103\\backend\\ASAF\\src\\main\\resources\\ASAF_FCM_KEY.json";
        String jsonPath = "/home/ubuntu/ASAF_FCM_KEY.json";
//        String jsonPath = "C:\\Users\\user\\Desktop\\S09P12D103\\backend\\ASAF\\src\\main\\resources\\ASAF_FCM_KEY.json";


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(jsonPath)))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
