package com.ASAF.controller;

import com.ASAF.dto.MemberDTO;
import com.ASAF.dto.NoticeDTO;
import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.NoticeEntity;
import com.ASAF.service.FirebaseCloudMessageDataService;
import com.ASAF.service.MemberService;
import com.ASAF.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FirebaseCloudMessageDataService firebaseCloudMessageDataService;

    // DB에 저장하지 않고 공지를 즉시 전송합니다.
    @PostMapping("/immediate")
    public ResponseEntity<Boolean> createNoticeImmediate(@RequestBody List<NoticeDTO> noticeDTOList) throws IOException {
        System.out.println("<공지 즉시 보내기>");
        System.out.println("총 인원 : " + noticeDTOList.size() + "명");
        if (noticeDTOList != null && !noticeDTOList.isEmpty()) {
            List<MemberEntity> users = new ArrayList<>();
            System.out.println("발신 시각 : " + noticeDTOList.get(0).getSend_time());
            System.out.println("------수신인 목록------");
            for (NoticeDTO data : noticeDTOList) {
                // users 에 받는 사람 정보들을 저장한다.
                users.add(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())));
                System.out.println(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())).getMemberName());
            }
            // sender에 발신인 이름을 저장한다.
            String sender = memberService.findById(noticeDTOList.get(0).getSender()).getMemberName();
            // noticeEntity에 공지 내용을 저장한다.
            NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTOList.get(0));
            // 발신인, 수신인, 공지내용을 첨부하여 서비스 메서드를 실행한다.
            firebaseCloudMessageDataService.sendNotificationToUsers(users,noticeEntity ,sender);
            return ResponseEntity.ok(true);
        } else {
            System.out.println("실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    // 공지 예약 전송
    @PostMapping
    public ResponseEntity<Boolean> createNotice(@RequestBody List<NoticeDTO> noticeDTOList) throws IOException {
        System.out.println("<공지 생성>");
        System.out.println("총 인원 : " + noticeDTOList.size() + "명");
        if (noticeDTOList != null && !noticeDTOList.isEmpty()) {
            List<MemberEntity> users = new ArrayList<>();
            System.out.println("발신 시각 : " + noticeDTOList.get(0).getSend_time());
            System.out.println("------수신인 목록------");
            for (NoticeDTO data : noticeDTOList) {
                // users 에 각 수신인 정보를 저장한다.
                users.add(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())));
                // 각 공지를 DB에 저장한다.
                NoticeDTO result = noticeService.createNotice(data);
                System.out.println(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())).getMemberName());
            }
            // sender에 발신인 이름을 저장한다.
            String sender = memberService.findById(noticeDTOList.get(0).getSender()).getMemberName();
            // noticeEntity에 공지 내용을 저장한다.
            NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTOList.get(0));
            // sendTime에 발신 시각을 저장한다.
            Long sendTime = NoticeEntity.toNoticeEntity(noticeDTOList.get(0)).getSend_time();
            // 발신인, 수신인, 공지 내용, 발신 시각을 첨부하여 서비스 메서드를 실행한다.
            firebaseCloudMessageDataService.sendNotificationToUsers_reservation(users,noticeEntity ,sender,sendTime);
            return ResponseEntity.ok(true);
        } else {
            System.out.println("실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    // 공지 조회
    @GetMapping
    public ResponseEntity<List<NoticeDTO>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    // 공지 조회 /{id}
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable int id) {
        NoticeDTO noticeDTO = noticeService.getNoticeById(id);
        if (noticeDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(noticeDTO);
    }

    // 공지 수정
    @PutMapping("/{id}")
    public ResponseEntity<NoticeDTO> updateNotice(@PathVariable int id, @RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setId(id);
        return ResponseEntity.ok(noticeService.updateNotice(noticeDTO));
    }

    // 공지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable int id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }

    // 즉시 발송 (알람)
    // 데이터를 리스트형식으로 받는다.
    // users 리스트배열을 생성하고 receiver에 해당하는 유저들을 넣는다.
    // sender 문자열배열을 생성하고 sender의 이름을 찾아서 넣는다.
    // noticeEntity라는 NoticeEntity 인스턴스를 생성하고 첫번째 발송 내용을 넣는다.

    @PostMapping("/immediate")
    public ResponseEntity<List<NoticeDTO>> createNoticeimmediate(@RequestBody List<NoticeDTO> noticeDTOList) throws IOException {
        // List<NoticeDTO> results = noticeService.createNotice(noticeDTOList);
        System.out.println("total : " + noticeDTOList.size());
        if (noticeDTOList != null && !noticeDTOList.isEmpty()) {
            // 수정해야함 테스트 데이터임
            List<MemberEntity> users = new ArrayList<>();
            for (NoticeDTO data : noticeDTOList) {
                System.out.println("receiver : " + data.getReceiver());
                users.add(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())));
            }

            String sender = memberService.findById(noticeDTOList.get(0).getSender()).getMemberName();
            NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTOList.get(0));
            // 각 사용자에게 알림을 보냅니다.
            firebaseCloudMessageDataService.sendNotificationToUsers(users,noticeEntity ,sender);


            return ResponseEntity.ok(noticeDTOList);
        } else {
            System.out.println("데이터가 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // DB 저장 후 예약 발송
    @GetMapping("/send/{noticeId}")
    public String sendNotification(@PathVariable("noticeId") int noticeId) {
        try {
             // 사용자 목록을 가져옵니다. findAll() 메서드를 사용합니다.
            List<MemberDTO> userList = memberService.findAll();
            System.out.println(userList);

            // MemberDTO 목록을 MemberEntity 목록으로 변환합니다.
            List<MemberEntity> users = new ArrayList<>();
            for (MemberDTO userDTO : userList) {
                users.add(MemberEntity.toMemberEntity(userDTO));
            }

            // 알림을 보낼 공지사항을 가져옵니다. (NoticeDTO를 반환합니다.)
            NoticeDTO noticeDTO = noticeService.getNoticeById(noticeId);
            System.out.println(noticeDTO);

            // NoticeDTO를 NoticeEntity로 변환합니다.
            NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTO);

            // 수정해야함 테스트 데이터임
            String sender = memberService.findById(1).getMemberName();

            // 각 사용자에게 알림을 보냅니다.
            firebaseCloudMessageDataService.sendNotificationToUsers(users, noticeEntity,sender);

            return "Notification sent successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error sending notification: " + e.getMessage();
        }
    }


}
