package com.pgbezerra.securitysample.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgbezerra.securitysample.model.entity.Product;
import com.pgbezerra.securitysample.repository.ProductRepository;

@RestController
@RequestMapping(value = "products")
public class ProductResource {
	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping
	public ResponseEntity<List<Product>> findAll(){
		return ResponseEntity.ok(productRepository.findaAll());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> findById(@PathVariable(value = "id") Long id){
		return ResponseEntity.ok(productRepository.findById(id));
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable(value = "id") Long id, @RequestBody Product product){
		product.setId(id);
		productRepository.update(product);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteBydId(@PathVariable(value = "id") Long id){
		productRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Product product){
		product = productRepository.insert(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

}
