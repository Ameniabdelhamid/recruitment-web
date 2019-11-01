package fr.d2factory.libraryapp.repository;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.entity.book.ISBN;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public BookRepository() {
    }

    public void addBooks(List<Book> books) {
        /* Add books to the list of available books */
        if (books != null)
            availableBooks = books.stream()
                    .collect(Collectors.toMap(Book::getIsbn, book -> book));
    }


    public Book findBook(long isbnCode) {
        /* Search for a specific book by its isbn code */
        return availableBooks.entrySet().stream()
                .filter(e -> e.getKey().getIsbnCode() == isbnCode)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        /* Save the borrowed book within the list of borrowed ones, at the same time remove it from list of available books */
        borrowedBooks.put(book, borrowedAt);
        availableBooks.remove(book.getIsbn(), borrowedAt);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        /* Return the date of borrowing a book */
        return borrowedBooks.get(book);
    }

    public void saveBookReturn(Book book) {
        /* Delete borrowed book from list of borrowed books and re-add it to list of available ones */
        availableBooks.put(book.getIsbn(), book);
        borrowedBooks.remove(book);

    }

    public Map<ISBN, Book> getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(Map<ISBN, Book> availableBooks) {
        this.availableBooks = availableBooks;
    }

    public Map<Book, LocalDate> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(Map<Book, LocalDate> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}