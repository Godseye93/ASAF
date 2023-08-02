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
