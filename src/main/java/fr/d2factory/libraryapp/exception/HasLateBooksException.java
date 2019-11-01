package fr.d2factory.libraryapp.exception;

/**
 * This exception is thrown when a member who owns late books tries to borrow another entity
 */
public class HasLateBooksException extends RuntimeException {
    public HasLateBooksException(String errorMessage) {
        super(errorMessage);
    }
}
