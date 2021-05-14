package com.example.springboot.hateoas.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.hateoas.models.Produto;
import com.example.springboot.hateoas.models.repository.IProdutoRepository;

@RestController
public class ProdutoResource {
	
	@Autowired
	private IProdutoRepository produtoRepository;
	
	@GetMapping("/produtos")
	public ResponseEntity<List<Produto>> getAllProdutos(){
		List<Produto> produtosList = produtoRepository.findAll();
		if(produtosList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			for(Produto produto : produtosList) {
				long id = produto.getIdProduto();
				produto.add(linkTo(methodOn(ProdutoResource.class).getOneProduto(id)).withSelfRel());
			}
			return new ResponseEntity<List<Produto>>(produtosList, HttpStatus.OK);
		}
	}
	
	@GetMapping("/produtos/{id}")
	public ResponseEntity<Produto> getOneProduto(@PathVariable(value="id") long id){
		Optional<Produto> produtoO = produtoRepository.findById(id);
		if(!produtoO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			produtoO.get().add(linkTo(methodOn(ProdutoResource.class).getAllProdutos()).withRel("Lista de Produtos"));
			return new ResponseEntity<Produto>(produtoO.get(), HttpStatus.OK);
		}
	}
	
	@PostMapping("/produtos")
	public ResponseEntity<Produto> saveProduto(@RequestBody @Valid Produto produto) {
		Produto produtoSalvo = produtoRepository.save(produto);
		produtoSalvo.add(linkTo(methodOn(ProdutoResource.class).getOneProduto(produtoSalvo.getIdProduto())).withSelfRel());
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/produtos/{id}")
	public ResponseEntity<?> deleteProduto(@PathVariable(value="id") long id) {
		Optional<Produto> produtoO = produtoRepository.findById(id);
		if(!produtoO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			produtoRepository.delete(produtoO.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	@PutMapping("/produtos/{id}")
	public ResponseEntity<Produto> updateProduto(@PathVariable(value="id") long id,
										@RequestBody @Valid Produto produto) {
		Optional<Produto> produtoO = produtoRepository.findById(id);
		if(!produtoO.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			produto.setIdProduto(produtoO.get().getIdProduto());
			return new ResponseEntity<Produto>(produtoRepository.save(produto), HttpStatus.OK);
		}
	}
}
