package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

/**
 * The Class UserController.
 *
 * @author Manmeet
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/** The cart repository. */
	@Autowired
	private CartRepository cartRepository;
	
	/** The bcrypt encoder. */
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@GetMapping("/id/{id}")
	public ResponseEntity<Customer> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	/**
	 * Find by user name.
	 *
	 * @param username the username
	 * @return the response entity
	 */
	@GetMapping("/{username}")
	public ResponseEntity<Customer> findByUserName(@PathVariable String username) {
		Customer user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	/**
	 * Creates the user.
	 *
	 * @param createUserRequest the create user request
	 * @return the response entity
	 */
	@PostMapping("/create")
	public ResponseEntity<Customer> createUser(@RequestBody CreateUserRequest createUserRequest) {
		Customer user = new Customer();

		// Check if the values are null or not
		if (createUserRequest.getUsername() == null || createUserRequest.getPassword() == null) {
			return ResponseEntity.badRequest().build();
		}

		// Sanity check for username and password
		if (createUserRequest.getPassword().length() < 8 || createUserRequest.getUsername().length() < 1) {
			return ResponseEntity.badRequest().build();
		}
		
		// Sanity check for confirmedPassword 
		if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			return ResponseEntity.badRequest().build();
		}

		user.setUsername(createUserRequest.getUsername());
		// Hashing the password
		user.setPassword(bcryptEncoder.encode(createUserRequest.getPassword()));
		
		Cart cart = new Cart();
		cartRepository.save(cart);
		
		user.setCart(cart);
		userRepository.save(user);
		
		return ResponseEntity.ok(user);
	}

}
