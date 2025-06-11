package com.furb.bookapi.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.furb.bookapi.model.Book;
import com.furb.bookapi.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {
        if (book.getTitle() == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Title and ISBN are required");
        }
        return repository.save(book);
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
    }

    public Book update(Long id, Book book) {
        Book existing = findById(id);
        existing.setTitle(book.getTitle());
        existing.setIsbn(book.getIsbn());
        existing.setPublicationYear(book.getPublicationYear());
        existing.setCategories(book.getCategories());
        return repository.save(existing);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete book linked to other records");
        }
    }
}
