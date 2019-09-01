package com.example.demo.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.UserOrder;

/**
 * The Interface OrderRepository.
 */
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	
	/**
	 * Find by user.
	 *
	 * @param user the user
	 * @return the list
	 */
	List<UserOrder> findByUser(Customer user);
}
