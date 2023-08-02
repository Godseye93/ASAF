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

    @PostMapping
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
        NoticeDTO result = noticeService.createNotice(noticeDTO);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeDTO> updateNotice(@PathVariable int id, @RequestBody NoticeDTO noticeDTO) {
        noticeDTO.setId(id);
        return ResponseEntity.ok(noticeService.updateNotice(noticeDTO));
    }

    @GetMapping
    public ResponseEntity<List<NoticeDTO>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable int id) {
        NoticeDTO noticeDTO = noticeService.getNoticeById(id);
        if (noticeDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(noticeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable int id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok().build();
    }

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

            // 각 사용자에게 알림을 보냅니다.
            firebaseCloudMessageDataService.sendNotificationToUsers(users, noticeEntity);

            return "Notification sent successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error sending notification: " + e.getMessage();
        }
    }


}
