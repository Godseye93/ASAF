package com.ASAF.controller;

import com.ASAF.dto.BookDTO;
import com.ASAF.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RequestMapping("/book")
@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    // 도서 등록
    @PostMapping
    public BookDTO registerBook(@RequestBody BookDTO bookDTO) {
        return bookService.registerBook(bookDTO);
    }

    // 도서 정보 수정
    @PutMapping("/{book_number}")
    public BookDTO updateBook(@PathVariable int book_number, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(book_number, bookDTO);
    }

    // 도서 대출, 반납하기
    @PutMapping("/borrow/{book_number}")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable int book_number, @RequestBody BookDTO bookDTO) {
        BookDTO result = bookService.borrowBook(book_number, bookDTO);

        if (result == null) {
            System.out.println("null값");
            System.out.println(book_number);
            System.out.println(bookDTO);
            return ResponseEntity.badRequest().build();
        } else {
            System.out.println("제대로 데이터 들어감");
            System.out.println(book_number);
            System.out.println(bookDTO);
            return ResponseEntity.ok(result);
        }
    }

    // 도서 정보 삭제
    @DeleteMapping("/{book_number}")
    public ResponseEntity<String> deleteBook(@PathVariable int book_number) {
        bookService.deleteBook(book_number);
        String message = String.format("%d번 도서 삭제성공", book_number);
        return ResponseEntity.ok(message);
    }

    // 도서 정보 가져오기 (한 권)
    @GetMapping("/{book_number}")
    public BookDTO getBook(@PathVariable int book_number) {
        return bookService.getBook(book_number);
    }

    // 도서 정보 가져오기 (전체 도서)
    @GetMapping("/{class_code}/{region_code}/{generation_code}")
    public ResponseEntity<List<BookDTO>> findBookDTOsByClassRegionAndGeneration(
            @PathVariable("class_code") int class_code,
            @PathVariable("region_code") int region_code,
            @PathVariable("generation_code") int generation_code) {

        List<BookDTO> books = bookService.findBookDTOsByClassRegionAndGeneration(class_code, region_code, generation_code);
        return ResponseEntity.ok(books);
    }

    // 도서 정보 가져오기 (전체 도서) (수량)
    @GetMapping("/distinct/{class_code}/{region_code}/{generation_code}")
    public ResponseEntity<List<Map<String, Object>>> findDistinctBookDTOsWithCountByClassRegionAndGeneration(
            @PathVariable("class_code") int class_code,
            @PathVariable("region_code") int region_code,
            @PathVariable("generation_code") int generation_code) {

        System.out.println(class_code);
        System.out.println(region_code);
        System.out.println(generation_code);
        List<Map<String, Object>> result = bookService.findDistinctBookDTOsWithBookNameCountByClassRegionAndGeneration(class_code, region_code, generation_code);

        return ResponseEntity.ok(result);
    }

    // 대출중인 책 목록 가져오기
    @GetMapping("/borrowed/{classCode}/{regionCode}/{generationCode}/sorted-by-name")
    public ResponseEntity<List<BookDTO>> findBooksInBorrowedStateSortedByName(
            @PathVariable("classCode") int classCode,
            @PathVariable("regionCode") int regionCode,
            @PathVariable("generationCode") int generationCode) {

        List<BookDTO> borrowedBooks = bookService.findBorrowedBooksByClassRegionAndGenerationSortedByName(classCode, regionCode, generationCode);

        return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
    }

    //학생정보로 빌린 책 목록 보여주기
    @GetMapping("/borrowed/user/{memberId}")
    public ResponseEntity<List<BookDTO>> findBorrowedBooksByUserId(
            @PathVariable("memberId") int memberId) {

        List<BookDTO> borrowedBooks = bookService.findBorrowedBooksByUserId(memberId);

        return new ResponseEntity<>(borrowedBooks, HttpStatus.OK);
    }
}
