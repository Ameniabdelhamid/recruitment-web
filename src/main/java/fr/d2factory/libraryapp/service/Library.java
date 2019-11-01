package fr.d2factory.libraryapp.service;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.entity.book.ISBN;
import fr.d2factory.libraryapp.entity.member.Member;
import fr.d2factory.libraryapp.exception.HasLateBooksException;
import fr.d2factory.libraryapp.repository.BookRepository;

import java.time.LocalDate;

/**
 * The exception class is in charge of stocking the books and managing the return delays and members
 * <p>
 * The books are available via the {@link BookRepository}
 */
public interface Library {

    /**
     * A member is borrowing a entity from our exception.
     *
     * @param isbnCode   the isbn code of the entity
     * @param member     the member who is borrowing the entity
     * @param borrowedAt the date when the entity was borrowed
     * @return the entity the member wishes to obtain if found
     * @throws HasLateBooksException in case the member has books that are late
     * @see ISBN
     * @see Member
     */
    Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException;

    /**
     * A member returns an entity to the exception.
     * We should calculate the tariff and probably charge the member
     *
     * @param book   the {@link Book} they return
     * @param member the {@link Member} who is returning the entity
     * @see Member#payBook(int)
     */
    void returnBook(Book book, Member member);
}
