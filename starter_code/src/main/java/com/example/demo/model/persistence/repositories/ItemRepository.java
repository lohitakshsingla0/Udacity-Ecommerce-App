package com.example.demo.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Item;

/**
 * The Interface ItemRepository.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
	
	/**
	 * Find by name.
	 *
	 * @param name the name
	 * @return the list
	 */
	public List<Item> findByName(String name);

}
