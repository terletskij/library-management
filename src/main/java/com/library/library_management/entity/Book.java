package com.library.library_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book title is required")
    @Size(min = 3, message = "Book title must be at least 3 characters")
    @Pattern(regexp = "^[A-Z].*", message = "Book title must start with a capital letter ")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Pattern(regexp = "^[A-Z][a-z]+\\s[A-Z][a-z]+$", message = "Author must contain two capitalized words")
    @Column(nullable = false)
    private String author;

    @Min(value = 0, message = "Amount cannot be negative")
    @Column(nullable = false)
    private Integer amount;

    public Book() {}

    public Book(String title, String author, Integer amount) {
        this.title = title;
        this.author = author;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
