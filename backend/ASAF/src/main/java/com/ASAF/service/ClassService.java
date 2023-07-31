package com.ASAF.service;

import com.ASAF.dto.ClassDTO;
import com.ASAF.entity.ClassEntity;
import com.ASAF.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    @Autowired
    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public ClassDTO findById(int id) {
        Optional<ClassEntity> optionalClassEntity = classRepository.findById(id);
        if (optionalClassEntity.isPresent()) {
            return ClassDTO.toClassDTO(optionalClassEntity.get());
        } else {
            return null;
        }
    }
}
