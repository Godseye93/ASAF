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
                if(data.getReceiver() != data.getSender()) {
                    users.add(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())));
                }
                System.out.println(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())).getMemberName());
            }
            // sender에 발신인 이름을 저장한다.
            String sender = memberService.findById(noticeDTOList.get(0).getSender()).getMemberName();
            // noticeEntity에 공지 내용을 저장한다.
            NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTOList.get(0));
            // profileImage에 발신인 프로필 이미지를 저장한다.
            String profileImage =  memberService.findById(noticeDTOList.get(0).getSender()).getProfile_image();
            // 발신인, 수신인, 공지내용을 첨부하여 서비스 메서드를 실행한다.
            firebaseCloudMessageDataService.sendNotificationToUsers(users,noticeEntity ,sender, profileImage);
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
            // 공지 1개를 DB에 저장한다.
            NoticeDTO result = noticeService.createNotice(noticeDTOList.get(0));
            for (NoticeDTO data : noticeDTOList) {
                System.out.println(data.getRegisterTime());
                // users 에 각 수신인 정보를 저장한다.
                if(data.getReceiver() != data.getSender()) {
                    users.add(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())));
                }
                System.out.println(MemberEntity.toMemberEntity(memberService.findById(data.getReceiver())).getMemberName());
            }
            // sender에 발신인 이름을 저장한다.
            String sender = memberService.findById(noticeDTOList.get(0).getSender()).getMemberName();
            // noticeEntity에 공지 내용을 저장한다.
            NoticeEntity noticeEntity = NoticeEntity.toNoticeEntity(noticeDTOList.get(0));
            // sendTime에 발신 시각을 저장한다.
            Long sendTime = NoticeEntity.toNoticeEntity(noticeDTOList.get(0)).getSend_time();
            // profileImage에 발신인 프로필 이미지를 저장한다.
            String profileImage =  memberService.findById(noticeDTOList.get(0).getSender()).getProfile_image();
            // 발신인, 수신인, 공지 내용, 발신 시각을 첨부하여 서비스 메서드를 실행한다.
            firebaseCloudMessageDataService.sendNotificationToUsers_reservation(users,noticeEntity ,sender,sendTime, profileImage);
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
    @PutMapping
    public ResponseEntity<Boolean> updateNotice(@RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setId(noticeDTO.getId());
        noticeService.updateNotice(noticeDTO);
        System.out.println(noticeDTO);
        return ResponseEntity.ok(true);
    }

    // 공지 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteNotice(@PathVariable int id) {
        noticeService.deleteNotice(id);
        System.out.println("삭제 완료");
        return ResponseEntity.ok(true);
    }

    // 공지를 조회 (sender & registerTime)
    @GetMapping("/getBySenderIdAndDate")
    public ResponseEntity<List<NoticeEntity>> getNoticesBySenderAndDate(@RequestParam int sender, @RequestParam long registerTime) {
        System.out.println(registerTime);
        System.out.println(sender);
        List<NoticeEntity> notices = noticeService.getNoticesBySenderAndRegisterTime(sender, registerTime);
        if (notices.isEmpty()) {
            System.out.println("비었음");
            return ResponseEntity.notFound().build();
        }
        System.out.println(notices);
        return ResponseEntity.ok(notices);
    }
}
