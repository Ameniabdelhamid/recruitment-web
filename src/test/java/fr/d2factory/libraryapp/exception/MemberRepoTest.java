package fr.d2factory.libraryapp.exception;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.entity.book.ISBN;
import fr.d2factory.libraryapp.entity.member.Resident;
import fr.d2factory.libraryapp.entity.member.Student;
import fr.d2factory.libraryapp.exception.util.JSONDataConverter;
import fr.d2factory.libraryapp.repository.BookRepository;
import fr.d2factory.libraryapp.repository.MemberRepository;
import fr.d2factory.libraryapp.service.Library;
import fr.d2factory.libraryapp.service.LibraryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Tests for {@link MemberRepository}
 */
public class MemberRepoTest {
    private List<Book> books;
    private Book book;
    private Resident resident;
    private Student student;
    private Library library;
    private BookRepository bookRepository;
    private MemberRepository memberRepository;

    @Before
    public void setup() throws IOException {
        // instantiate the exception and the repository
        bookRepository = new BookRepository();
        memberRepository = new MemberRepository();
        books = JSONDataConverter.ConvertJSONFile();
        book = new Book("Harry Potter", "J.K. Rowling", new ISBN(46578964513L));
        student = new Student("Sam", "Doe", "sam@gmail.com", 120, false);
        resident = new Resident();
        bookRepository.addBooks(books);
        library = new LibraryService(bookRepository, memberRepository);
        //Borrow some books
        library.borrowBook(46578964513L, student, LocalDate.now());
        library.borrowBook(3326456467846L, student, LocalDate.of(2019, 10, 29));
    }

    @Test
    public void When_searching_for_borrowed_books_by_member_list_is_found() {
        List<Book> bookByStudent = memberRepository.getBorrowedBooksByMember(student);
        Assert.assertEquals(bookByStudent.size(), 2);
    }

    @Test
    public void When_searching_for_borrowed_books_by_member_not_borrowing_list_is_empty() {
        List<Book> bookByStudent = memberRepository.getBorrowedBooksByMember(resident);
        Assert.assertEquals(bookByStudent.size(), 0);
    }

    @Test
    public void When_saving_borrowing_members_list_gets_updated() {
        memberRepository.saveBorrowedBookMember(book, resident);
        Assert.assertTrue(memberRepository.getBorrowedBooksMembers().containsKey(resident));
    }


}
