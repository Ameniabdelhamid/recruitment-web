package fr.d2factory.libraryapp.service;


import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.entity.member.Member;
import fr.d2factory.libraryapp.exception.BookNotAvailableException;
import fr.d2factory.libraryapp.exception.HasLateBooksException;
import fr.d2factory.libraryapp.repository.BookRepository;
import fr.d2factory.libraryapp.repository.MemberRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class LibraryService implements Library {
    private BookRepository bookRepo;
    private MemberRepository memberRepo;

    public LibraryService(BookRepository bookRepo, MemberRepository memberRepo) {
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
    }

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException, BookNotAvailableException {

        /* Check if a member is late with their previous  books */
        memberRepo.getBorrowedBooksByMember(member).forEach(book -> {
            LocalDate borrowedDate = bookRepo.findBorrowedBookDate(book);
            if (borrowedDate != null && ChronoUnit.DAYS.between(borrowedDate, LocalDate.now()) > member.getNbrOfMaxDays()) {
                throw new HasLateBooksException("You have books not returned yet you cannot borrow new books !");
            }
        });
        /* Check if a book is available to borrow it */
        Book book = this.bookRepo.findBook(isbnCode);
        Optional<LocalDate> bookToBorrow = Optional.ofNullable(this.bookRepo.findBorrowedBookDate(book));
        if (bookToBorrow.isPresent()) {
            throw new BookNotAvailableException("No such book available !");
        }
        /* Save the borrowed book */
        bookRepo.saveBookBorrow(book, borrowedAt);
        /* Save member who have borrowed book */
        memberRepo.saveBorrowedBookMember(book, member);
        return book;
    }

    @Override
    public void returnBook(Book book, Member member) {
        LocalDate dateOfBorrow = this.bookRepo.findBorrowedBookDate(book);
        /* Check if a member has borrowed the book */
        if (!memberRepo.getBorrowedBooksByMember(member).isEmpty() && memberRepo.getBorrowedBooksByMember(member).contains(book)) {
            /* Calculate how many days a member has kept the borrowed book */
            int numberOfDays = (int) ChronoUnit.DAYS.between(dateOfBorrow, LocalDate.now());
            member.payBook(numberOfDays);
            bookRepo.saveBookReturn(book);

        }

    }
}
