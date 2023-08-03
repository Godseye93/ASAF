package com.ASAF.service;

import com.ASAF.dto.FcmDataMessage;
import com.ASAF.dto.MemberDTO;
import com.ASAF.dto.Message;
import com.ASAF.dto.Notification;
import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.NoticeEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Component
public class FirebaseCloudMessageDataService {
    @Value("${firebase.messaging.server-key}")
    private String firebaseCloudMessagingServerKey;

    private static final Logger logger = LoggerFactory.getLogger(FirebaseCloudMessageDataService.class);

    public final ObjectMapper objectMapper;


    public FirebaseCloudMessageDataService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/ssafy-common-proj/messages:send";

    // 클라이언트 토큰 등록


    // 클라이언트 토큰 불러오기


    /**
     * FCM에 push 요청을 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
     * @return
     * @throws IOException
     */
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "/ASAF_FCM_KEY.json";

        // GoogleApi를 사용하기 위해 oAuth2를 이용해 인증한 대상을 나타내는객체
        GoogleCredentials googleCredentials = GoogleCredentials
                // 서버로부터 받은 service key 파일 활용
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                // 인증하는 서버에서 필요로 하는 권한 지정
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        String token = googleCredentials.getAccessToken().getTokenValue();
        System.out.println("서버토큰 : " + token);
        return token;
    }


    /**
     * targetToken에 해당하는 device로 FCM 푸시 알림 전송
     * background 대응을 위해서 data로 전송한다.
     * @param targetToken
     * @param title
     * @param body
     * @throws IOException
     */
    public void sendDataMessageTo(String targetToken, String title, String body, String img) throws IOException {
        String message = makeDataMessage(targetToken, title, body, img);
        logger.info("message : {}", message);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                // 전송 토큰 추가
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    /**
     * FCM 알림 메시지 생성
     * background 대응을 위해서 data로 전송한다.
     * @param targetToken
     * @param title
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    private String makeDataMessage(String targetToken, String title, String body,String img) throws JsonProcessingException, IOException {
//    	Notification noti = new Notification(title, body, img);
        Map<String,String> map = new HashMap<>();
        map.put("title", title);
        map.put("body", body);
        map.put("image", img);

        Message message = new Message();
//    	message.setNotification(noti);
        message.setToken(targetToken);
        message.setData(map);

        FcmDataMessage fcmMessage = new FcmDataMessage(false, message);

        return objectMapper.writeValueAsString(fcmMessage);
    }


// 예약 발송
    public void sendNotificationToUsers_reservation(List<MemberEntity> users, NoticeEntity noticeEntity, String sender, Long sendTime ) throws IOException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Date sendDate = new Date(sendTime);
        long initialDelay = sendDate.getTime() - System.currentTimeMillis();

        String title = noticeEntity.getTitle();
        String content = noticeEntity.getContent();
        String body = String.format("작성자 : %s \n %s", sender, content);

        if (initialDelay <= 0) {
            // 이미 지난 시간인 경우 즉시 전송하도록 예외처리
            for (MemberEntity user : users) {
                sendNotificationToUser(user, title, body);
            }
        }else{
            // 예약 발송
            scheduler.schedule(() -> {
                for (MemberEntity user : users) {
                    try {
                        sendNotificationToUser(user, title, body);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, initialDelay, TimeUnit.MILLISECONDS);
        }
    }
// 공지를 각 유저에게 할당
    public void sendNotificationToUsers(List<MemberEntity> users, NoticeEntity noticeEntity, String sender ) throws IOException {
        String title = noticeEntity.getTitle();
        String content = noticeEntity.getContent();


        String body = String.format("[%s] \n %s", sender, content);

        for (MemberEntity user : users) {
            sendNotificationToUser(user, title, body);
        }
    }
//
    // 각 유저에게 보낼 공지를 작성
    private void sendNotificationToUser(MemberEntity user, String title, String body) throws IOException {
        String token = user.getToken();

        // Create Notification and Message instances
        Notification notification = new Notification(title, body, null);
        sendDataMessageTo(token, title, body, "123");

    }
}
