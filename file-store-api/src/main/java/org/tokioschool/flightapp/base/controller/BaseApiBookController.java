package org.tokioschool.flightapp.base.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tokioschool.flightapp.base.dto.*;
import org.tokioschool.flightapp.base.service.BookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/base/api")
@Validated
public class BaseApiBookController {

    private final BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<PageDTO<BookDTO>> searchBook(
            @Valid @RequestParam(value="genre", required = false)String genre,
            @Valid @RequestParam(value="page", required = false, defaultValue = "0")int page,
            @Valid @Min(1) @Max(100)
            @RequestParam(value = "page_size", required = false,defaultValue = "10")int pageSize) {

        PageDTO<BookDTO> pageDTO = bookService.searchBooksByPageIdAndPageSize(
                BookSearchRequestDTO.builder().genre(genre).page(page).pageSize(pageSize).build());

        return ResponseEntity.ok(pageDTO);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("bookId") int bookId){
        BookDTO bookDTO = bookService.getBookByBookId(bookId);
        return ResponseEntity.ok(bookDTO);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBook(
            @RequestBody @Valid BookRequestDTO bookRequestDTO){
        BookDTO bookDTO = bookService.createBook(bookRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDTO);
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<BookDTO> editBook(
            @PathVariable("bookId") int bookId,
            @RequestBody @Valid BookRequestDTO bookRequestDTO){

        BookDTO bookDTO = bookService.editBook(bookId,bookRequestDTO);
        return ResponseEntity.ok(bookDTO);
    }

    @PatchMapping("/books/{bookId}")
    public ResponseEntity<BookDTO> patchBook(
            @PathVariable("bookId") int bookId,
            @RequestBody @Valid BookGenreRequestDTO bookGenreRequestDTO){

        BookDTO bookDTO = bookService.editBookGenre(bookId, bookGenreRequestDTO);

        return ResponseEntity.ok(bookDTO);
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") int bookId){
        bookService.deleteBookById(bookId);
        return ResponseEntity.noContent().build();
    }

}
