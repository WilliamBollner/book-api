package com.furb.bookapi.controller;

import com.furb.bookapi.model.Author;
import com.furb.bookapi.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Indica que esta classe é um controlador REST
@RequestMapping("/api/authors") // Define o prefixo para todas as rotas deste controller
public class AuthorController {

    // Injeção automática do serviço de autores
    @Autowired
    private AuthorService authorService;

    // Endpoint para listar todos os autores
    @GetMapping
    public List<Author> getAll() {
        return authorService.findAll();
    }

    // Endpoint para buscar um autor específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id) {
        return authorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para criar um novo autor
    @PostMapping
    public Author create(@RequestBody Author author) {
        return authorService.save(author);
    }

    // Endpoint para atualizar um autor existente
    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Long id, @RequestBody Author author) {
        return authorService.findById(id).map(existing -> {
            author.setId(id);
            return ResponseEntity.ok(authorService.save(author));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para deletar um autor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deteAuthor(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}