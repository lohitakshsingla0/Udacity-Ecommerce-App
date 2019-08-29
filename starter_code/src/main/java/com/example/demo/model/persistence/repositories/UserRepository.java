package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Customer;

/**
 * The Interface UserRepository.
 */
public interface UserRepository extends JpaRepository<Customer, Long> {
	
	/**
	 * Find by username.
	 *
	 * @param username the username
	 * @return the customer
	 */
	Customer findByUsername(String username);
}
