package fr.d2factory.libraryapp.repository;

import fr.d2factory.libraryapp.entity.book.Book;
import fr.d2factory.libraryapp.entity.member.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRepository {
    private Map<Member, List<Book>> borrowedBooksMembers = new HashMap<>();

    public MemberRepository() {
    }

    public Map<Member, List<Book>> getBorrowedBooksMembers() {
        return borrowedBooksMembers;
    }

    /**
     * Retrieve all borrowed books by member
     */
    public List<Book> getBorrowedBooksByMember(Member member) {
        return borrowedBooksMembers.computeIfAbsent(member, k -> new ArrayList<>());
    }

    /**
     * Save all members who have borrowed a book
     */
    public void saveBorrowedBookMember(Book book, Member member) {
        List<Book> booksByMember = this.getBorrowedBooksByMember(member);
        booksByMember.add(book);
        borrowedBooksMembers.put(member, booksByMember);


    }

}
