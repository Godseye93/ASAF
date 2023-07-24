package com.ASAF.service;

import com.ASAF.dto.BookDTO;
import com.ASAF.entity.BookEntity;
import com.ASAF.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 도서 등록
    public BookDTO registerBook(BookDTO bookDTO) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBook_name(bookDTO.getBook_name());
        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setPublisher(bookDTO.getPublisher());
        BookEntity savedEntity = bookRepository.save(bookEntity);
        bookDTO.setBook_number(savedEntity.getBook_number());
        return bookDTO;
    }

    // 도서 정보 수정
    public BookDTO updateBook(int bookNumber, BookDTO bookDTO) {
        BookEntity bookEntity = bookRepository.findById(bookNumber).orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
        bookEntity.setBook_name(bookDTO.getBook_name());
        bookEntity.setAuthor(bookDTO.getAuthor());
        bookEntity.setPublisher(bookDTO.getPublisher());
        BookEntity updatedEntity = bookRepository.save(bookEntity);
        return new BookDTO(updatedEntity);
    }

    // 도서 정보 삭제
    public void deleteBook(int bookNumber) {
        bookRepository.deleteById(bookNumber);
    }

    // 도서 정보 가져오기 (한 권)
    public BookDTO getBook(int bookNumber) {
        BookEntity bookEntity = bookRepository.findById(bookNumber).orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
        return new BookDTO(bookEntity);
    }

    // 도서 정보 가져오기 (전체 도서)
    public List<BookDTO> getAllBooks() {
        List<BookEntity> bookEntities = bookRepository.findAll();
        return bookEntities.stream()
                .map(bookEntity -> new BookDTO(bookEntity))
                .collect(Collectors.toList());
    }
}
