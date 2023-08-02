package com.ASAF.service;

import com.ASAF.dto.FcmDataMessage;
import com.ASAF.dto.Message;
import com.ASAF.dto.Notification;
import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.NoticeEntity;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirebaseCloudMessageDataService {

    @Value("${firebase.messaging.server-key}")
    private String firebaseCloudMessagingServerKey;

    private final HttpTransport httpTransport;
    private final JacksonFactory jacksonFactory;

    public FirebaseCloudMessageDataService(HttpTransport httpTransport, JacksonFactory jacksonFactory) {
        this.httpTransport = httpTransport;
        this.jacksonFactory = jacksonFactory;
    }

    // 공지를 각 유저에게 할당
    public void sendNotificationToUsers(List<MemberEntity> users, NoticeEntity noticeEntity) throws IOException {
        String title = noticeEntity.getTitle();
        String content = noticeEntity.getContent();
        int writer = noticeEntity.getSender();

        String body = String.format("작성자 : %s", writer, content);

        for (MemberEntity user : users) {
            sendNotificationToUser(user, title, body);
        }
    }

    // 각 유저에게 보낼 공지를 작성
    private void sendNotificationToUser(MemberEntity user, String title, String body) {
        String token = user.getToken();

        // Create Notification and Message instances
        Notification notification = new Notification(title, body, null);
        Message message = new Message(notification, token);

        try {
            boolean success = sendNotification(message);
            if (!success) {
                // Handle the case when the message failed to send
                // ...
            }
        } catch (IOException e) {
            System.err.println("Failed to send notification.");
            e.printStackTrace();
        }
    }

    // Firebase 주소의 인스턴스를 통해 작성된 공지를 전송
    private boolean sendNotification(Message message) throws IOException {
        FirebaseMessaging firebaseMessaging = getFirebaseMessagingInstance();
        if (firebaseMessaging != null) {
            FcmDataMessage fcmDataMessage = new FcmDataMessage(false, message);

            // Add code to send FcmDataMessage using FCM REST API or another method
            // ...

            return true;
        }
        return false;
    }

    // 사용할 Firebase 주소의 인스턴스 생성
    private FirebaseMessaging getFirebaseMessagingInstance() {
        try {
            FirebaseApp firebaseApp = FirebaseApp.getInstance(); // 기존에 초기화된 FirebaseApp 인스턴스를 가져옵니다.
            return FirebaseMessaging.getInstance(firebaseApp);
        } catch (Exception e) {
            System.err.println("Failed to initialize Firebase Cloud Messaging.");
            e.printStackTrace();
        }
        return null;
    }
}
