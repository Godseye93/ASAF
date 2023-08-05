package com.ASAF.dto;

import com.ASAF.entity.GenerationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GenerationDTO {
    private int generation_code;
    private String generation_num;

    public static GenerationDTO toGenerationDTO(GenerationEntity generationEntity) {
        GenerationDTO generationDTO = new GenerationDTO();
        generationDTO.setGeneration_code(generationEntity.getGeneration_code());
        generationDTO.setGeneration_num(generationEntity.getGeneration_num());
        return generationDTO;
    }
}
