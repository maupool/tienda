/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tienda.repository;

import com.tienda.domain.Categoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository {
    public List<Categoria> findByActiveTrue();

    public List<Categoria> findAll();

    public List<Categoria> findByActivoTrue();

    public Optional<Categoria> findById(Integer idCategoria);

    public void save(Categoria categoria);

    public boolean existsById(Integer idCategoria);

    public void deleteById(Integer idCategoria);
}
