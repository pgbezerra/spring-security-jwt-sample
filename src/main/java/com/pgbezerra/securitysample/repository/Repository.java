package com.pgbezerra.securitysample.repository;

import java.util.List;

public interface Repository<T, P> {
	
	T insert(T obj);
	List<T> findaAll();
	T findById(P id);
	boolean update(T obj);
	boolean deleteById(P id);
	
}
