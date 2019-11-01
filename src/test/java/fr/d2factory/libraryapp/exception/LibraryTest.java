package fr.d2factory.libraryapp.exception;

import fr.d2factory.libraryapp.entity.book.Book;
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
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Tests for {@link LibraryService}
 */
public class LibraryTest {
    private Library library;
    private BookRepository bookRepository;
    private MemberRepository memberRepository;

    @Before
    public void setup() throws IOException {
        //  instantiate the exception and the repository
        bookRepository = new BookRepository();
        memberRepository = new MemberRepository();
        library = new LibraryService(bookRepository, memberRepository);
        List<Book> books = JSONDataConverter.ConvertJSONFile();
        // add some test books (use BookRepository#addBooks)
        bookRepository.addBooks(books);

    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available() {
        Student student = new Student();
        student.setWallet(200);
        Book book = library.borrowBook(46578964513L, student, LocalDate.now());

        Assert.assertEquals(book.getTitle(), "Harry Potter");

        Resident resident = new Resident("Sam", "Doe", "sam@gmail.com", 300, false);
        resident.setWallet(300);
        Book books = library.borrowBook(3326456467846L, resident, LocalDate.now());
        Assert.assertEquals(books.getTitle(), "Around the world in 80 days");

        Assert.assertNotNull(books);

    }

    @Test(expected = BookNotAvailableException.class)
    public void borrowed_book_is_no_longer_available() {
        Student student = new Student("John", "Doe", "john@gmail.com", 200, false);
        Book book = library.borrowBook(3326456467846L, student, LocalDate.now());
        Assert.assertEquals(book.getTitle(), "Around the world in 80 days");
        /* try to borrow the same book */
        Student anotherStudent = new Student("Laurent", "Dupont", "laurent@gmail.com", 100, false);
        Book sameBook = library.borrowBook(3326456467846L, anotherStudent, LocalDate.now());
        Assert.assertEquals(sameBook.getTitle(), "Around the world in 80 days");


    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
        Resident resident = new Resident("Sam", "Doe", "sam@gmail.com", 300, false);
        LocalDate borrowedAt = LocalDate.of(2019, 10, 19);
        Book book = library.borrowBook(465789453149L, resident, borrowedAt);
        /* Calculate number of days a resident kept the book */
        int nbrDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
        /* Call return book method */
        library.returnBook(book, resident);
        float tax = resident.getWallet() - nbrDays * 0.1f;
        resident.setWallet(tax);
        Assert.assertEquals(tax, resident.getWallet(), 0);

    }

    @Test
    public void students_pay_10_cents_the_first_30days() {
        Student student = new Student("John", "Doe", "john@gmail.com", 200, false);
        LocalDate borrowedAt = LocalDate.of(2019, 10, 22);
        Book book = library.borrowBook(3326456467846L, student, LocalDate.now());
        /* Calculate number of days a student kept the book */
        int nbrDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
        library.returnBook(book, student);
        float tax = student.getWallet() - nbrDays * 0.1f;
        student.setWallet(tax);
        Assert.assertEquals(tax, student.getWallet(), 0);


    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days() {
        Student student = new Student();
        student.setWallet(100);
        LocalDate borrowedAt = LocalDate.of(2019, 10, 27);
        Book book = library.borrowBook(968787565445L, student, LocalDate.now());
        /* Calculate number of days a student kept the book */
        int nbrDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
        library.returnBook(book, student);
        float tax = student.getWallet();
        if (nbrDays > 15)
            tax = student.getWallet() - (nbrDays - 15) * 0.1f;
        student.setWallet(tax);
        Assert.assertEquals(tax, student.getWallet(), 0);
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {
        Student student = new Student();
        student.setWallet(150);
        LocalDate borrowedAt = LocalDate.of(2019, 9, 22);
        Book book = library.borrowBook(3326456467846L, student, LocalDate.now());
        /* Calculate number of days a student kept the book */
        int nbrDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
        library.returnBook(book, student);
        float tax = student.getWallet() - ((30 * 0.1f) + (nbrDays - 30) * 0.15f);
        student.setWallet(tax);
        Assert.assertEquals(tax, student.getWallet(), 0);

    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        Resident resident = new Resident();
        resident.setWallet(150);
        LocalDate borrowedAt = LocalDate.of(2019, 4, 22);
        Book book = library.borrowBook(3326456467846L, resident, LocalDate.now());
        /* Calculate number of days a student kept the book */
        int nbrDays = (int) ChronoUnit.DAYS.between(borrowedAt, LocalDate.now());
        library.returnBook(book, resident);
        float tax = resident.getWallet() - ((60 * 0.1f) + (nbrDays - 60) * 0.2f);
        resident.setWallet(tax);
        Assert.assertEquals(tax, resident.getWallet(), 0);

    }

    @Test(expected = HasLateBooksException.class)
    public void members_cannot_borrow_book_if_they_have_late_books() {
        /* First example with a student member */
        Student student = new Student();
        student.setWallet(330);
        LocalDate borrowedFirstBookAt = LocalDate.of(2019, 8, 1);
        /* Borrow the first book and keep it more than the limited period of 30 days */
        library.borrowBook(968787565445L, student, borrowedFirstBookAt);
        student.setLate(true);
        /* Try to borrow another book */
        Book anotherBook = library.borrowBook(46578964513L, student, LocalDate.of(2019, 10, 31));
        Assert.assertNull(anotherBook);
        /* Second example with a resident example */
        Resident resident = new Resident();
        resident.setWallet(220);
        LocalDate borrowedAt = LocalDate.of(2019, 7, 28);
        library.borrowBook(46578964513L, resident, borrowedAt);
        resident.setLate(true);
        Book book = library.borrowBook(3326456467846L, resident, LocalDate.now());
        Assert.assertNull(book);

    }

    @Test
    public void members_can_borrow_book_if_they_always_return_books_on_time() {
        Resident resident = new Resident();
        resident.setWallet(76);
        LocalDate borrowedAt = LocalDate.of(2019, 10, 26);
        Book book = library.borrowBook(3326456467846L, resident, borrowedAt);
        library.returnBook(book, resident);
        /* Now try to borrow second book */
        Book secondBook = library.borrowBook(968787565445L, resident, LocalDate.of(2019, 10, 30));
        Assert.assertEquals(secondBook.getTitle(), "Catch 22");
    }
}
