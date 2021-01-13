package com.pgbezerra.securitysample.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.pgbezerra.securitysample.model.entity.Product;
import com.pgbezerra.securitysample.repository.ProductRepository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
	
	
	private static final List<Product> products = new ArrayList<>();

	@Override
	public Product insert(Product product) {
		if(products.isEmpty())
			product.setId(1L);
		else
			product.setId(products.get(products.size() -1).getId() + 1L);
		products.add(product);
		return product;
	}

	@Override
	public List<Product> findaAll() {
		return products;
	}

	@Override
	public Product findById(Long id) {
		return products.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public boolean update(Product product) {
		Product oldProduct = findById(product.getId());
		updateData(oldProduct, product);
		return true;
	}

	private void updateData(Product oldProduct, Product product) {
		oldProduct.setName(product.getName());
		oldProduct.setValue(product.getValue());
		oldProduct.setCategory(product.getCategory());
	}

	@Override
	public boolean deleteById(Long id) {
		Product product = findById(id);
		products.remove(product);
		return true;
	}

}
