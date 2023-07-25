// BusService.java
package com.ASAF.service;

import com.ASAF.dto.BusDTO;
import com.ASAF.entity.BusEntity;
import com.ASAF.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    public List<BusDTO> findAll() {
        List<BusEntity> bus = busRepository.findAll();
        return bus.stream().map(BusDTO::toBusDTO).collect(Collectors.toList());
    }

    // 2. 특정 지역 버스들을 조회
    public BusDTO findByRegion(int region_code) {
        Optional<BusEntity> optionalBusEntity = busRepository.findByRegion_code(region_code);
        if (optionalBusEntity.isPresent()){
            BusEntity busEntity = optionalBusEntity.get();
            return BusDTO.toBusDTO(busEntity);
        } else {
            return null;
        }
    }

}

