package com.pgbezerra.securitysample.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.pgbezerra.securitysample.model.entity.Category;
import com.pgbezerra.securitysample.repository.CategoryRepository;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
	
	private static final List<Category> categories = new ArrayList<>();

	@Override
	public Category insert(Category category) {
		if(categories.isEmpty())
			category.setId(1);
		else
			category.setId(categories.get(categories.size() -1).getId() + 1);
		categories.add(category);
		return category;
	}

	@Override
	public List<Category> findaAll() {
		return categories;
	}

	@Override
	public Category findById(Integer id) {
		return categories.stream().filter(category -> category.getId().equals(id)).findFirst().orElse(null);
	}

	@Override
	public boolean update(Category category) {
		Category oldCategory = findById(category.getId());
		updateData(oldCategory, category);
		return true;
	}

	private void updateData(Category oldCategory, Category category) {
		oldCategory.setName(category.getName());
	}

	@Override
	public boolean deleteById(Integer id) {
		Category category = findById(id);
		categories.remove(category);
		return true;
	}


}
