package com.ASAF.service;

import com.ASAF.dto.FcmDataMessage;
import com.ASAF.dto.Message;
import com.ASAF.dto.Notification;
import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.NoticeEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class FirebaseCloudMessageDataService {
    // 파이어베이스에 로그를 남긴다.
    private static final Logger logger = LoggerFactory.getLogger(FirebaseCloudMessageDataService.class);
    // 내장 클래스 ObjectMapper의 인스턴스를 생성한다.
    public final ObjectMapper objectMapper;
    // 생성자
    public FirebaseCloudMessageDataService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    // Firebasestorage Clouding Message의 주소
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/ssafy-common-proj/messages:send";

    // 1-1. 공지 즉시 발송 컨트롤러에서 실행합니다.
    public void sendNotificationToUsers(List<MemberEntity> users, NoticeEntity noticeEntity, String sender ) throws IOException {
        String title = noticeEntity.getTitle();
        String content = noticeEntity.getContent();
        // 발신인과 내용를 조합해서 전체 내용를 만듭니다.
        String body = String.format("[%s] \n %s", sender, content);
        for (MemberEntity user : users) {
            // 각 유저 정보 & 제목 & 전체 내용을 첨부시켜 sendNotificationToUser 메서드를 실행합니다.
            sendNotificationToUser(user, title, body);
        }
    }
    // 1-2. 각 유저에게 보낼 공지를 작성
    private void sendNotificationToUser(MemberEntity user, String title, String body) throws IOException {
        // 유저 정보에서 토큰을 가져옵니다.
        String token = user.getToken();
        // 제목 & 전체 내용 & 이미지를 첨부시킨 notification 인스턴스를 만듭니다.
        String image = user.getToken();
        // 토큰 & 제목 & 전체 내용 & 이미지를 첨부시켜 sendDataMessageTo메서드를 실행합니다.
        sendDataMessageTo(token, title, body, "image");
    }

    // 1. 예약 발송 컨트롤러에서 실행합니다.
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

    /**
     * targetToken에 해당하는 device로 FCM 푸시 알림 전송
     * background 대응을 위해서 data로 전송한다.
     * @param targetToken
     * @param title
     * @param body
     * @throws IOException
     */
    // 2-1 토큰, 제목, 전체 내용, 이미지를 사용해 Firebase Cloud Messaging 으로 최종 요청합니다.
    public void sendDataMessageTo(String targetToken, String title, String body, String img) throws IOException {
        // makeDataMessage를 사용하여 메시지를 생성합니다..
        String message = makeDataMessage(targetToken, title, body, img);
        logger.info("message : {}", message);
        // 내장 클래스 OkHttpClient의 인스턴스 client를 생성합니다..
        OkHttpClient client = new OkHttpClient();
        // 내장 클래스 RequestBody의 인스턴스 requestBody를 생성합니다..
        // message를 첨부하여 requestBody의 메소드 create를 실행합니다.
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        // 내장 클래스 Request의 인스턴스 request를 생성합니다.
        // request의 url을 Firebasestorage Clouding Message의 주소,
        // post를 requestBody,
        // addHeader에 전송 토큰을 넣고 빌드합니다. 전송 토큰은 미리 정의해 둔 메서드의 리턴값으로 받아옵니다.
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
        // 내장클래스 Response의 인스턴스 response를 생성하고 request를 담아 내장 메서드 execute()를 실행합니다.
        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
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
    // 2-2 토큰, 제목, 전체 내용, 이미지를 조합해 메시지를 만듭니다.
    private String makeDataMessage(String targetToken, String title, String body, String img) throws JsonProcessingException, IOException {
        // HashMap라는 내장 클래스를 사용해서 map 인스턴스를 만듭니다. dir와 흡사합니다.
        Map<String,String> map = new HashMap<>();
        // map 인스턴스에 key와 value로 제목, 전체 내용, 이미지를 넣어줍니다.
        map.put("title", title);
        map.put("body", body);
        map.put("image", img);

        // 미리 정의 해둔 메시지 인스턴스를 만듭니다.
        Message message = new Message();
        // 메시지의 토큰값을 설정합니다.
        message.setToken(targetToken);
        // 메시지의 데이터값을 설정합니다.
        message.setData(map);

        // 미리 정의 해둔 FcmDataMessage 인스턴스 fcmMessage를 만듭니다.
        FcmDataMessage fcmMessage = new FcmDataMessage(false, message);
        // fcmMessage를 첨부하여 내장 클래스의 인스턴스인 objectMapper의 writeValueAsString 메서드를 실행합니다.
        return objectMapper.writeValueAsString(fcmMessage);
    }

    /**
     * FCM에 push 요청을 보낼 때 인증을 위해 Header에 포함시킬 AccessToken 생성
     * @return
     * @throws IOException
     */
    // 2-3 전송 토큰을 받아옵니다.
    private String getAccessToken() throws IOException {
        // 파이어베이스 속성 경로 firebaseConfigPath를 생성합니다.
        String firebaseConfigPath = "/ASAF_FCM_KEY.json";
        // GoogleApi를 사용하기 위해 oAuth2를 이용해 인증한 대상을 나타내는객체
        // 구글 인증 클래스의 인스턴스를 생성하고 파이어베이스 속성경로를 담아 메서드를 실행합니다.
        GoogleCredentials googleCredentials = GoogleCredentials
                // 서버로부터 받은 service key 파일 활용
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                // 인증하는 서버에서 필요로 하는 권한 지정
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        // GoogleCredentials의 내장함수 refreshIfExpired()를 실행합니다.
        googleCredentials.refreshIfExpired();
        // 생성되어 GoogleCredentials의 인스턴스에 담긴 토큰을 담을 token를 생성합니다.
        String token = googleCredentials.getAccessToken().getTokenValue();
//        System.out.println("서버토큰 : " + token);
        return token;
    }
}
