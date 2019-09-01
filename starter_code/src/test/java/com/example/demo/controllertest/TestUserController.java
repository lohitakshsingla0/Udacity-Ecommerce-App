package com.example.demo.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Customer;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestUserController {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CartRepository cartRepo;

    @Mock
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockHttpServletRequest request;

    private Customer user;

    private CreateUserRequest userRequest;

    @BeforeEach
    public void init() throws Exception {

	userRequest = new CreateUserRequest();
	userRequest.setUsername("LohitakshTestTwo");
	userRequest.setPassword("password1");
	userRequest.setConfirmPassword("password1");

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

    @Test
    @DisplayName("Test method to test user fetching controllers using unique username and id")
    public void testGetUserWithUsernameAndId() throws Exception {

	when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
	when(userRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));

	mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{username}", "LohitakshTestTwo")
		.accept(MediaType.APPLICATION_JSON).header("Authorization", request.getParameter("Authorization")))
		.andExpect(status().isOk());

	mockMvc.perform(
		MockMvcRequestBuilders.get("/api/user/{username}", "LohitakshTestTwo").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());

	mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{username}", "testValue")
		.accept(MediaType.APPLICATION_JSON).header("Authorization", request.getParameter("Authorization")))
		.andExpect(status().isNotFound());

	mockMvc.perform(MockMvcRequestBuilders.get("/api/user/id/{id}", user.getId()).accept(MediaType.APPLICATION_JSON)
		.header("Authorization", request.getParameter("Authorization"))).andExpect(status().isOk());

	mockMvc.perform(
		MockMvcRequestBuilders.get("/api/user/id/{id}", user.getId()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());

	mockMvc.perform(MockMvcRequestBuilders.get("/api/user/id/{id}", 80).accept(MediaType.APPLICATION_JSON)
		.header("Authorization", request.getParameter("Authorization"))).andExpect(status().isNotFound());
    }

}
