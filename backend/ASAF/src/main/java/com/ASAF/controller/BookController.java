package com.ASAF.controller;

import com.ASAF.dto.BookDTO;
import com.ASAF.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/quantity/{class_code}/{region_code}/{generation_code}")
    public ResponseEntity<List<BookDTO>> findBookDTOsByClassRegionAndGenerationTest(
            @PathVariable("class_code") int class_code,
            @PathVariable("region_code") int region_code,
            @PathVariable("generation_code") int generation_code) {

        List<BookDTO> books = bookService.findBookDTOsByClassRegionAndGenerationTest(class_code, region_code, generation_code);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/distinct/{class_code}/{region_code}/{generation_code}")
    public ResponseEntity<Map<String, Object>> findDistinctBookDTOsWithCountByClassRegionAndGeneration(
            @PathVariable("class_code") int class_code,
            @PathVariable("region_code") int region_code,
            @PathVariable("generation_code") int generation_code) {

        Map<String, Object> response = bookService.findDistinctBookDTOsWithCountByClassRegionAndGeneration(class_code, region_code, generation_code);
        return ResponseEntity.ok(response);
    }
}
