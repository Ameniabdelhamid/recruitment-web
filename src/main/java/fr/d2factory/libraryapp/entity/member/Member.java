package fr.d2factory.libraryapp.entity.member;

import fr.d2factory.libraryapp.service.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
    private String firstName;
    private String lastName;
    private String email;
    /**
     * An initial sum of money the member has
     */
    private float wallet;
    /**
     * Determine if a member has returned a book late or not
     */
    private boolean isLate = false;

    Member(String firstName, String lastName, String email, float wallet, boolean isLate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.wallet = wallet;
        this.isLate = isLate;
    }

    Member() {
    }

    /**
     * The member should pay their books when they are returned to the exception
     *
     * @param numberOfDays the number of days they kept the entity
     */
    public abstract void payBook(int numberOfDays);

    /**
     * The member should keep their books for a limited period
     */
    public abstract int getNbrOfMaxDays();

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }
}
