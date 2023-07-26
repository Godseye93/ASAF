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
        seatEntity.setSeat_num(seatDTO.getSeat_num());
        seatEntity.setName(seatDTO.getName());
        // SeatEntity를 데이터베이스에 저장
        SeatEntity savedSeatEntity = seatRepository.save(seatEntity);
        seatDTO.setSeat_id(savedSeatEntity.getSeat_id());
        return seatDTO;
    }

    // 자리 정보
    public List<SeatDTO> getAllSeats() {
        List<SeatEntity> seatEntities = seatRepository.findAll();
        return seatEntities.stream()
                .map(seatEntity -> new SeatDTO(seatEntity))
                .collect(Collectors.toList());
    }
}
