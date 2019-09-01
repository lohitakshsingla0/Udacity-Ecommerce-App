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
 * @author Lohitaksh
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<Customer> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<Customer> findByUserName(@PathVariable String username) {
		Customer user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<Customer> createUser(@RequestBody CreateUserRequest createUserRequest) {
		Customer user = new Customer();

		if (createUserRequest.getUsername() == null || createUserRequest.getPassword() == null) {
			return ResponseEntity.badRequest().build();
		}

		if (createUserRequest.getPassword().length() < 8 || createUserRequest.getUsername().length() < 1) {
			return ResponseEntity.badRequest().build();
		}
		
		if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			return ResponseEntity.badRequest().build();
		}

		user.setUsername(createUserRequest.getUsername());
		user.setPassword(bcryptEncoder.encode(createUserRequest.getPassword()));
		
		Cart cart = new Cart();
		cartRepository.save(cart);
		
		user.setCart(cart);
		userRepository.save(user);
		
		return ResponseEntity.ok(user);
	}

}
