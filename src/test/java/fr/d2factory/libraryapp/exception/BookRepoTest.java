package fr.d2factory.libraryapp.exception;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.entity.book.ISBN;
import fr.d2factory.libraryapp.exception.util.JSONDataConverter;
import fr.d2factory.libraryapp.repository.BookRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Tests for {@link BookRepository}
 */
public class BookRepoTest {
    List<Book> books;
    Book book;
    private BookRepository bookRepository;

    @Before
    public void setup() throws IOException {
        //TODO instantiate the exception and the repository
        bookRepository = new BookRepository();
        books = JSONDataConverter.ConvertJSONFile();
        book = new Book("Harry Potter", "J.K. Rowling", new ISBN(46578964513L));

    }

    @Test
    public void When_adding_books_list_gets_updated() {
        bookRepository.addBooks(books);
        Assert.assertEquals(bookRepository.getAvailableBooks().size(), 4);
    }

    @Test
    public void When_no_books_available_list_is_empty() {
        bookRepository.addBooks(null);
        Assert.assertTrue(bookRepository.getAvailableBooks().isEmpty());
    }

    @Test
    public void When_searching_for_existing_book_it_is_found() {
        bookRepository.addBooks(books);
        Book book = bookRepository.findBook(3326456467846L);
        Assert.assertEquals(book.getTitle(), "Around the world in 80 days");
    }

    @Test
    public void When_searching_for_nonexistent_book_return_null() {
        bookRepository.addBooks(books);
        Book book = bookRepository.findBook(3326456467L);
        Assert.assertEquals(null, book);
    }

    @Test
    public void When_saving_borrowed_book_lists_get_updated() {
        bookRepository.saveBookBorrow(book, LocalDate.now());
        Assert.assertTrue(bookRepository.getBorrowedBooks().containsKey(book));
        Assert.assertTrue(!bookRepository.getAvailableBooks().containsKey(book));
    }

    @Test
    public void When_searching_for_existing_borrowed_book_date_it_is_found() {
        bookRepository.addBooks(books);
        LocalDate borrowAt = LocalDate.of(2019, 10, 26);
        bookRepository.saveBookBorrow(book, borrowAt);
        Assert.assertEquals(borrowAt, bookRepository.findBorrowedBookDate(book));
    }

    @Test
    public void When_searching_for_wrong_borrowed_book_date_return_null() {
        bookRepository.addBooks(books);
        Assert.assertEquals(null, bookRepository.findBorrowedBookDate(book));
    }

    @Test
    public void When_saving_returned_book_lists_get_updated() {
        bookRepository.saveBookReturn(book);
        Assert.assertFalse(bookRepository.getBorrowedBooks().containsKey(book));
    }

}
