package com.ASAF.service;

import com.ASAF.dto.SeatDTO;
import com.ASAF.entity.SeatEntity;
import com.ASAF.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    // 배치 완료
    public SeatDTO completeSeat(SeatDTO seatDTO) {
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setSeat_id(seatDTO.getSeat_id());
        seatEntity.setSeat_assign(seatDTO.getSeat_assign());
        SeatEntity saveSeatEntity = seatRepository.save(seatEntity);
        seatDTO.setSeat_id(saveSeatEntity.getSeat_id());
        return seatDTO;
    }

    // 반 별 배치 정보 가져오기
    public List<SeatDTO> getSeatsByClass(int classCode) {
        List<SeatEntity> seatsByClass = seatRepository.findByClassCode(classCode);
        return seatsByClass.stream()
                .map(seatEntity -> new SeatDTO(seatEntity))
                .collect(Collectors.toList());
    }
}
