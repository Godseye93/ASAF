package com.ASAF.service;

import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.NoticeEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class FirebaseCloudMessageDataService {
    // 내장 클래스 ObjectMapper의 인스턴스를 생성한다.
    public final ObjectMapper objectMapper;
    // 생성자
    public FirebaseCloudMessageDataService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    // 1-1. 공지 즉시 발송 컨트롤러에서 실행
    public void sendNotificationToUsers(List<MemberEntity> users, NoticeEntity noticeEntity, String sender, String profileImage) throws IOException {
        String title = noticeEntity.getTitle();
        String content = noticeEntity.getContent();
        String body = String.format("[%s] \n %s", sender, content);

        // 사람들의 토큰을 tokens 리스트에 저장한다.
        List<String> tokens = new ArrayList<>();
        for (MemberEntity user : users) {
            // 각 유저가 가지고 있는 토큰을 추출한다.
            if (tokens.isEmpty() || !tokens.contains(user.getToken())){
                tokens.add(user.getToken());
            }
        }

        if (!tokens.isEmpty()) {
            sendNotificationToTokens(tokens, title, body, profileImage);
        }
    }

    // 1-2. 예약 발송 컨트롤러에서 실행
    public void sendNotificationToUsers_reservation(List<MemberEntity> users, NoticeEntity noticeEntity, String sender, Long sendTime, String profileImage) throws IOException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Date sendDate = new Date(sendTime);
        long initialDelay = sendDate.getTime() - System.currentTimeMillis();

        String title = noticeEntity.getTitle();
        String content = noticeEntity.getContent();
        String body = String.format("[%s] \n %s", sender, content);
        List<String> tokens = new ArrayList<>();
        for (MemberEntity user : users) {
            if (tokens.isEmpty() || !tokens.contains(user.getToken())){
                tokens.add(user.getToken());
            }
        }

        if (noticeEntity.getNotification() == true){
            if (initialDelay <= 0) {
                // 이미 지난 시간인 경우 즉시 전송하도록 예외처리
                sendNotificationToTokens(tokens, title, body, profileImage);
            }else{
                // 예약 발송
                scheduler.schedule(() -> {

                    try {
                        sendNotificationToTokens(tokens, title, body, profileImage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }, initialDelay, TimeUnit.MILLISECONDS);
            }
        }
    }

    // 2. 해당 토큰에게 공지 발송
    public void sendNotificationToTokens(List<String> targetTokens, String title, String body, String img) throws IOException {
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", title)
                .putData("body", body)
                .putData("image", img)
                .addAllTokens(targetTokens)
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            System.out.println("Multicast 메시지를 보냄: " + response.getSuccessCount() + "개의 메시지가 성공");
            System.out.println("Multicast 메시지 실패: " + response.getFailureCount() + "개의 메시지 실패");
        }  catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
