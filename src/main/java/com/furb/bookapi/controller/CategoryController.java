package com.furb.bookapi.controller;

import com.furb.bookapi.model.Category;
import com.furb.bookapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações relacionadas a categorias.
 * Mapeia requisições HTTP para os métodos correspondentes no {@link CategoryService}.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Retorna todas as categorias.
     * @return Lista de categorias.
     */
    @GetMapping
    public List<Category> getAll() {
        return categoryService.findAll();
    }

    /**
     * Busca uma categoria pelo ID.
     * @param id Identificador da categoria.
     * @return Resposta HTTP 200 com a categoria ou 404 se não encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova categoria.
     * @param category Dados da categoria a ser criada.
     * @return Categoria criada.
     */
    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryService.save(category);
    }

    /**
     * Atualiza uma categoria existente.
     * @param id Identificador da categoria.
     * @param category Novos dados da categoria.
     * @return Resposta HTTP 200 com a categoria atualizada ou 404 se não encontrada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.findById(id).map(existing -> {
            category.setId(id);
            return ResponseEntity.ok(categoryService.save(category));
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove uma categoria pelo ID.
     * @param id Identificador da categoria.
     * @return Resposta HTTP 204 (No Content) em caso de sucesso.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}