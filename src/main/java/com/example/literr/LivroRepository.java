package com.example.literr;

import com.example.literr.classes.objects.Autor;
import com.example.literr.classes.objects.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("SELECT a FROM Autor a")
    List<Autor> findAllAutores();
}