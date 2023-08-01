//package com.ASAF.dto;
//
//import com.ASAF.entity.SeatEntity;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//import java.util.List;
//import java.util.Map;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class SeatDTO {
//    private Long seat_id;
//    private int seat_num;
//    private String name;
//
//    private DocumentDTO documentDTO;
//    private ClassInfoDTO classInfoDTO;
//    private ClassDTO classDTO;
//    private RegionDTO regionDTO;
//    private GenerationDTO generationDTO;
//
//    public SeatDTO(SeatEntity seatEntity) {
//        this.seat_id = seatEntity.getSeat_id();
//        this.seat_num = seatEntity.getSeat_num();
//        this.name = seatEntity.getName();
//        this.documentDTO = DocumentDTO.toDocumentDTO(seatEntity.getDocumentEntity());
//        this.classInfoDTO = ClassInfoDTO.toClassInfoDTO(seatEntity.getClassInfoEntity());
//        this.classDTO = ClassDTO.toClassDTO(seatEntity.getClassEntity());
//        this.regionDTO = RegionDTO.toRegionDTO(seatEntity.getRegionEntity());
//        this.generationDTO = GenerationDTO.toGenerationDTO(seatEntity.getGenerationEntity());
//    }
//
////    public static SeatDTO toSeatDTO(SeatEntity seatEntity) {
////        SeatDTO seatDTO = new SeatDTO();
////        seatDTO.setSeat_id(seatEntity.getSeat_id());
////        seatDTO.setSeat_num(seatEntity.getSeat_num());
////        seatDTO.setName(seatEntity.getName());
////        return seatDTO;
////    }
//}
