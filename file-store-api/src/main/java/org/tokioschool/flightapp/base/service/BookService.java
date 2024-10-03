package org.tokioschool.flightapp.base.service;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.base.domain.Author;
import org.tokioschool.flightapp.base.domain.Book;
import org.tokioschool.flightapp.base.dto.*;
import org.tokioschool.flightapp.core.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookService {

  private List<Book> books;
  private List<Author> authors;

  @PostConstruct
  public void postConstruct() {

    Faker faker = new Faker();

    authors =
        new ArrayList<>(
            IntStream.range(0, 10)
                .mapToObj(i -> Author.builder().id(i).name(faker.book().author()).build())
                .toList());

    books =
        new ArrayList<>(
            IntStream.range(0, 101)
                .mapToObj(
                    i ->
                        Book.builder()
                            .id(i)
                            .title(faker.book().title())
                            .genre(faker.book().genre())
                            .authors(List.of(authors.get((int) (Math.random() * 10))))
                            .build())
                .toList());
  }

  Author getAuthorById(int authorId) {
    return authors.stream()
        .filter(a -> a.getId() == authorId)
        .findFirst()
        .orElseThrow(
            () -> new NotFoundException("Author with id:%s not found".formatted(authorId)));
  }

  Book getBookById(int bookId) {
    return books.stream()
        .filter(b -> b.getId() == bookId)
        .findFirst()
        .orElseThrow(() -> new NotFoundException("Book with id:%s not found".formatted(bookId)));
  }

  public BookDTO getBookByBookId(int bookId) {
    return toBookDTO(getBookById(bookId));
  }

  public PageDTO<BookDTO> searchBooksByPageIdAndPageSize(
      BookSearchRequestDTO bookSearchRequestDTO) {

    List<Book> filteredBooks =
        Optional.ofNullable(StringUtils.trimToNull(bookSearchRequestDTO.getGenre()))
            .map(String::toLowerCase)
            .map(
                genre ->
                    books.stream().filter(b -> b.getGenre().toLowerCase().contains(genre)).toList())
            .orElse(books);
    int start = bookSearchRequestDTO.getPage() * bookSearchRequestDTO.getPageSize();

    if (start >= filteredBooks.size()) {
      return PageDTO.<BookDTO>builder()
          .items(List.of())
          .page(bookSearchRequestDTO.getPage())
          .pageSize(bookSearchRequestDTO.getPageSize())
          .total(filteredBooks.size())
          .build();
    }

    int end = Math.min(start + bookSearchRequestDTO.getPageSize(), filteredBooks.size());

    List<BookDTO> items =
            IntStream.range(start, end).mapToObj(filteredBooks::get).map(this::toBookDTO).toList();

    return PageDTO.<BookDTO>builder()
        .page(bookSearchRequestDTO.getPage())
        .pageSize(bookSearchRequestDTO.getPageSize())
        .total(filteredBooks.size())
        .items(items)
        .build();
  }

  public void deleteBookById(int bookId){
    Optional<Book> maybeBook = books.stream().filter(b -> b.getId() == bookId).findFirst();
    maybeBook.ifPresent(books::remove);
  }


  private BookDTO toBookDTO(Book book) {

    return BookDTO.builder()
        .id(book.getId())
        .title(book.getTitle())
        .genre(book.getGenre())
        .authorId(book.getAuthors().stream().map(Author::getId).toList())
        .build();
  }

  private synchronized int nextBookId(){
    return books.stream().map(Book::getId).reduce(Math::max).map(id -> id +1).orElse(-1);
  }

  public BookDTO createBook(BookRequestDTO bookRequestDTO){
    Book book =
            Book.builder()
                    .id(nextBookId())
                    .authors(List.of(getAuthorById(bookRequestDTO.getAuthorId())))
                    .title(bookRequestDTO.getTitle())
                    .genre(bookRequestDTO.getGenre())
                    .build();

    books.add(book);

    return toBookDTO(book);
  }

  public BookDTO editBook(int bookId, BookRequestDTO bookRequestDTO){

    Book book =  getBookById(bookId);
    book.setTitle(bookRequestDTO.getTitle());
    book.setGenre(bookRequestDTO.getGenre());
    book.setAuthors(List.of(getAuthorById(bookRequestDTO.getAuthorId())));

    return toBookDTO(book);

  }

  public BookDTO editBookGenre(int bookId, BookGenreRequestDTO bookGenreRequestDTO){
    Book book = getBookById(bookId);
    book.setGenre(bookGenreRequestDTO.getGenre());
    return toBookDTO(book);
  }


}
