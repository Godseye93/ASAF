// BusService.java
package com.ASAF.service;

import com.ASAF.dto.BusDTO;
import com.ASAF.dto.BusDTO;
import com.ASAF.dto.BusDTO;
import com.ASAF.dto.BusDTO;
import com.ASAF.entity.BusEntity;
import com.ASAF.entity.BusEntity;
import com.ASAF.entity.BusEntity;
import com.ASAF.entity.BusEntity;
import com.ASAF.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
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

    public void save(BusDTO busDTO) {
        BusEntity busEntity = BusEntity.toBusEntity(busDTO);
        busRepository.save(busEntity);
    }

    public BusDTO update(int busNum, String location) {
        Optional<BusEntity> busEntityOptional = busRepository.findByBusNum(busNum);
        if (busEntityOptional.isPresent()) {
            BusEntity busEntity = busEntityOptional.get();
            busEntity.setLocation(location);
            BusEntity updatedBusEntity = busRepository.save(busEntity);
            return BusDTO.toBusDTO(updatedBusEntity);
        }
        return null;
    }

}

