package com.ASAF.entity;

import com.ASAF.dto.GenerationDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "generation")
public class GenerationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int generation_code;

    @Column
    private String generation_num;

    @OneToMany(mappedBy = "generation_code", cascade = CascadeType.ALL)
    private List<ClassInfoEntity> classInfoEntityList = new ArrayList<>();

    public static GenerationEntity toGenerationEntity(GenerationDTO generationDTO) {
        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(generationDTO.getGeneration_code());
        generationEntity.setGeneration_num(generationDTO.getGeneration_num());
        // 자동으로 행 추가시키는 코드
//        ClassInfoEntity classInfoEntity = new ClassInfoEntity();
//        classInfoEntity.setGeneration_code(generationEntity);
//        generationEntity.getClassInfoEntityList().add(classInfoEntity);



        return generationEntity;
    }

    public static GenerationEntity toUpdateGenerationEntity(GenerationDTO generationDTO) {
        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(generationDTO.getGeneration_code());
        generationEntity.setGeneration_num(generationDTO.getGeneration_num());

        return generationEntity;
    }
}
