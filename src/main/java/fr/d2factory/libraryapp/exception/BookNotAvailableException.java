package fr.d2factory.libraryapp.exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String errorMessage) {
        super(errorMessage);
    }
}
