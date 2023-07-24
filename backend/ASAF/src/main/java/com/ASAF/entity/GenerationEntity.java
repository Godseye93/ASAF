package com.ASAF.entity;

import com.ASAF.dto.GenerationDTO;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "generation")
public class GenerationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long generation_code;

    @Column(nullable = false)
    private String generation_num;

    public static GenerationEntity toGenerationEntity(GenerationDTO generationDTO) {
        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(generationDTO.getGeneration_code());
        generationEntity.setGeneration_num(generationDTO.getGeneration_num());

        return generationEntity;
    }

    public static GenerationEntity toUpdateGenerationEntity(GenerationDTO generationDTO) {
        GenerationEntity generationEntity = new GenerationEntity();
        generationEntity.setGeneration_code(generationDTO.getGeneration_code());
        generationEntity.setGeneration_num(generationDTO.getGeneration_num());

        return generationEntity;
    }
}
