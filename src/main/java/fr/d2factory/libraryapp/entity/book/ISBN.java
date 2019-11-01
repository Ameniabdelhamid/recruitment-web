package fr.d2factory.libraryapp.entity.book;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * A simple representation of ISBN entity
 */
public class ISBN {
    @JsonProperty("isbnCode")
    private long isbnCode;

    public ISBN() {
    }

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    public long getIsbnCode() {
        return isbnCode;
    }

    public void setIsbnCode(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ISBN isbn = (ISBN) o;
        return isbnCode == isbn.isbnCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbnCode);
    }
}