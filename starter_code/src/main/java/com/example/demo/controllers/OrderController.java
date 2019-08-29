package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

/**
 * The Class OrderController.
 *
 * @author Manmeet
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	
	/** The user repository. */
	@Autowired
	private UserRepository userRepository;
	
	/** The order repository. */
	@Autowired
	private OrderRepository orderRepository;
	
	
	/**
	 * Submit.
	 *
	 * @param username the username
	 * @return the response entity
	 */
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		Customer user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		return ResponseEntity.ok(order);
	}
	
	/**
	 * Gets the orders for user.
	 *
	 * @param username the username
	 * @return the orders for user
	 */
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		Customer user = userRepository.findByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
