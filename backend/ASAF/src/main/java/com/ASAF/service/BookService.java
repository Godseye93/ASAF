package com.ASAF.service;

import com.ASAF.dto.BookDTO;
import com.ASAF.entity.*;
import com.ASAF.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
    // 도서 목록 중복 제거 및 중복 개수 포함 조회
    public List<Map<String, Object>> findDistinctBookDTOsWithBookNameCountByClassRegionAndGeneration(int class_code, int region_code, int generation_code) {
        List<BookEntity> books = bookRepository.findBooksByClassRegionAndGeneration(class_code, region_code, generation_code);

        // Convert to BookDTO
        List<BookDTO> bookDTOList = books.stream()
                .map(bookEntity -> new BookDTO(bookEntity))
                .collect(Collectors.toList());

        Map<String, Integer> bookNameCount = bookDTOList.stream()
                .collect(Collectors.groupingBy(BookDTO::getBookName, Collectors.reducing(0, book -> 1, Integer::sum)));

        List<BookDTO> distinctBookDTOList = bookDTOList.stream()
                .filter(distinctByKey(BookDTO::getBookName)) // 중복 제거 (bookName 값 기준)
                .collect(Collectors.toList());

        Map<String, Long> trueBorrowStateCount = countTrueBorrowState(bookDTOList);

        List<Map<String, Object>> response = distinctBookDTOList.stream().map(bookDTO -> {
            Map<String, Object> bookMap = new ObjectMapper().convertValue(bookDTO, TreeMap.class);
            bookMap.put("bookNameCount", bookNameCount.get(bookDTO.getBookName()));
            bookMap.put("borrowDate", bookDTO.getFormattedBorrowDate());
            bookMap.put("returnDate", bookDTO.getFormattedReturnDate());
            bookMap.put("trueBorrowStateCount", trueBorrowStateCount.getOrDefault(bookDTO.getBookName(), 0L));
            bookMap.remove("formattedBorrowDate");
            bookMap.remove("formattedReturnDate");
            return bookMap;
        }).collect(Collectors.toList());

        return response;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Map<String, Long> countTrueBorrowState(List<BookDTO> bookDTOList) {
        return bookDTOList.stream()
                .filter(bookDTO -> bookDTO.getBorrowState())
                .collect(Collectors.groupingBy(BookDTO::getBookName, Collectors.counting()));
    }

    public List<BookDTO> findBorrowedBooksByClassRegionAndGenerationSortedByName(int class_code, int region_code, int generation_code) {
        List<BookEntity> borrowedBooks = bookRepository.findBooksByClassRegionAndGenerationAndBorrowState(class_code, region_code, generation_code);

        // Convert to BookDTO and sort by bookName
        List<BookDTO> bookDTOList = borrowedBooks.stream()
                .map(bookEntity -> new BookDTO(bookEntity))
                .sorted(Comparator.comparing(BookDTO::getBookName))
                .collect(Collectors.toList());

        return bookDTOList;
    }

    // 본인이 빌린 책 목록 보여주기
    public List<BookDTO> findBorrowedBooksByUserId(int memberId) {
        List<BookEntity> borrowedBooks = bookRepository.findByBorrowStateTrueAndId_id(memberId);

        // Convert to BookDTO
        List<BookDTO> bookDTOList = borrowedBooks.stream()
                .map(bookEntity -> new BookDTO(bookEntity))
                .collect(Collectors.toList());

        return bookDTOList;
    }

    // 책 대출하기
    public BookDTO borrowBook(int book_number, BookDTO bookDTO) {
        Optional<BookEntity> bookEntityOptional = bookRepository.findById(book_number);

        if (bookEntityOptional.isPresent()) {
            BookEntity bookEntity = bookEntityOptional.get();
            BookDTO originalBookDTO = new BookDTO(bookEntity);

            if (originalBookDTO.getBorrowState() == bookDTO.getBorrowState()) {
                return null;
            }

            // 도서 대출 정보 업데이트
            bookEntity.setBorrower(bookDTO.getBorrower());
            bookEntity.setBorrowDate(bookDTO.getBorrowDate());
            bookEntity.setReturnDate(bookDTO.getReturnDate());
            bookEntity.setBorrowState(bookDTO.getBorrowState());

            MemberEntity member = memberRepository.findById(bookDTO.getId())
                    .orElseThrow(() -> new NotFoundException("Member not found"));
            bookDTO.updateMemberId(bookEntity, member);

            // 업데이트 된 데이터 저장
            bookRepository.save(bookEntity);

            return new BookDTO(bookEntity);
        } else {
            throw new NotFoundException("Book with book_number " + book_number + " not found");
        }
    }
}