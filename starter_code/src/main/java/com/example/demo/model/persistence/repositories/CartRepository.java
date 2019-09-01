package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Customer;

/**
 * The Interface CartRepository.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
	
	/**
	 * Find by user.
	 *
	 * @param user the user
	 * @return the cart
	 */
	Cart findByUser(Customer user);
}
