package com.example.demo.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestCartController {
    
	/** The cart controller. */
    @InjectMocks
    private CartController cartController;

    /** The user repo. */
    @Mock
    private UserRepository userRepo;

    /** The cart repo. */
    @Mock
    private CartRepository cartRepo;

    /** The item repo. */
    @Mock
    private ItemRepository itemRepo;

    /** The mock mvc. */
    @Autowired
    private MockMvc mockMvc;

    /** The object mapper. */
    @Autowired
    private ObjectMapper objectMapper;

    /** The request. */
    @Autowired
    private MockHttpServletRequest request;

    /** The user. */
    private Customer user;

    /** The user request. */
    private CreateUserRequest userRequest;

    /**
     * Inits the.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws Exception               the exception
     */
    @BeforeEach
    public void init() throws JsonProcessingException, Exception {
	userRequest = new CreateUserRequest();
	userRequest.setUsername("ManmeetTestThree");
	userRequest.setPassword("password");
	userRequest.setConfirmPassword("password");

	MvcResult entityResult = mockMvc.perform(
		MockMvcRequestBuilders.post("/api/user/create").content(objectMapper.writeValueAsString(userRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andReturn();
	user = objectMapper.readValue(entityResult.getResponse().getContentAsString(), Customer.class);

	MvcResult result = mockMvc
		.perform(MockMvcRequestBuilders.post("/login").content(objectMapper.writeValueAsString(userRequest)))
		.andExpect(status().isOk()).andReturn();
	request.addParameter("Authorization", result.getResponse().getHeader("Authorization"));

    }

    /**
     * Test cart controller.
     *
     * @throws JsonProcessingException the json processing exception
     * @throws Exception               the exception
     */
    @Test
    @DisplayName("Test method to test addition and deletion of items in the cart")
    public void testCartController() throws JsonProcessingException, Exception {

	when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);

	// This particular code test the adding of item in the cart. These test handles
	// negative scenarios too.

	ModifyCartRequest cartRequest = new ModifyCartRequest();
	cartRequest.setItemId(2L);
	cartRequest.setQuantity(8);
	cartRequest.setUsername("ManmeetTestThree");

	mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
		.content(objectMapper.writeValueAsString(cartRequest)).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).header("Authorization", request.getParameter("Authorization")))
		.andExpect(status().isOk());

	mockMvc.perform(
		MockMvcRequestBuilders.post("/api/cart/addToCart").content(objectMapper.writeValueAsString(cartRequest))
			.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());

	cartRequest.setUsername("testValue");
	mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/addToCart")
		.content(objectMapper.writeValueAsString(cartRequest)).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).header("Authorization", request.getParameter("Authorization")))
		.andExpect(status().isNotFound());

	// This particular code test the deletion of item from the cart. These test
	// handles negative test scenarios too.

	ModifyCartRequest cartDeleteRequest = new ModifyCartRequest();
	cartDeleteRequest.setItemId(2L);
	cartDeleteRequest.setQuantity(3);
	cartDeleteRequest.setUsername("ManmeetTestThree");

	mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/removeFromCart")
		.content(objectMapper.writeValueAsString(cartDeleteRequest)).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).header("Authorization", request.getParameter("Authorization")))
		.andExpect(status().isOk());

	mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/removeFromCart")
		.content(objectMapper.writeValueAsString(cartDeleteRequest)).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());

	cartDeleteRequest.setUsername("testValue");
	mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/removeFromCart")
		.content(objectMapper.writeValueAsString(cartDeleteRequest)).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).header("Authorization", request.getParameter("Authorization")))
		.andExpect(status().isNotFound());
    }
}
