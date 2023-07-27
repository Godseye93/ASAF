package com.ASAF.service;

import com.ASAF.dto.LockerDTO;
import com.ASAF.entity.LockerEntity;
import com.ASAF.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LockerService {

    private final LockerRepository lockerRepository;

    // 배치 완료
    public LockerDTO completeLocker(LockerDTO lockerDTO) {
        LockerEntity lockerEntity = new LockerEntity();
        lockerEntity.setLocker_id(lockerDTO.getLocker_id());
        lockerEntity.setLocker_num(lockerDTO.getLocker_num());
        lockerEntity.setName(lockerDTO.getName());

        LockerEntity savedLockerEntity = lockerRepository.save(lockerEntity);
        lockerDTO.setLocker_id(savedLockerEntity.getLocker_id());
        return lockerDTO;
    }

    // 사물함 정보
    public List<LockerDTO> getAllLockers() {
        List<LockerEntity> lockerEntities = lockerRepository.findAll();
        return lockerEntities.stream()
                .map(lockerEntity -> new LockerDTO(lockerEntity))
                .collect(Collectors.toList());
    }
}
