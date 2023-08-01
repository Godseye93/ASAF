package com.ASAF.service;

import com.ASAF.dto.BookDTO;
import com.ASAF.entity.*;
import com.ASAF.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    @Autowired
    private ClassInfoRepository classInfoRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private GenerationRepository generationRepository;
    @Autowired
    private MemberRepository memberRepository;

    public Map<String, Object> findBooksByClassRegionAndGeneration(int class_code, int region_code, int generation_code) {
        List<BookEntity> books = bookRepository.findBooksByClassRegionAndGeneration(class_code, region_code, generation_code);

        // Convert to BookDTO
        List<BookDTO> bookDTOList = books.stream()
                .map(bookEntity -> new BookDTO(bookEntity))
                .collect(Collectors.toList());

        int count = books.size();

        Map<String, Object> response = new HashMap<>();
        response.put("books", bookDTOList);
        response.put("count", count);

        return response;
    }

//     도서 등록
    public BookDTO registerBook(BookDTO bookDTO) {
        ClassInfoEntity classInfo = classInfoRepository.findById(bookDTO.getClass_num())
                .orElseThrow(() -> new NotFoundException("ClassInfo not found"));

        ClassEntity classCode = classRepository.findById(bookDTO.getClass_code())
                .orElseThrow(() -> new NotFoundException("Class not found"));

        RegionEntity regionCode = regionRepository.findById(bookDTO.getRegion_code())
                .orElseThrow(() -> new NotFoundException("Region not found"));

        GenerationEntity generationCode = generationRepository.findById(bookDTO.getGeneration_code())
                .orElseThrow(() -> new NotFoundException("Generation not found"));

        MemberEntity member = memberRepository.findById(bookDTO.getId())
                .orElseThrow(() -> new NotFoundException("Member not found"));

        BookEntity bookEntity = bookDTO.toEntity(classInfo, classCode, regionCode, generationCode, member);
        bookRepository.save(bookEntity);

        return new BookDTO(bookEntity);
    }

    public class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

//     도서 정보 수정
    public BookDTO updateBook(int book_number, BookDTO bookDTO) {
    BookEntity bookEntity = bookRepository.findById(book_number)
            .orElseThrow(() -> new NotFoundException("Book not found"));

    ClassInfoEntity classInfo = classInfoRepository.findById(bookDTO.getClass_num())
            .orElseThrow(() -> new NotFoundException("ClassInfo not found"));

    ClassEntity classCode = classRepository.findById(bookDTO.getClass_code())
            .orElseThrow(() -> new NotFoundException("Class not found"));

    RegionEntity regionCode = regionRepository.findById(bookDTO.getRegion_code())
            .orElseThrow(() -> new NotFoundException("Region not found"));

    GenerationEntity generationCode = generationRepository.findById(bookDTO.getGeneration_code())
            .orElseThrow(() -> new NotFoundException("Generation not found"));

    MemberEntity member = memberRepository.findById(bookDTO.getId())
            .orElseThrow(() -> new NotFoundException("Member not found"));

    bookDTO.updateEntity(bookEntity, classInfo, classCode, regionCode, generationCode, member);
    bookRepository.save(bookEntity);

    return new BookDTO(bookEntity);
}


    // 도서 삭제
    public void deleteBook(int book_number) {
        BookEntity bookEntity = bookRepository.findById(book_number)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        bookRepository.delete(bookEntity);
    }

    // 도서 정보 가져오기 (한 권)
    public BookDTO getBook(int book_number) {
        BookEntity bookEntity = bookRepository.findById(book_number).orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
        return new BookDTO(bookEntity);
    }

    // 도서 정보 가져오기 (전체 도서)
    public List<BookDTO> findBookDTOsByClassRegionAndGeneration(int class_code, int region_code, int generation_code) {
        List<BookEntity> bookEntities = bookRepository.findBooksByClassRegionAndGeneration(class_code, region_code, generation_code);
        return bookEntities.stream()
                .map(BookDTO::fromBookEntity)
                .collect(Collectors.toList());
    }
}
