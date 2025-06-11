package com.furb.bookapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furb.bookapi.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {}
