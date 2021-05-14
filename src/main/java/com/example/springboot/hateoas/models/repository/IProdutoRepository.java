package com.example.springboot.hateoas.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.hateoas.models.Produto;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long>{

}
