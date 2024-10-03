package org.tokioschool.flightapp.base.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tokioschool.flightapp.base.dto.BookDTO;
import org.tokioschool.flightapp.base.dto.BookSearchRequestDTO;
import org.tokioschool.flightapp.base.dto.PageDTO;
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

}
